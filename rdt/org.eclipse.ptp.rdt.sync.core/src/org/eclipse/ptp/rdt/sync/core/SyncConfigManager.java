/*******************************************************************************
 * Copyright (c) 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - Initial API and implementation
 *******************************************************************************/
package org.eclipse.ptp.rdt.sync.core;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.ptp.internal.rdt.sync.core.RDTSyncCorePlugin;
import org.eclipse.ptp.internal.rdt.sync.core.messages.Messages;
import org.eclipse.ptp.rdt.sync.core.exceptions.MissingConnectionException;
import org.eclipse.ptp.rdt.sync.core.listeners.ISyncConfigListener;
import org.eclipse.ptp.rdt.sync.core.resources.RemoteSyncNature;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.core.IRemoteConnectionType;
import org.eclipse.remote.core.IRemoteFileService;
import org.eclipse.remote.core.IRemoteServicesManager;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.XMLMemento;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * Main class for managing sync configurations
 * 
 * @since 3.0
 */
public class SyncConfigManager {
	private static final String SYNC_CONFIG_KEY = "project-sync-config"; //$NON-NLS-1$
	private static final String CONFIGS_ELEMENT = "sync-configs"; //$NON-NLS-1$
	private static final String CONFIG_ELEMENT = "sync-config"; //$NON-NLS-1$
	private static final String CONFIG_NAME_ELEMENT = "config-name"; //$NON-NLS-1$
	private static final String SYNC_PROVIDER_ID_ELEMENT = "sync-provider-id"; //$NON-NLS-1$
	private static final String CONNECTION_NAME_ELEMENT = "connection-name"; //$NON-NLS-1$
	private static final String LOCATION_ELEMENT = "location"; //$NON-NLS-1$
	private static final String REMOTE_SERVICES_ID_ELEMENT = "remote-services-id"; //$NON-NLS-1$
	private static final String ACTIVE_ELEMENT = "active"; //$NON-NLS-1$
	private static final String SYNC_ON_PREBUILD_ELEMENT = "sync-on-prebuild"; //$NON-NLS-1$
	private static final String SYNC_ON_POSTBUILD_ELEMENT = "sync-on-postbuild"; //$NON-NLS-1$
	private static final String SYNC_ON_SAVE_ELEMENT = "sync-on-save"; //$NON-NLS-1$
	private static final String CONFIG_PROPERTIES_ELEMENT = "config-properties"; //$NON-NLS-1$
	private static final String LOCAL_SYNC_CONFIG_NAME = "Local"; //$NON-NLS-1$
	private static final String PROJECT_LOCAL_PATH = "${project_loc}"; //$NON-NLS-1$

	private static final Map<String, ListenerList<ISyncConfigListener>> fSyncConfigListenerMap = Collections
			.synchronizedMap(new HashMap<String, ListenerList<ISyncConfigListener>>());
	private static final Map<IProject, SyncConfig> fActiveSyncConfigMap = Collections
			.synchronizedMap(new HashMap<IProject, SyncConfig>());
	private static final Map<IProject, Map<String, SyncConfig>> fSyncConfigMap = Collections
			.synchronizedMap(new HashMap<IProject, Map<String, SyncConfig>>());

	/**
	 * Add a new sync configuration to the project
	 * 
	 * @param project
	 *            project
	 * @param config
	 *            sync configuration to add to the project
	 */
	public static void addConfig(IProject project, SyncConfig config) {
		try {
			loadConfigs(project);
			doAddConfig(project, config);
			saveConfigs(project);
			fireSyncConfigAdded(project, config);
		} catch (CoreException e) {
			RDTSyncCorePlugin.log(e);
		}
	}

	/**
	 * Register to receive sync configuration events.
	 * 
	 * @param natureId
	 *            project nature ID of projects to notify of changes
	 * @param listener
	 *            listener to receive events
	 */
	public static void addSyncConfigListener(String natureId, ISyncConfigListener listener) {
		ListenerList<ISyncConfigListener> list = fSyncConfigListenerMap.get(natureId);
		if (list == null) {
			list = new ListenerList<ISyncConfigListener>();
			fSyncConfigListenerMap.put(natureId, list);
		}
		list.add(listener);
	}

	private static void doAddConfig(IProject project, SyncConfig config) {
		Map<String, SyncConfig> projConfigs = fSyncConfigMap.get(project);
		if (projConfigs == null) {
			projConfigs = new HashMap<String, SyncConfig>();
			fSyncConfigMap.put(project, projConfigs);
		}
		projConfigs.put(config.getName(), config);
		config.setProject(project);
	}

	private static boolean doRemoveConfig(IProject project, SyncConfig config) {
		Map<String, SyncConfig> projConfigs = fSyncConfigMap.get(project);
		if (projConfigs != null && projConfigs.size() > 1) {
			return projConfigs.remove(config.getName()) != null;
		}
		return false;
	}

	private static void fireSyncConfigAdded(IProject project, SyncConfig config) {
		for (Object obj : getListenersFor(project).getListeners()) {
			ISyncConfigListener listener = (ISyncConfigListener) obj;
			listener.configAdded(project, config);
		}
	}

	private static void fireSyncConfigRemoved(IProject project, SyncConfig config) {
		for (Object obj : getListenersFor(project).getListeners()) {
			ISyncConfigListener listener = (ISyncConfigListener) obj;
			listener.configRemoved(project, config);
		}
	}

	private static void fireSyncConfigSelected(IProject project, SyncConfig newConfig, SyncConfig oldConfig) {
		for (Object obj : getListenersFor(project).getListeners()) {
			ISyncConfigListener listener = (ISyncConfigListener) obj;
			listener.configSelected(project, newConfig, oldConfig);
		}
	}

	/**
	 * Get the active configuration for the project. There is always at least one active configuration for every project. Returns
	 * null if the project is not a synchronized project.
	 * 
	 * @param project
	 * @return active configuration
	 */
	public static SyncConfig getActive(IProject project) {
		try {
			if (project.hasNature(RemoteSyncNature.NATURE_ID)) {
				try {
					loadConfigs(project);
					return fActiveSyncConfigMap.get(project);
				} catch (CoreException e) {
					RDTSyncCorePlugin.log(e);
				}
			}
		} catch (CoreException e) {
			// fail
		}
		return null;
	}

	/**
	 * Get the synchronize location URI of the resource associated with the active sync configuration. Returns null if the project
	 * containing the resource is not a synchronized project.
	 * 
	 * @param resource
	 *            target resource - cannot be null
	 * @return URI or null if not a sync project
	 * @throws CoreException
	 */
	public static URI getActiveSyncLocationURI(IResource resource) throws CoreException {
		SyncConfig config = getActive(resource.getProject());
		if (config != null) {
			return getSyncLocationURI(config, resource);
		}
		return null;
	}

	/**
	 * Find a configuration by name
	 * 
	 * @param project
	 *            project containing configuration
	 * @param name
	 *            name of configuration
	 * @return configuration or null if no configuration with supplied name found
	 */
	public static SyncConfig getConfig(IProject project, String name) {
		try {
			loadConfigs(project);
			Map<String, SyncConfig> map = fSyncConfigMap.get(project);
			if (map != null) {
				return map.get(name);
			}
		} catch (CoreException e) {
			RDTSyncCorePlugin.log(e);
		}
		return null;
	}

	/**
	 * Get the sync configurations associated with the project
	 * 
	 * @param project
	 * @return sync configurations for the project
	 */
	public static SyncConfig[] getConfigs(IProject project) {
		try {
			loadConfigs(project);
			Map<String, SyncConfig> configs = fSyncConfigMap.get(project);
			if (configs != null) {
				return configs.values().toArray(new SyncConfig[0]);
			}
		} catch (CoreException e) {
			RDTSyncCorePlugin.log(e);
		}
		return new SyncConfig[0];
	}

	private static ListenerList<Object> getListenersFor(IProject project) {
		ListenerList<Object> listeners = new ListenerList<Object>();
		for (String nature : fSyncConfigListenerMap.keySet()) {
			try {
				if (project.hasNature(nature)) {
					for (Object listener : fSyncConfigListenerMap.get(nature).getListeners()) {
						listeners.add(listener);
					}
				}
			} catch (CoreException e) {
				// Ignore
			}
		}
		return listeners;
	}

	/**
	 * Get a local sync config, really a config that does no sync'ing, for when the user wants to just work locally.
	 * This method must agree with {@link #isLocal(SyncConfig)}.
	 * 
	 * @return a local config
	 * @throws CoreException
	 *             on problems retrieving local service elements
	 * @since 4.0
	 */
	public static SyncConfig getLocalConfig(String syncServiceId) throws CoreException {
		IRemoteServicesManager servicesManager = RDTSyncCorePlugin.getService(IRemoteServicesManager.class);
		IRemoteConnectionType localConnType = servicesManager.getLocalConnectionType();
		if (localConnType == null) {
			throw new CoreException(new Status(IStatus.ERROR, RDTSyncCorePlugin.PLUGIN_ID, Messages.SyncConfigManager_0));
		}

		IRemoteConnection localConnection = localConnType.getConnections().get(0);
		if (localConnection == null) {
			throw new CoreException(new Status(IStatus.ERROR, RDTSyncCorePlugin.PLUGIN_ID, Messages.SyncConfigManager_1));
		}

		return SyncConfigManager.newConfig(LOCAL_SYNC_CONFIG_NAME, syncServiceId, localConnection, PROJECT_LOCAL_PATH);
	}

	/**
	 * Get the name of the local sync config
	 *
	 * @return name for local sync config
	 * @since 5.0
	 */
	public static String getLocalConfigName() {
		return LOCAL_SYNC_CONFIG_NAME;
	}

	/**
	 * Get the synchronize location URI of the resource associated with the sync configuration. Returns null if the sync
	 * configuration has not been configured correctly.
	 * 
	 * @param config
	 *            sync configuration
	 * @param resource
	 *            target resource
	 * @return URI or null if not correctly configured
	 * @throws CoreException
	 */
	public static URI getSyncLocationURI(SyncConfig config, IResource resource) throws CoreException {
		if (config != null) {
			IPath path = new Path(config.getLocation()).append(resource.getProjectRelativePath());
			IRemoteConnection conn;
			try {
				conn = config.getRemoteConnection();
			} catch (MissingConnectionException e) {
				return null;
			}
			IRemoteFileService fileService = conn.getService(IRemoteFileService.class);
			if (fileService != null) {
				return fileService.toURI(path);
			}
		}
		return null;
	}

	/**
	 * Check if this config is active for the project.
	 * 
	 * @param project
	 * @param config
	 * @return true if this config is the active config for the project
	 */
	public static boolean isActive(IProject project, SyncConfig config) {
		SyncConfig active = fActiveSyncConfigMap.get(project);
		return (active != null && config != null && active.equals(config));
	}

	/**
	 * Return whether the config is local (no sync'ing is done)
	 * This definition must agree with how local configs are created in {@link #getLocalConfig(String)}.
	 * 
	 * @param config
	 * @return whether config is local
	 */
	public static boolean isLocal(SyncConfig config) {
		return LOCAL_SYNC_CONFIG_NAME.equals(config.getName());
	}

	/**
	 * Return whether the config is remote (sync'ing is done)
	 * 
	 * @param config
	 * @return whether config is remote
	 */
	public static boolean isRemote(SyncConfig config) {
		return !isLocal(config);
	}

	private static void loadConfigs(IProject project) throws CoreException {
		if (!fSyncConfigMap.containsKey(project)) {
			IScopeContext context = new ProjectScope(project);
			Preferences node = context.getNode(RDTSyncCorePlugin.PLUGIN_ID);
			String prefs = node.get(SYNC_CONFIG_KEY, null);
			if (prefs != null) {
				StringReader reader = new StringReader(prefs);
				XMLMemento rootMemento = XMLMemento.createReadRoot(reader);
				for (IMemento configMemento : rootMemento.getChildren(CONFIG_ELEMENT)) {
					String configName = configMemento.getString(CONFIG_NAME_ELEMENT);
					String location = configMemento.getString(LOCATION_ELEMENT);
					String connectionName = configMemento.getString(CONNECTION_NAME_ELEMENT);
					String remoteServicesId = configMemento.getString(REMOTE_SERVICES_ID_ELEMENT);
					String syncProviderId = configMemento.getString(SYNC_PROVIDER_ID_ELEMENT);
					Boolean syncOnPreBuild = configMemento.getBoolean(SYNC_ON_PREBUILD_ELEMENT);
					Boolean syncOnPostBuild = configMemento.getBoolean(SYNC_ON_POSTBUILD_ELEMENT);
					Boolean syncOnSave = configMemento.getBoolean(SYNC_ON_SAVE_ELEMENT);
					SyncConfig config = newConfig(configName, syncProviderId, remoteServicesId, connectionName, location);
					if (syncOnPreBuild != null) {
						config.setSyncOnPreBuild(syncOnPreBuild.booleanValue());
					}
					if (syncOnPostBuild != null) {
						config.setSyncOnPostBuild(syncOnPostBuild.booleanValue());
					}
					if (syncOnSave != null) {
						config.setSyncOnSave(syncOnSave.booleanValue());
					}
					IMemento configPropertiesMemento = configMemento.getChild(CONFIG_PROPERTIES_ELEMENT);
					if (configPropertiesMemento != null) {
						for (String key : configPropertiesMemento.getAttributeKeys()) {
							String value = configPropertiesMemento.getString(key);
							config.setProperty(key, value);
						}
					}
					doAddConfig(project, config);
				}
				String activeName = rootMemento.getString(ACTIVE_ELEMENT);
				if (activeName != null) {
					Map<String, SyncConfig> configMap = fSyncConfigMap.get(project);
					SyncConfig config = configMap.get(activeName);
					if (config != null) {
						fActiveSyncConfigMap.put(project, config);
					}
				}
			}
		}
	}

	/**
	 * @param name
	 * @param providerId
	 * @param conn
	 * @param location
	 * @return
	 * @since 4.0
	 */
	public static SyncConfig newConfig(String name, String providerId, IRemoteConnection conn, String location) {
		SyncConfig config = new SyncConfig(name);
		config.setSyncProviderId(providerId);
		config.setConnection(conn);
		config.setLocation(location);
		return config;
	}

	/**
	 * @param name
	 * @param providerId
	 * @param remoteServicesId
	 * @param connName
	 * @param location
	 * @return
	 */
	public static SyncConfig newConfig(String name, String providerId, String remoteServicesId, String connName, String location) {
		SyncConfig config = new SyncConfig(name);
		config.setSyncProviderId(providerId);
		config.setRemoteServicesId(remoteServicesId);
		config.setConnectionName(connName);
		config.setLocation(location);
		return config;
	}

	/**
	 * Remove the sync configuration from the project. Note that this methods allows an active configuration to be removed.
	 * 
	 * Clients should check the active status of the configuration and call {@link #setActive(IProject, SyncConfig)} if necessary.
	 * 
	 * Clients will not be allowed to remove all configurations from the project. There must always be at least one configuration
	 * for each project.
	 * 
	 * @param project
	 *            project
	 * @param config
	 *            configuration to remove
	 */
	public static void removeConfig(IProject project, SyncConfig config) {
		try {
			loadConfigs(project);
			boolean removed = doRemoveConfig(project, config);
			if (removed) {
				saveConfigs(project);
				fireSyncConfigRemoved(project, config);
			}
		} catch (CoreException e) {
			RDTSyncCorePlugin.log(e);
		}
	}

	/**
	 * Remove the listener for sync config events
	 * 
	 * @param natureId
	 * @param listener
	 */
	public static void removeSyncConfigListener(String natureId, ISyncConfigListener listener) {
		ListenerList<ISyncConfigListener> list = fSyncConfigListenerMap.get(natureId);
		if (list != null) {
			list.remove(listener);
		}
	}

	/**
	 * Save the current configurations for the project. This method should be called prior to workbench shutdown if any
	 * modifications have been made to the configurations
	 * 
	 * @param project
	 * @throws CoreException
	 */
	public static void saveConfigs(IProject project) throws CoreException {
		Map<String, SyncConfig> projConfigs = fSyncConfigMap.get(project);
		if (projConfigs != null) {
			XMLMemento rootMemento = XMLMemento.createWriteRoot(CONFIGS_ELEMENT);
			for (SyncConfig config : projConfigs.values()) {
				IMemento configMemento = rootMemento.createChild(CONFIG_ELEMENT);
				configMemento.putString(CONFIG_NAME_ELEMENT, config.getName());
				configMemento.putString(LOCATION_ELEMENT, config.getLocation());
				configMemento.putString(CONNECTION_NAME_ELEMENT, config.getConnectionName());
				configMemento.putString(REMOTE_SERVICES_ID_ELEMENT, config.getRemoteServicesId());
				configMemento.putString(SYNC_PROVIDER_ID_ELEMENT, config.getSyncProviderId());
				configMemento.putBoolean(SYNC_ON_PREBUILD_ELEMENT, config.isSyncOnPreBuild());
				configMemento.putBoolean(SYNC_ON_POSTBUILD_ELEMENT, config.isSyncOnPostBuild());
				configMemento.putBoolean(SYNC_ON_SAVE_ELEMENT, config.isSyncOnSave());
				IMemento configPropertiesMemento = configMemento.createChild(CONFIG_PROPERTIES_ELEMENT);
				for (String key : config.getKeys()) {
					configPropertiesMemento.putString(key, config.getProperty(key));
				}
			}
			SyncConfig active = fActiveSyncConfigMap.get(project);
			if (active != null) {
				rootMemento.putString(ACTIVE_ELEMENT, active.getName());
			}
			StringWriter writer = new StringWriter();
			try {
				rootMemento.save(writer);
			} catch (IOException e) {
				throw new CoreException(
						new Status(IStatus.ERROR, RDTSyncCorePlugin.PLUGIN_ID, Messages.SyncConfigManager_Unable_to_save, e));
			}
			IScopeContext context = new ProjectScope(project);
			Preferences node = context.getNode(RDTSyncCorePlugin.PLUGIN_ID);
			node.put(SYNC_CONFIG_KEY, writer.toString());
			try {
				node.flush();
			} catch (BackingStoreException e) {
				RDTSyncCorePlugin.log(e);
			}
		}
	}

	/**
	 * Set the active sync configuration for the project. Automatically deselects the current active configuration.
	 * 
	 * @param project
	 * @param config
	 */
	public static void setActive(IProject project, SyncConfig config) {
		try {
			loadConfigs(project);
			SyncConfig oldConfig = fActiveSyncConfigMap.get(project);
			fActiveSyncConfigMap.put(project, config);
			saveConfigs(project);
			fireSyncConfigSelected(project, config, oldConfig);
		} catch (CoreException e) {
			RDTSyncCorePlugin.log(e);
		}
	}

	/**
	 * Batch update configurations for the project
	 * 
	 * @param project
	 *            project to update
	 * @param addedConfigs
	 *            configs to be added
	 * @param removedConfigs
	 *            configs to be removed
	 */
	public static void updateConfigs(IProject project, SyncConfig[] addedConfigs, SyncConfig[] removedConfigs) {
		for (SyncConfig config : addedConfigs) {
			doAddConfig(project, config);
		}
		for (SyncConfig config : removedConfigs) {
			doRemoveConfig(project, config);
		}
		try {
			saveConfigs(project);
		} catch (CoreException e) {
			RDTSyncCorePlugin.log(e);
		}
	}

	private SyncConfigManager() {
	}
}

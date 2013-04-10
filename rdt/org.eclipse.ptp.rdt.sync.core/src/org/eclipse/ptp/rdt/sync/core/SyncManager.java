/*******************************************************************************
 * Copyright (c) 2011 Oak Ridge National Laboratory and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    John Eblen - initial implementation
 *******************************************************************************/
package org.eclipse.ptp.rdt.sync.core;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ptp.internal.rdt.sync.core.RDTSyncCorePlugin;
import org.eclipse.ptp.internal.rdt.sync.core.messages.Messages;
import org.eclipse.ptp.rdt.sync.core.handlers.IMissingConnectionHandler;
import org.eclipse.ptp.rdt.sync.core.handlers.ISyncExceptionHandler;
import org.eclipse.ptp.rdt.sync.core.policy.ISynchronizePolicy;
import org.eclipse.ptp.remote.core.IRemoteServices;
import org.osgi.service.prefs.Preferences;

public class SyncManager {
	private static class SynchronizeJob extends Job {
		private final IProject fProject;
		private final BuildScenario fBuildScenario;
		private final IResourceDelta fDelta;
		private final SyncRunner fSyncRunner;
		private final EnumSet<SyncFlag> fSyncFlags;
		private final ISyncExceptionHandler fSyncExceptionHandler;

		public SynchronizeJob(IProject project, BuildScenario buildScenario, IResourceDelta delta, SyncRunner runner,
				EnumSet<SyncFlag> syncFlags, ISyncExceptionHandler seHandler) {
			super(Messages.SyncManager_4);
			fProject = project;
			fBuildScenario = buildScenario;
			fDelta = delta;
			fSyncRunner = runner;
			fSyncFlags = syncFlags;
			fSyncExceptionHandler = seHandler;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		protected IStatus run(IProgressMonitor monitor) {
			RecursiveSubMonitor subMonitor = RecursiveSubMonitor.convert(monitor);
			try {
				fSyncRunner.synchronize(fProject, fBuildScenario, fDelta, getFileFilter(fProject), subMonitor, fSyncFlags);
			} catch (CoreException e) {
				if (fSyncExceptionHandler == null) {
					defaultSyncExceptionHandler.handle(fProject, e);
				} else {
					fSyncExceptionHandler.handle(fProject, e);
				}
			} finally {
				monitor.done();
				SyncManager.notifySyncListeners(fProject);
			}
			return Status.OK_STATUS;
		}
	}

	// ACTIVE: Sync with current active configuration
	// ALL: Sync with all configurations
	// NONE: Do not transfer files but still call sync and do bookkeeping
	// UNAVAILABLE: Do not call sync. (Used internally during project creation and deletion.)
	public static enum SyncMode {
		ACTIVE, ALL, NONE, UNAVAILABLE
	};

	private static final String projectScopeSyncNode = "org.eclipse.ptp.rdt.sync.core"; //$NON-NLS-1$
	private static final String instanceScopeSyncNode = "org.eclipse.ptp.rdt.sync.core"; //$NON-NLS-1$
	private static final String SYNC_MODE_KEY = "sync-mode"; //$NON-NLS-1$
	private static final String SYNC_AUTO_KEY = "sync-auto"; //$NON-NLS-1$
	private static final String SHOW_ERROR_KEY = "show-error"; //$NON-NLS-1$

	public static final String SYNCHRONIZE_POLICY_EXTENSION = "synchronizePolicy"; //$NON-NLS-1$
	public static final String ATTR_CLASS = "class"; //$NON-NLS-1$
	public static final String ATTR_NATURE = "nature"; //$NON-NLS-1$

	private static ISyncExceptionHandler defaultSyncExceptionHandler = new ISyncExceptionHandler() {
		@Override
		public void handle(IProject project, CoreException e) {
			RDTSyncCorePlugin.log(Messages.SyncManager_8 + project.getName(), e);
		}
	};

	private static IMissingConnectionHandler defaultMissingConnectionHandler = new IMissingConnectionHandler() {
		@Override
		public void handle(IRemoteServices remoteServices, String connectionName) {
			RDTSyncCorePlugin.log(Messages.SyncManager_10 + connectionName);
		}
	};

	private static final Map<IProject, Set<ISyncListener>> fProjectToSyncListenersMap = Collections
			.synchronizedMap(new HashMap<IProject, Set<ISyncListener>>());

	private static final Map<IProject, ISynchronizePolicy> fPolicyMap = Collections
			.synchronizedMap(new HashMap<IProject, ISynchronizePolicy>());

	// Sync unavailable by default. Wizards should explicitly set the sync mode once the project is ready.
	private static final SyncMode DEFAULT_SYNC_MODE = SyncMode.UNAVAILABLE;
	private static final boolean DEFAULT_SYNC_AUTO_SETTING = true;
	private static final boolean DEFAULT_SHOW_ERROR_SETTING = true;

	/**
	 * Add a listener for sync events on a certain project
	 * 
	 * @param project
	 * @param listener
	 */
	public static void addPostSyncListener(IProject project, ISyncListener listener) {
		Set<ISyncListener> listenerSet = fProjectToSyncListenersMap.get(project);
		if (listenerSet == null) {
			listenerSet = new HashSet<ISyncListener>();
			fProjectToSyncListenersMap.put(project, listenerSet);
		}
		listenerSet.add(listener);
	};

	/**
	 * Return a copy of the default file filter
	 * If there are any problems retrieving the filter, the built-in default filter is returned.
	 * 
	 * @return the file filter. This is never null.
	 */
	public static SyncFileFilter getDefaultFileFilter() {
		IScopeContext context = InstanceScope.INSTANCE;
		Preferences node = context.getNode(instanceScopeSyncNode);
		if (node == null) {
			RDTSyncCorePlugin.log(Messages.SyncManager_6);
			return SyncFileFilter.createBuiltInDefaultFilter();
		}

		SyncFileFilter filter = SyncFileFilter.loadFilter(node);
		if (filter == null) {
			return SyncFileFilter.createBuiltInDefaultFilter();
		} else {
			return filter;
		}
	}

	/**
	 * Get the current default missing connection handler
	 * 
	 * @return default missing connection handler
	 */
	public static IMissingConnectionHandler getDefaultMissingConnectionHandler() {
		return defaultMissingConnectionHandler;
	}

	/**
	 * Get the current default sync exception handler
	 * 
	 * @return default sync exception handler
	 */
	public static ISyncExceptionHandler getDefaultSyncExceptionHandler() {
		return defaultSyncExceptionHandler;
	}

	/**
	 * Return a copy of the project's file filter.
	 * If there are any problems retrieving the filter, the workspace default filter is returned.
	 * Since only a copy is returned, users must execute "saveFileFilter(IProject, SyncFileFilter)" after making changes to have
	 * those changes actually applied.
	 * 
	 * @param project
	 *            cannot be null
	 * @return the file filter. This is never null.
	 */
	public static SyncFileFilter getFileFilter(IProject project) {
		if (project == null) {
			throw new NullPointerException();
		}
		IScopeContext context = new ProjectScope(project);
		Preferences node = context.getNode(projectScopeSyncNode);
		if (node == null) {
			RDTSyncCorePlugin.log(Messages.SyncManager_3);
			return SyncManager.getDefaultFileFilter();
		}

		SyncFileFilter filter = SyncFileFilter.loadFilter(node);
		if (filter == null) {
			return SyncManager.getDefaultFileFilter();
		} else {
			return filter;
		}
	}

	/**
	 * Should error messages be displayed for the given project?
	 * 
	 * @param project
	 * @return whether error messages should be displayed.
	 */
	public static boolean getShowErrors(IProject project) {
		if (project == null) {
			throw new NullPointerException();
		}
		IScopeContext context = new ProjectScope(project);
		Preferences node = context.getNode(projectScopeSyncNode);
		if (node == null) {
			RDTSyncCorePlugin.log(Messages.SyncManager_3);
			return DEFAULT_SHOW_ERROR_SETTING;
		}
		return node.getBoolean(SHOW_ERROR_KEY, DEFAULT_SHOW_ERROR_SETTING);
	}

	/**
	 * Should sync'ing be done automatically?
	 * 
	 * @return if sync'ing should be done automatically
	 */
	public static boolean getSyncAuto() {
		IScopeContext context = InstanceScope.INSTANCE;
		Preferences node = context.getNode(instanceScopeSyncNode);
		if (node == null) {
			RDTSyncCorePlugin.log(Messages.SyncManager_6);
			return DEFAULT_SYNC_AUTO_SETTING;
		}
		return node.getBoolean(SYNC_AUTO_KEY, DEFAULT_SYNC_AUTO_SETTING);
	}

	public static BuildScenario[] getSynchronizePolicy(IProject project, SyncMode mode) {
		ISynchronizePolicy policy = fPolicyMap.get(project);
		if (policy == null) {
			String[] natures = null;
			try {
				natures = project.getDescription().getNatureIds();
			} catch (CoreException e) {
				// Ignore
			}
			if (natures != null) {
				for (String nature : natures) {
					try {
						if (project.isNatureEnabled(nature)) {
							policy = getSynchronizePolicy(nature);
							if (policy != null) {
								fPolicyMap.put(project, policy);
								break;
							}
						}
					} catch (CoreException e) {
						// Ignore
					}
				}
			}
			if (policy == null) {
				return null;
			}
		}
		return policy.getSyncronizePolicy(project, mode);
	}

	private static ISynchronizePolicy getSynchronizePolicy(String nature) {
		IExtensionPoint point = Platform.getExtensionRegistry().getExtensionPoint(RDTSyncCorePlugin.PLUGIN_ID,
				SYNCHRONIZE_POLICY_EXTENSION);
		if (point != null) {
			for (IExtension extension : point.getExtensions()) {
				for (IConfigurationElement configElement : extension.getConfigurationElements()) {
					String natureId = configElement.getAttribute(ATTR_NATURE);
					if (natureId != null && natureId.equals(nature)) {
						try {
							return (ISynchronizePolicy) configElement.createExecutableExtension(ATTR_CLASS);
						} catch (CoreException e) {
							RDTSyncCorePlugin.log(e);
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Get sync mode for a project
	 * 
	 * @param project
	 *            cannot be null
	 * @return sync mode. This is never null.
	 */
	public static SyncMode getSyncMode(IProject project) {
		if (project == null) {
			throw new NullPointerException();
		}
		IScopeContext context = new ProjectScope(project);
		Preferences node = context.getNode(projectScopeSyncNode);
		if (node == null) {
			RDTSyncCorePlugin.log(Messages.SyncManager_3);
			return DEFAULT_SYNC_MODE;
		}

		return SyncMode.valueOf(node.get(SYNC_MODE_KEY, DEFAULT_SYNC_MODE.name()));
	}

	private static void notifySyncListeners(IProject project) {
		Set<ISyncListener> listenerSet = fProjectToSyncListenersMap.get(project);
		if (listenerSet == null) {
			return;
		}

		for (ISyncListener listener : listenerSet) {
			listener.handleSyncEvent(new SyncEvent());
		}
	}

	/**
	 * Remove a listener for sync events on a certain project
	 * 
	 * @param project
	 * @param listener
	 */
	public static void removePostSyncListener(IProject project, ISyncListener listener) {
		Set<ISyncListener> listenerSet = fProjectToSyncListenersMap.get(project);
		if (listenerSet != null) {
			listenerSet.remove(listener);
		}
	}

	/**
	 * Save a new default file filter.
	 * Use this in conjunction with "getDefaultFileFilter()" to modify the default filter.
	 * 
	 * @param filter
	 *            cannot be null
	 */
	public static void saveDefaultFileFilter(SyncFileFilter filter) {
		if (filter == null) {
			throw new NullPointerException();
		}

		IScopeContext context = InstanceScope.INSTANCE;
		Preferences node = context.getNode(instanceScopeSyncNode);
		if (node == null) {
			RDTSyncCorePlugin.log(Messages.SyncManager_6);
			return;
		}

		filter.saveFilter(node);

		BuildConfigurationManager.flushNode(node);
	}

	/**
	 * Save a new file filter for a project.
	 * Use this in conjunction with "getFileFilter(IProject)" to modify the current file filtering for a project.
	 * 
	 * @param project
	 *            cannot be null
	 * @param filter
	 *            cannot be null
	 */
	public static void saveFileFilter(IProject project, SyncFileFilter filter) {
		if (project == null || filter == null) {
			throw new NullPointerException();
		}

		IScopeContext context = new ProjectScope(project);
		Preferences node = context.getNode(projectScopeSyncNode);
		if (node == null) {
			RDTSyncCorePlugin.log(Messages.SyncManager_3);
			return;
		}

		filter.saveFilter(node);

		BuildConfigurationManager.flushNode(node);
	}

	// Note that the monitor is ignored for non-blocking jobs since SynchronizeJob creates its own monitor
	private static Job[] scheduleSyncJobs(IResourceDelta delta, IProject project, EnumSet<SyncFlag> syncFlags, SyncMode mode,
			boolean isBlocking, boolean useExceptionHandler, ISyncExceptionHandler seHandler, IProgressMonitor monitor)
			throws CoreException {
		int jobNum = 0;
		BuildScenario[] buildScenarios = getSynchronizePolicy(project, mode);
		if (buildScenarios != null) {
			Job[] syncJobs = new Job[buildScenarios.length];
			for (BuildScenario buildScenario : buildScenarios) {
				SynchronizeJob job = null;
				SyncRunner syncRunner = new SyncRunner(BuildConfigurationManager.getInstance().getProjectSyncProvider(project));
				if (syncRunner != null) {
					if (isBlocking) {
						try {
							syncRunner.synchronize(project, buildScenario, delta, getFileFilter(project), monitor, syncFlags);
						} catch (CoreException e) {
							if (!useExceptionHandler) {
								throw e;
							} else if (seHandler == null) {
								defaultSyncExceptionHandler.handle(project, e);
							} else {
								seHandler.handle(project, e);
							}
						} finally {
							SyncManager.notifySyncListeners(project);
						}
					} else {
						job = new SynchronizeJob(project, buildScenario, delta, syncRunner, syncFlags, seHandler);
						job.schedule();
					}
				}

				// Each build configuration is matched with a job, which may be null if a job could not be created.
				syncJobs[jobNum] = job;
				jobNum++;
			}
			return syncJobs;
		}

		return null;
	}

	/**
	 * Set the default sync exception handler
	 * 
	 * @param handler
	 */
	public static void setDefaultMissingConnectionHandler(IMissingConnectionHandler handler) {
		defaultMissingConnectionHandler = handler;
	}

	/**
	 * Set the default sync exception handler
	 * 
	 * @param handler
	 */
	public static void setDefaultSyncExceptionHandler(ISyncExceptionHandler handler) {
		defaultSyncExceptionHandler = handler;
	}

	/**
	 * Set whether error messages should be displayed
	 * 
	 * @param project
	 * @param shouldBeDisplayed
	 */
	public static void setShowErrors(IProject project, boolean shouldBeDisplayed) {
		if (project == null) {
			throw new NullPointerException();
		}

		IScopeContext context = new ProjectScope(project);
		Preferences node = context.getNode(projectScopeSyncNode);
		if (node == null) {
			RDTSyncCorePlugin.log(Messages.SyncManager_3);
			return;
		}

		if (shouldBeDisplayed == DEFAULT_SHOW_ERROR_SETTING) {
			node.remove(SHOW_ERROR_KEY);
		} else {
			node.putBoolean(SHOW_ERROR_KEY, shouldBeDisplayed);
		}

		BuildConfigurationManager.flushNode(node);
	}

	/**
	 * Turn automatic sync'ing on or off
	 * 
	 * @param isSyncAutomatic
	 */
	public static void setSyncAuto(boolean isSyncAutomatic) {
		IScopeContext context = InstanceScope.INSTANCE;
		Preferences node = context.getNode(instanceScopeSyncNode);
		if (node == null) {
			RDTSyncCorePlugin.log(Messages.SyncManager_6);
			return;
		}

		if (isSyncAutomatic == DEFAULT_SYNC_AUTO_SETTING) {
			node.remove(SYNC_AUTO_KEY);
		} else {
			node.putBoolean(SYNC_AUTO_KEY, isSyncAutomatic);
		}

		BuildConfigurationManager.flushNode(node);
	}

	/**
	 * Set sync mode for a project
	 * 
	 * @param project
	 * @param mode
	 */
	public static void setSyncMode(IProject project, SyncMode mode) {
		if (project == null || mode == null) {
			throw new NullPointerException();
		}

		IScopeContext context = new ProjectScope(project);
		Preferences node = context.getNode(projectScopeSyncNode);
		if (node == null) {
			RDTSyncCorePlugin.log(Messages.SyncManager_3);
			return;
		}

		if (mode == DEFAULT_SYNC_MODE) {
			node.remove(SYNC_MODE_KEY);
		} else {
			node.put(SYNC_MODE_KEY, mode.name());
		}

		BuildConfigurationManager.flushNode(node);
	}

	private static Job sync(IResourceDelta delta, IProject project, EnumSet<SyncFlag> syncFlags, boolean isBlocking,
			boolean useExceptionHandler, ISyncExceptionHandler seHandler, IProgressMonitor monitor) throws CoreException {
		if (getSyncMode(project) == SyncMode.UNAVAILABLE) {
			return null;
		}

		Job[] syncJobs = scheduleSyncJobs(delta, project, syncFlags, SyncMode.ACTIVE, isBlocking, useExceptionHandler, seHandler,
				monitor);
		return syncJobs[0];
	}

	/**
	 * Invoke sync for active (default) configuration on a project
	 * 
	 * @param delta
	 *            project delta
	 * @param project
	 *            project to sync
	 * @param syncFlags
	 *            sync flags
	 * @param seHandler
	 *            logic to handle exceptions
	 * @return the scheduled sync job
	 * @throws CoreException
	 */
	public static Job sync(IResourceDelta delta, IProject project, EnumSet<SyncFlag> syncFlags, ISyncExceptionHandler seHandler)
			throws CoreException {
		return sync(delta, project, syncFlags, false, true, seHandler, null);
	}

	/**
	 * Invoke sync for all configurations on a project.
	 * Note that there is no syncAllBlocking, because it was not needed but would be easy to add.
	 * 
	 * @param delta
	 *            project delta
	 * @param project
	 *            project to sync
	 * @param syncFlags
	 *            sync flags
	 * @param seHandler
	 *            logic to handle exceptions
	 * @return array of sync jobs scheduled
	 * @throws CoreException
	 *             on problems sync'ing
	 */
	public static Job[] syncAll(IResourceDelta delta, IProject project, EnumSet<SyncFlag> syncFlags, ISyncExceptionHandler seHandler)
			throws CoreException {
		SyncMode mode = getSyncMode(project);
		if (mode == SyncMode.UNAVAILABLE) {
			return new Job[0];
		}

		return scheduleSyncJobs(delta, project, syncFlags, mode, false, true, seHandler, null);
	}

	/**
	 * Invoke sync and block until sync finishes. This does not spawn another thread and no locking of resources is done.
	 * Throws sync exceptions for client to handle.
	 * 
	 * @param delta
	 *            project delta
	 * @param project
	 *            project to sync
	 * @param syncFlags
	 *            sync flags
	 * @param monitor
	 *            progress monitor
	 * @return the scheduled sync job
	 * @throws CoreException
	 *             on problems sync'ing
	 */
	public static Job syncBlocking(IResourceDelta delta, IProject project, EnumSet<SyncFlag> syncFlags, IProgressMonitor monitor)
			throws CoreException {
		return sync(delta, project, syncFlags, true, false, null, monitor);
	}

	/**
	 * Invoke sync and block until sync finishes. This does not spawn another thread and no locking of resources is done.
	 * Sync exceptions are handled by the passed exception handler or by the default handler if null.
	 * 
	 * @param delta
	 *            project delta
	 * @param project
	 *            project to sync
	 * @param syncFlags
	 *            sync flags
	 * @param monitor
	 *            progress monitor
	 * @param seHandler
	 *            sync exception handler
	 * @return the scheduled sync job
	 * @throws CoreException
	 *             on problems sync'ing
	 */
	public static Job syncBlocking(IResourceDelta delta, IProject project, EnumSet<SyncFlag> syncFlags, IProgressMonitor monitor,
			ISyncExceptionHandler seHandler) throws CoreException {
		return sync(delta, project, syncFlags, true, true, seHandler, monitor);
	}

	// Static class - do not allow creating of instances
	private SyncManager() {
	}
}

/*******************************************************************************
 * Copyright (c) 2011, 2019 University of Illinois All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html 
 * 	
 * Contributors: 
 * 	Albert L. Rossi - design and implementation
 ******************************************************************************/
package org.eclipse.ptp.rm.lml.monitor.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ptp.internal.rm.jaxb.core.JAXBExtensionUtils;
import org.eclipse.ptp.internal.rm.jaxb.core.JAXBInitializationUtils;
import org.eclipse.ptp.internal.rm.lml.monitor.core.LMLMonitorCorePlugin;
import org.eclipse.ptp.internal.rm.lml.monitor.core.MonitorControl;
import org.eclipse.ptp.internal.rm.lml.monitor.core.messages.Messages;
import org.eclipse.ptp.rm.jaxb.control.core.ILaunchController;
import org.eclipse.ptp.rm.jaxb.control.core.LaunchControllerManager;
import org.eclipse.ptp.rm.jaxb.core.data.MonitorType;
import org.eclipse.ptp.rm.jaxb.core.data.ResourceManagerData;
import org.eclipse.ptp.rm.jaxb.core.data.lml.LayoutRoot;
import org.eclipse.ptp.rm.lml.core.LMLManager;
import org.eclipse.ptp.rm.lml.monitor.core.listeners.IMonitorChangedListener;
import org.eclipse.ptp.rm.lml.monitor.core.listeners.IMonitorRefreshListener;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.progress.UIJob;

public class MonitorControlManager {
	private static final MonitorControlManager fInstance = new MonitorControlManager();
	private static final ListenerList fMonitorChangedListeners = new ListenerList();
	private static final ListenerList fSelectionChangedListeners = new ListenerList();
	private static final ListenerList fMonitorRefreshListeners = new ListenerList();
	private static final String MONITORS_SAVED_STATE = "monitors.xml";//$NON-NLS-1$
	private static final String MONITORS_ATTR = "monitors";//$NON-NLS-1$
	private static final String MONITOR_ID_ATTR = "monitor";//$NON-NLS-1$
	private static final String RMXSDJAXBPackage = "org.eclipse.ptp.rm.jaxb.core.data"; //$NON-NLS-1$
	private static final String LMLNamespace = "http://eclipse.org/ptp/lml"; //$NON-NLS-1$
	private static final String LMLPrefix = "lml"; //$NON-NLS-1$
	private static final String LocalPartOfLMLLayout = "layout"; //$NON-NLS-1$
	private static Map<String, String> fSystemTypesByConfigName = new TreeMap<String, String>();
	/**
	 * Stores default layout for each config name, if available
	 */
	private static Map<String, String> fSystemLayoutByConfigName = new TreeMap<String, String>();

	public static String generateMonitorId(String remoteServicesId, String connectionName, String monitorType) {
		return LaunchControllerManager.generateControlId(remoteServicesId, connectionName, monitorType);
	}

	public static MonitorControlManager getInstance() {
		return fInstance;
	}

	public static String getSystemType(String configName) {
		loadSystemTypes();
		return fSystemTypesByConfigName.get(configName);
	}

	/**
	 * Retrieve the default layout string for the given RMS configuration name.
	 * If there is no default layout specified, return null.
	 * This layout can be sent as request to LML_DA. It represents a stand-alone
	 * LML layout instance.
	 * 
	 * @param configName
	 *            name of the RMS configuration such as
	 * @return the xml layout for this configuration or null, if there is no layout defined
	 */
	public static String getSystemLayout(String configName) {
		loadSystemLayouts();
		return fSystemLayoutByConfigName.get(configName);
	}

	public static String[] getConfigurationNames() {
		loadSystemTypes();
		Set<String> set = new TreeSet<String>();
		set.addAll(fSystemTypesByConfigName.keySet());
		return set.toArray(new String[0]);
	}

	/**
	 * Gets all extensions to the monitors extension point and loads their names
	 * and types.
	 */
	private static void loadSystemTypes() {
		String[] configNames = JAXBExtensionUtils.getConfiguationNames();
		for (String name : configNames) {
			URL url = JAXBExtensionUtils.getConfigurationURL(name);
			try {
				ResourceManagerData data = JAXBInitializationUtils.initializeRMData(url);
				MonitorType monitorType = data.getMonitorData();
				if (monitorType != null) {
					String type = monitorType.getSchedulerType();
					if (type != null) {
						fSystemTypesByConfigName.put(name, type);
					}
				}
			} catch (Exception e) {
				// Ignore
			}
		}
	}

	/**
	 * Gets all extensions to the monitors extension point and loads their names
	 * and default layouts. Stores them in the map fSystemLayoutByConfigName.
	 */
	private static void loadSystemLayouts() {
		String[] configNames = JAXBExtensionUtils.getConfiguationNames();
		// Use JAXB marshaller to convert JAXB object back to XML data, which is sent to LML_DA
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(RMXSDJAXBPackage);
		} catch (JAXBException e1) {
			e1.printStackTrace();
			return;
		}

		Marshaller marshaller;
		try {
			marshaller = jaxbContext.createMarshaller();
		} catch (JAXBException e1) {
			e1.printStackTrace();
			return;
		}

		for (String name : configNames) {
			URL url = JAXBExtensionUtils.getConfigurationURL(name);
			try {
				ResourceManagerData data = JAXBInitializationUtils.initializeRMData(url);
				MonitorType monitorType = data.getMonitorData();
				if (monitorType != null) {
					// Check if there is a layout existant
					if (monitorType.getLayout() != null) {
						final StringWriter sw = new StringWriter();
						QName qname = new QName(LMLNamespace, LocalPartOfLMLLayout, LMLPrefix);
						JAXBElement<LayoutRoot> jaxbEl = new JAXBElement<LayoutRoot>(qname, LayoutRoot.class,
								monitorType.getLayout());
						marshaller.marshal(jaxbEl, sw);
						fSystemLayoutByConfigName.put(name, sw.toString());
					}
				}
			} catch (Exception e) {
				// Ignore
			}
		}
	}

	private final Map<String, IMonitorControl> fMonitorControls = Collections
			.synchronizedMap(new HashMap<String, IMonitorControl>());
	private final Set<IMonitorControl> fMonitorControlsToStart = new HashSet<IMonitorControl>();

	private MonitorControlManager() {
	}

	public void addMonitorChangedListener(IMonitorChangedListener listener) {
		fMonitorChangedListeners.add(listener);
	}

	private void addMonitorControl(IMonitorControl monitor) {
		fMonitorControls.put(monitor.getId(), monitor);
		fireMonitorAdded(new IMonitorControl[] { monitor });
	}

	public void addMonitorRefreshListener(IMonitorChangedListener listener) {
		fMonitorRefreshListeners.add(listener);
	}

	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		fSelectionChangedListeners.add(listener);
	}

	public IMonitorControl createMonitorControl(ILaunchController controller) {
		return createMonitorControl(controller.getRemoteServicesId(), controller.getConnectionName(), controller.getConfiguration()
				.getName());
	}

	public IMonitorControl createMonitorControl(String remoteServicesId, String connectionName, String configName) {
		String monitorId = LaunchControllerManager.generateControlId(remoteServicesId, connectionName, configName);
		IMonitorControl monitor = new MonitorControl(monitorId);
		monitor.setRemoteServicesId(remoteServicesId);
		monitor.setConnectionName(connectionName);
		monitor.setConfigurationName(configName);
		monitor.save();
		addMonitorControl(monitor);
		saveMonitors();
		return monitor;
	}

	public void fireMonitorAdded(final IMonitorControl[] monitors) {
		UIJob job = new UIJob(Messages.MonitorControlManager_monitorAddedJobName) {

			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				for (Object listener : fMonitorChangedListeners.getListeners()) {
					((IMonitorChangedListener) listener).monitorAdded(monitors);
				}
				return Status.OK_STATUS;
			}

		};
		job.schedule();
	}

	public void fireMonitorRefresh(final IMonitorControl[] monitors) {
		UIJob job = new UIJob(Messages.MonitorControlManager_monitoRefreshJobName) {

			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				for (Object listener : fMonitorRefreshListeners.getListeners()) {
					((IMonitorRefreshListener) listener).refresh(monitors);
				}
				return Status.OK_STATUS;
			}

		};
		job.schedule();
	}

	public void fireMonitorRemoved(final IMonitorControl[] monitors) {
		UIJob job = new UIJob(Messages.MonitorControlManager_monitorRemovedJobName) {

			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				for (Object listener : fMonitorChangedListeners.getListeners()) {
					((IMonitorChangedListener) listener).monitorRemoved(monitors);
				}
				return Status.OK_STATUS;
			}

		};
		job.schedule();
	}

	public void fireMonitorUpdated(final IMonitorControl[] monitors) {
		UIJob job = new UIJob(Messages.MonitorControlManager_monitorUpdatedJobName) {

			@Override
			public IStatus runInUIThread(IProgressMonitor progress) {
				for (Object listener : fMonitorChangedListeners.getListeners()) {
					((IMonitorChangedListener) listener).monitorUpdated(monitors);
				}
				return Status.OK_STATUS;
			}

		};
		job.schedule();
	}

	public void fireSelectionChanged(final SelectionChangedEvent event) {
		UIJob job = new UIJob(Messages.MonitorControlManager_monitorSelectionChangedJobName) {

			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				for (Object listener : fSelectionChangedListeners.getListeners()) {
					((ISelectionChangedListener) listener).selectionChanged(event);
				}
				String monitorId = null;
				if (event.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection sel = (IStructuredSelection) event.getSelection();
					if (!sel.isEmpty()) {
						monitorId = ((IMonitorControl) sel.getFirstElement()).getId();
					}
				}
				LMLManager.getInstance().selectLgui(monitorId);
				return Status.OK_STATUS;
			}

		};
		job.schedule();
	}

	public IMonitorControl getMonitorControl(String monitorId) {
		return fMonitorControls.get(monitorId);
	}

	public Collection<IMonitorControl> getMonitorControls() {
		return Collections.unmodifiableCollection(fMonitorControls.values());
	}

	private File getSaveLocation() {
		return LMLMonitorCorePlugin.getDefault().getStateLocation().append(MONITORS_SAVED_STATE).toFile();
	}

	/**
	 * Load a monitor from persistent storage. Logs an error if it can't be
	 * loaded.
	 * 
	 * @param memento
	 */
	private void loadMonitor(IMemento memento) {
		try {
			IMonitorControl monitor = new MonitorControl(memento.getID());
			if (monitor.load()) {
				fMonitorControlsToStart.add(monitor);
			}
			addMonitorControl(monitor);
		} catch (CoreException e) {
			LMLMonitorCorePlugin.log(e.getLocalizedMessage());
		}
	}

	/**
	 * Load all monitors from persistent storage.
	 */
	private void loadMonitors() {
		try {
			FileReader reader = new FileReader(getSaveLocation());
			IMemento memento = XMLMemento.createReadRoot(reader);
			IMemento[] monitorsMemento = memento.getChildren(MONITOR_ID_ATTR);
			for (IMemento monitorMemento : monitorsMemento) {
				loadMonitor(monitorMemento);
			}
		} catch (FileNotFoundException e) {
			Status status = new Status(IStatus.INFO, LMLMonitorCorePlugin.getUniqueIdentifier(), e.getLocalizedMessage());
			LMLMonitorCorePlugin.log(status);
		} catch (WorkbenchException e) {
			LMLMonitorCorePlugin.log(e.getLocalizedMessage());
		}
	}

	public void removeMonitorChangedListener(IMonitorChangedListener listener) {
		fMonitorChangedListeners.remove(listener);
	}

	public void removeMonitorControls(IMonitorControl[] monitors) {
		for (IMonitorControl monitor : monitors) {
			fMonitorControls.remove(monitor.getId());
			monitor.dispose();
		}
		saveMonitors();
		fireMonitorRemoved(monitors);
	}

	public void removeMonitorRefreshListener(IMonitorChangedListener listener) {
		fMonitorRefreshListeners.remove(listener);
	}

	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		fSelectionChangedListeners.remove(listener);
	}

	/**
	 * Save monitors to persistent storage. Just saves the monitor metadata. The
	 * actual monitor information will be saved when the monitor is created.
	 */
	private void saveMonitors() {
		final XMLMemento memento = XMLMemento.createWriteRoot(MONITORS_ATTR);
		for (IMonitorControl monitor : fMonitorControls.values()) {
			memento.createChild(MONITOR_ID_ATTR, monitor.getId());
		}

		try {
			FileWriter writer = new FileWriter(getSaveLocation());
			memento.save(writer);
		} catch (final IOException e) {
			LMLMonitorCorePlugin.log(e.getLocalizedMessage());
		}
	}

	public void start() {
		fMonitorControls.clear();
		fMonitorControlsToStart.clear();
		loadMonitors();
		startMonitors();
	}

	private void startMonitors() {
		// Not implemented
	}

	public void stop() throws CoreException {
		stopMonitors();
	}

	private void stopMonitors() {
		for (IMonitorControl monitor : fMonitorControls.values()) {
			try {
				monitor.stop();
			} catch (CoreException e) {
				LMLMonitorCorePlugin.log(e.getLocalizedMessage());
			}
		}
	}
}

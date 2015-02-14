/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - Initial API and implementation
 *******************************************************************************/
package org.eclipse.ptp.launch;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.ptp.core.util.LaunchUtils;
import org.eclipse.ptp.launch.internal.messages.Messages;
import org.eclipse.ptp.rm.jaxb.control.core.ILaunchController;
import org.eclipse.ptp.rm.jaxb.control.core.LaunchControllerManager;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.core.IRemoteConnectionType;
import org.eclipse.remote.core.IRemoteFileService;
import org.eclipse.remote.core.IRemoteServicesManager;
import org.eclipse.remote.core.launch.IRemoteLaunchConfigService;

/**
 * Utility methods for managing launch configuration attributes.
 * 
 * @since 7.0
 * 
 */
public class RMLaunchUtils {

	/**
	 * Get the launch controller for this configuration
	 * 
	 * @param configuration
	 * @return launch controller or null if none is available
	 * @throws CoreException
	 */
	public static ILaunchController getLaunchController(ILaunchConfiguration configuration) throws CoreException {
		String type = LaunchUtils.getTargetConfigurationName(configuration);
		if (type != null) {
			IRemoteLaunchConfigService launchConfigService = PTPLaunchPlugin.getService(IRemoteLaunchConfigService.class);
			IRemoteConnection conn = launchConfigService.getActiveConnection(configuration);
			if (conn != null) {
				String connName = conn.getName();
				String remId = conn.getConnectionType().getId();
				if (connName != null && remId != null) {
					return LaunchControllerManager.getInstance().getLaunchController(remId, connName, type);
				}
			}
		}
		return null;
	}

	/**
	 * Get the remote connection associated with the launch configuration
	 * 
	 * @param configuration
	 *            launch configuration
	 * @return remote connection or null if it is invalid or not specified
	 */
	public static IRemoteConnection getRemoteConnection(ILaunchConfiguration configuration) {
		IRemoteLaunchConfigService launchConfigService = PTPLaunchPlugin.getService(IRemoteLaunchConfigService.class);
		return launchConfigService.getActiveConnection(configuration);
	}

	/**
	 * Set the remote connection associated with the launch configuration
	 * 
	 * @param configuration
	 *            launch configuration
	 * @param connection
	 *            remote connection
	 */
	public static void setRemoteConnection(ILaunchConfiguration configuration, IRemoteConnection connection) {
		IRemoteLaunchConfigService launchConfigService = PTPLaunchPlugin.getService(IRemoteLaunchConfigService.class);
		launchConfigService.setActiveConnection(configuration, connection);
	}

	/**
	 * Get the local file manager
	 * 
	 * @param configuration
	 * @return local file manager (always succeeds)
	 */
	public static IRemoteFileService getLocalFileService(ILaunchConfiguration configuration) {
		IRemoteServicesManager servicesManager = PTPLaunchPlugin.getService(IRemoteServicesManager.class);
		IRemoteConnectionType connType = servicesManager.getLocalConnectionType();
		return connType.getConnections().get(0).getService(IRemoteFileService.class);
	}

	/**
	 * Get the remote file service for the connection associated with this launch configuration
	 * 
	 * @param configuration
	 * @return remote file service or null if none is available
	 */
	public static IRemoteFileService getRemoteFileService(ILaunchConfiguration configuration) {
		IRemoteConnection conn = getRemoteConnection(configuration);
		if (conn != null) {
			return conn.getService(IRemoteFileService.class);
		}
		return null;
	}

	/**
	 * Get the remote file service for the connection associated with this launch configuration. Opens the connection if it is not
	 * already open.
	 * 
	 * @param configuration
	 * @param monitor
	 * @return remote file service or null if none is available
	 * @throws CoreException
	 */
	public static IRemoteFileService getRemoteFileService(ILaunchConfiguration configuration, IProgressMonitor monitor)
			throws CoreException {
		SubMonitor progress = SubMonitor.convert(monitor, 10);
		IRemoteConnection conn = getRemoteConnection(configuration);
		if (conn != null) {
			if (!conn.isOpen()) {
				conn.open(progress.newChild(10));
				if (!progress.isCanceled() && !conn.isOpen()) {
					throw new CoreException(new Status(IStatus.ERROR, PTPLaunchPlugin.getUniqueIdentifier(),
							Messages.AbstractParallelLaunchConfigurationDelegate_Connection_is_not_open));
				}
			}
			return conn.getService(IRemoteFileService.class);
		}
		return null;
	}

	/**
	 * Constructor
	 */
	private RMLaunchUtils() {
	}
}

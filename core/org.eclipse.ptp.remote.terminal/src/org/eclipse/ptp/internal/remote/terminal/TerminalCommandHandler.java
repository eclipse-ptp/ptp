/*******************************************************************************
 * Copyright (c) 2012 IBM and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.ptp.internal.remote.terminal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.core.IRemoteConnectionHostService;
import org.eclipse.remote.core.exception.RemoteConnectionException;
import org.eclipse.tm.internal.terminal.provisional.api.ITerminalConnector;
import org.eclipse.tm.internal.terminal.provisional.api.TerminalConnectorExtension;
import org.eclipse.tm.terminal.view.core.TerminalServiceFactory;
import org.eclipse.tm.terminal.view.core.interfaces.ITerminalService;
import org.eclipse.tm.terminal.view.core.interfaces.constants.ITerminalsConnectorConstants;
import org.eclipse.ui.handlers.HandlerUtil;

@SuppressWarnings("restriction")
public class TerminalCommandHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection sel = HandlerUtil.getActiveMenuSelection(event);
		IStructuredSelection selection = (IStructuredSelection) sel;
		Object firstElement = selection.getFirstElement();
		if (firstElement instanceof IProject) {
			IProject prj = ((IProject) firstElement).getProject();
			connector(prj);
		}
		return null;
	}

	private static Map<String, ITerminalConnector> cons = new HashMap<String, ITerminalConnector>();

	/**
	 * Problems occur if we try to open two connections to the same machine. Keep track
	 * of what's alive and don't open it twice.
	 * 
	 * @param irc
	 * @return
	 * @throws RemoteConnectionException
	 */
	private static synchronized ITerminalConnector getConnector(IRemoteConnection irc) throws RemoteConnectionException {
		IRemoteConnectionHostService hostSvc = irc.getService(IRemoteConnectionHostService.class);
		ITerminalConnector con = cons.get(hostSvc.getHostname());
		if (con == null) {
			con = TerminalConnectorExtension.makeTerminalConnector("org.eclipse.tm.terminal.RemoteConnector"); //$NON-NLS-1$
			cons.put(hostSvc.getHostname(), con);
		}
		return con;
	}

	private void connector(IProject prj) {
		IRemoteConnection irc = Util.getRemoteConnection(prj);
		if (irc == null) {
			return;
		}

		IRemoteConnectionHostService hostSvc = irc.getService(IRemoteConnectionHostService.class);

		// Define the terminal properties
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(ITerminalsConnectorConstants.PROP_TITLE, irc.getName());
		properties.put(ITerminalsConnectorConstants.PROP_ENCODING, "UTF-8"); //$NON-NLS-1$
		properties.put(ITerminalsConnectorConstants.PROP_PROCESS_WORKING_DIR, "/tmp"); //$NON-NLS-1$

		// Create the done callback object
		ITerminalService.Done done = new ITerminalService.Done() {
			@Override
			public void done(IStatus done) {
				// Place any post processing here
			}
		};

		// Open the terminal
		ITerminalService terminal = TerminalServiceFactory.getService();
		if (terminal != null) {
			terminal.openConsole(properties, done);
		}
	}
}

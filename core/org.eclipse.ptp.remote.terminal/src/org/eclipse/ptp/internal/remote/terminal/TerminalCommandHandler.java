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
import org.eclipse.tm.terminal.connector.remote.IRemoteSettings;
import org.eclipse.tm.terminal.view.core.TerminalServiceFactory;
import org.eclipse.tm.terminal.view.core.interfaces.ITerminalService;
import org.eclipse.tm.terminal.view.core.interfaces.constants.ITerminalsConnectorConstants;
import org.eclipse.ui.handlers.HandlerUtil;

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

	private void connector(IProject prj) {
		IRemoteConnection irc = Util.getRemoteConnection(prj);
		if (irc != null) {
			// Define the terminal properties
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(IRemoteSettings.REMOTE_SERVICES, irc.getConnectionType().getId());
			properties.put(IRemoteSettings.CONNECTION_NAME, irc.getName());

			properties.put(ITerminalsConnectorConstants.PROP_DELEGATE_ID,
					"org.eclipse.tm.terminal.connector.telnet.launcher.remote"); //$NON-NLS-1$
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
}

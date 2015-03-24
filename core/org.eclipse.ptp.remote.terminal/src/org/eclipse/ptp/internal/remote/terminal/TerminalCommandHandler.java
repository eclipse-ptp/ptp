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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.core.IRemoteConnectionHostService;
import org.eclipse.remote.core.exception.RemoteConnectionException;
import org.eclipse.remote.internal.terminal.view.ITerminalView;
import org.eclipse.remote.internal.terminal.view.TerminalView;
import org.eclipse.remote.terminal.IRemoteSettings;
import org.eclipse.tm.internal.terminal.provisional.api.ISettingsStore;
import org.eclipse.tm.internal.terminal.provisional.api.ITerminalConnector;
import org.eclipse.tm.internal.terminal.provisional.api.TerminalConnectorExtension;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
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
		try {
			IRemoteConnection irc = Util.getRemoteConnection(prj);
			if (irc == null) {
				return;
			}

			IRemoteConnectionHostService hostSvc = irc.getService(IRemoteConnectionHostService.class);
			ITerminalConnector con = getConnector(irc);
			final ITerminalView tvr = (ITerminalView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.showView("org.eclipse.tm.terminal.view.TerminalView", hostSvc.getHostname(), IWorkbenchPage.VIEW_CREATE); //$NON-NLS-1$

			ISettingsStore store = new HashSettingsStore();
			con.save(store);
			store.put(IRemoteSettings.CONNECTION_NAME, irc.getName());
			store.put(IRemoteSettings.REMOTE_SERVICES, irc.getConnectionType().getId());
			con.load(store);
			tvr.newTerminal(con);

			// Set the terminal name, if possible
			if (tvr instanceof TerminalView) {
				TerminalView tv = (TerminalView) tvr;
				IConfigurationElement cfig = new TitleConfigurationElement(irc.getName());
				tv.setInitializationData(cfig, null, null);
			}
		} catch (CoreException e1) {
			Activator.log(e1);
		}
	}
}

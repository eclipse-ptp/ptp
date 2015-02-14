/*******************************************************************************
 * Copyright (c) 2012 Oak Ridge National Laboratory and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    John Eblen - initial implementation
 *******************************************************************************/
package org.eclipse.ptp.internal.rdt.sync.ui.handlers;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ptp.internal.rdt.sync.ui.RDTSyncUIPlugin;
import org.eclipse.ptp.internal.rdt.sync.ui.messages.Messages;
import org.eclipse.ptp.rdt.sync.core.handlers.IMissingConnectionHandler;
import org.eclipse.remote.core.IRemoteConnectionType;
import org.eclipse.remote.core.IRemoteConnectionWorkingCopy;
import org.eclipse.remote.core.exception.RemoteConnectionException;
import org.eclipse.remote.ui.IRemoteUIConnectionService;
import org.eclipse.remote.ui.IRemoteUIConnectionWizard;
import org.eclipse.remote.ui.widgets.RemoteConnectionWidget;

public class CommonMissingConnectionHandler implements IMissingConnectionHandler {
	private static long lastMissingConnectiontDialogTimeStamp = 0;
	private static final long timeBetweenDialogs = 5000; // 5 seconds

	@Override
	public void handle(final IRemoteConnectionType connectionType, final String connectionName) {
		RDTSyncUIPlugin.getStandardDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				// Avoid flooding the display with missing connection dialogs
				if (System.currentTimeMillis() - lastMissingConnectiontDialogTimeStamp <= timeBetweenDialogs) {
					return;
				}
				lastMissingConnectiontDialogTimeStamp = System.currentTimeMillis();
				String[] buttonLabels = new String[2];
				buttonLabels[0] = IDialogConstants.OK_LABEL;
				buttonLabels[1] = Messages.CommonMissingConnectionHandler_6;
				String newline = System.getProperty("line.separator"); //$NON-NLS-1$
				MessageDialog dialog = new MessageDialog(null, Messages.CommonMissingConnectionHandler_0, null,
						Messages.CommonMissingConnectionHandler_1 + connectionName + Messages.CommonMissingConnectionHandler_2
								+ newline + newline + Messages.CommonMissingConnectionHandler_3 + newline
								+ Messages.CommonMissingConnectionHandler_4 + newline + Messages.CommonMissingConnectionHandler_5,
						MessageDialog.ERROR, buttonLabels, 0);
				int buttonPressed = dialog.open();
				if (buttonPressed == 1) {
					IRemoteUIConnectionService connectionServices = connectionType.getService(IRemoteUIConnectionService.class);
					if (connectionServices != null) {
						IRemoteUIConnectionWizard wizard = connectionServices.getConnectionWizard(dialog.getShell());
						wizard.setConnectionName(RemoteConnectionWidget.DEFAULT_CONNECTION_NAME);
						IRemoteConnectionWorkingCopy wc = wizard.open();
						try {
							wc.save();
						} catch (RemoteConnectionException e) {
							RDTSyncUIPlugin.log(e);
						}
					}
				}
			}
		});
	}
}
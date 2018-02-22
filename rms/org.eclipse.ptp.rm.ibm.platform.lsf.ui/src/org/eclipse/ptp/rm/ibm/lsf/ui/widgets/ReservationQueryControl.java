// Copyright (c) 2013 IBM Corporation and others. All rights reserved. 
// This program and the accompanying materials are made available under the 
// terms of the Eclipse Public License v1.0s which accompanies this distribution, 
// and is available at http://www.eclipse.org/legal/epl-v10.html

package org.eclipse.ptp.rm.ibm.lsf.ui.widgets;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Vector;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.ptp.rm.ibm.lsf.ui.LSFCommand;
import org.eclipse.ptp.rm.jaxb.control.ui.IWidgetDescriptor2;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class ReservationQueryControl extends LSFQueryControl {
	private static final String queryCommand[] = { "brsvs", "-w" }; //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * Create the custom widget for the JAXB ui. In this case the widget is a
	 * push button that pops up a dialog with a list of active reservations when
	 * the button is pushed.
	 * 
	 * @param parent
	 *            : Container for the widget
	 * @param wd
	 *            : Information about the custom widget
	 */
	public ReservationQueryControl(Composite parent, final IWidgetDescriptor2 wd) {
		super(parent, wd);
		queryTitle = Messages.ReservationQueryTitle;
	}

	@Override
	protected void configureQueryButton(Button button, final IRemoteConnection connection) {
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			/**
			 * Handle button press event. Pop up a dialog listing reservations. If the user
			 * selects a reservation and clicks the ok button notify listeners that this
			 * widget has been modified.
			 * 
			 * @param e:
			 *            The selection event
			 */
			public void widgetSelected(SelectionEvent e) {
				getQueryResponse(connection);
			}
		});
	}

	@Override
	protected void processCommandResponse(LSFCommand command, IStatus runStatus) {
		/*
		 * brsvs command output has two line output formats, reservation attributes, with
		 * 6 columns and host reservations, with two columns. Fill out any row for a
		 * host reservation so it has 6 columns and move the host info to column 4 so
		 * it appears in the correct column in the table display
		 */
		if (runStatus.getSeverity() == IStatus.OK) {
			Vector<String[]> commandResponse = command.getCommandResponse();
			for (int i = 0; i < commandResponse.size(); i++) {
				String columns[] = commandResponse.elementAt(i);
				if (columns.length == 2) {
					String newColumns[] = new String[6];
					Arrays.fill(newColumns, ""); //$NON-NLS-1$
					newColumns[4] = columns[1];
					commandResponse.setElementAt(newColumns, i);
				}
			}
		}
		super.processCommandResponse(command, runStatus);
	}

	/**
	 * Issue the 'brsvs' command to query the reservation list and set up the
	 * column heading and reservation data arrays.
	 * 
	 * @param connection
	 *            : Connection to the remote system
	 */
	@Override
	protected void getQueryResponse(IRemoteConnection connection) {
		try {
			IStatus runStatus;
			LSFCommand command;

			command = new LSFCommand(Messages.ReservationCommandDesc, connection, queryCommand);
			widgetDescriptor.getLaunchConfigurationDialog().run(true, true, command);
			runStatus = command.getRunStatus();
			processCommandResponse(command, runStatus);
		} catch (InvocationTargetException e) {
			// Do nothing
		} catch (InterruptedException e) {
			// Do nothing
		}
	}
}

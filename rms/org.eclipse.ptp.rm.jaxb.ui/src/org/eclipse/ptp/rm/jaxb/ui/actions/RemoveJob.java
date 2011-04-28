/*******************************************************************************
 * Copyright (c) 2011 University of Illinois All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html 
 * 	
 * Contributors: 
 * 	Albert L. Rossi - design and implementation
 ******************************************************************************/
package org.eclipse.ptp.rm.jaxb.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ptp.rm.jaxb.ui.data.PersistentCommandJobStatus;
import org.eclipse.ptp.rm.jaxb.ui.messages.Messages;

/**
 * Removes the job from the list.
 * 
 * @author arossi
 * 
 */
public class RemoveJob extends AbstractStatusAction {
	/*
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		if (MessageDialog.openQuestion(view.getSite().getShell(), Messages.CannotUndoOperation, Messages.RemoveJobWarning)) {
			view.removeJob(status.getJobId());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ptp.rm.jaxb.ui.actions.AbstractStatusAction#validate(org.
	 * eclipse.jface.action.IAction,
	 * org.eclipse.ptp.rm.jaxb.ui.data.PersistentCommandJobStatus)
	 */
	@Override
	protected void validate(IAction action, PersistentCommandJobStatus status) {
		action.setEnabled(true);
	}
}

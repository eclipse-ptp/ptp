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

package org.eclipse.ptp.internal.rdt.sync.git.ui;

import java.net.URI;

import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.ptp.internal.rdt.sync.git.ui.messages.Messages;
import org.eclipse.ptp.rdt.sync.core.SyncConfigManager;
import org.eclipse.ptp.rdt.sync.ui.AbstractSynchronizeParticipant;
import org.eclipse.ptp.rdt.sync.ui.ISynchronizeParticipant;
import org.eclipse.ptp.rdt.sync.ui.ISynchronizeParticipantDescriptor;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.core.IRemoteConnectionHostService;
import org.eclipse.remote.core.IRemoteFileService;
import org.eclipse.remote.ui.IRemoteUIConnectionService;
import org.eclipse.remote.ui.IRemoteUIConstants;
import org.eclipse.remote.ui.IRemoteUIFileService;
import org.eclipse.remote.ui.widgets.RemoteConnectionWidget;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Launches a dialog that configures a remote sync target with OK and Cancel
 * buttons. Also has a text field to allow the name of the configuration to be
 * changed.
 */
public class GitParticipant extends AbstractSynchronizeParticipant {
	private static final String FILE_SCHEME = "file"; //$NON-NLS-1$
	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private IRemoteConnection fSelectedConnection;

	private String fProjectName = ""; //$NON-NLS-1$
	private Button fBrowseButton;

	private Text fLocationText;
	private RemoteConnectionWidget fRemoteConnectionWidget;
	private IWizardContainer container;

	private boolean fRemoteDirSelected;
	private boolean fRemoteDirFocused;

	// If false, automatically select "Remote Tools" provider instead of letting the user select the provider.
	private final boolean showProviderCombo = false;

	public GitParticipant(ISynchronizeParticipantDescriptor descriptor) {
		super(descriptor);
	}

	/**
	 * Attempt to open a connection.
	 */
	private void checkConnection() {
		IRemoteUIConnectionService svc = getUIConnectionService();
		if (svc != null) {
			svc.openConnectionWithProgress(fRemoteConnectionWidget.getShell(), null, fSelectedConnection);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ptp.rdt.sync.ui.ISynchronizeParticipant#createConfigurationArea
	 * (org.eclipse.swt.widgets.Composite,
	 * org.eclipse.jface.operation.IRunnableContext)
	 */
	@Override
	public void createConfigurationArea(Composite parent, IRunnableContext context) {
		this.container = (IWizardContainer) context;
		final Composite configArea = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		configArea.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		configArea.setLayoutData(gd);

		int flags = showProviderCombo ? RemoteConnectionWidget.FLAG_FORCE_CONNECTION_TYPE_SELECTION : 0;
		flags |= RemoteConnectionWidget.FLAG_NO_LOCAL_SELECTION;
		fRemoteConnectionWidget = new RemoteConnectionWidget(configArea, SWT.NONE, null, flags, context);
		fRemoteConnectionWidget.filterConnections(IRemoteConnectionHostService.class, IRemoteFileService.class);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		gd.grabExcessHorizontalSpace = true;
		fRemoteConnectionWidget.setLayoutData(gd);
		fRemoteConnectionWidget.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleConnectionSelected();
			}
		});

		Label locationLabel = new Label(configArea, SWT.LEFT);
		locationLabel.setText(Messages.GitParticipant_location);

		fLocationText = new Text(configArea, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		gd.grabExcessHorizontalSpace = true;
		gd.widthHint = 250;
		fLocationText.setLayoutData(gd);
		fLocationText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				// MBSCustomPageManager.addPageProperty(REMOTE_SYNC_WIZARD_PAGE_ID,
				// PATH_PROPERTY, fLocationText.getText());
				update();
				if (fRemoteDirFocused) {
					fRemoteDirSelected = true;
				}
			}
		});
		// Listener to control where the focus is when the remote directory value is changed
		fLocationText.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0) {
				fRemoteDirFocused = false;
			}
			@Override
			public void focusGained(FocusEvent arg0) {
				fRemoteDirFocused = true;
			}
		});
		handleConnectionSelected();

		// new connection button
		fBrowseButton = new Button(configArea, SWT.PUSH);
		fBrowseButton.setText(Messages.GitParticipant_browse);
		fBrowseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (fSelectedConnection != null) {
					checkConnection();
					if (fSelectedConnection.isOpen()) {
						IRemoteUIFileService fileSvc = fSelectedConnection.getConnectionType().getService(
								IRemoteUIFileService.class);
						if (fileSvc != null) {
							fileSvc.setConnection(fSelectedConnection);
							String correctPath = fLocationText.getText();
							String selectedPath = fileSvc.browseDirectory(
									fLocationText.getShell(),
									"Project Location (" + fSelectedConnection.getName() + ")", correctPath, IRemoteUIConstants.NONE); //$NON-NLS-1$ //$NON-NLS-2$
							if (selectedPath != null) {
								fLocationText.setText(selectedPath);
								fRemoteDirSelected = true;
							}
						}
					}
				}
			}
		});
	}

	/**
	 * Return the path we are going to display. If it is a file URI then remove
	 * the file prefix.
	 * 
	 * Only do this if the connection is open. Otherwise we will attempt to
	 * connect to the first machine in the list, which is annoying.
	 * 
	 * @return String
	 */
	private String getDefaultPathDisplayString() {
		//		String projectName = ""; //$NON-NLS-1$
		// IWizardPage page = getWizard().getStartingPage();
		// if (page instanceof CDTMainWizardPage) {
		// projectName = ((CDTMainWizardPage) page).getProjectName();
		// }
		if (fSelectedConnection != null && fSelectedConnection.isOpen()) {
			IRemoteFileService fileSvc = fSelectedConnection.getService(IRemoteFileService.class);
			if (fileSvc != null) {
				URI defaultURI = fileSvc.toURI(fileSvc.getBaseDirectory());

				// Handle files specially. Assume a file if there is no project to
				// query
				if (defaultURI != null && defaultURI.getScheme().equals(FILE_SCHEME)) {
					return Platform.getLocation().append(fProjectName).toString();
				}
				if (defaultURI == null) {
					return ""; //$NON-NLS-1$
				}
				return new Path(defaultURI.getPath()).append(fProjectName).toString();
			}
		}
		return ""; //$NON-NLS-1$
	}

	/**
	 * @see ISynchronizeParticipant#getErrorMessage()
	 */
	@Override
	public String getErrorMessage() {
		if (fSelectedConnection == null) {
			return Messages.GitParticipant_1;
		}
		if (fLocationText.getText().length() == 0) {
			return Messages.GitParticipant_2;
		}
		IRemoteFileService fileSvc = fSelectedConnection.getService(IRemoteFileService.class);
		if (fileSvc != null && fileSvc.toURI(fLocationText.getText()) == null) {
			return Messages.GitParticipant_3;
		}
		// should we check permissions of: fileManager.getResource(fLocationText.getText()).getParent() ?
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.rdt.sync.ui.ISynchronizeParticipant#getConnection()
	 */
	@Override
	public IRemoteConnection getConnection() {
		return fSelectedConnection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.rdt.sync.ui.ISynchronizeParticipant#getLocation()
	 */
	@Override
	public String getLocation() {
		return fLocationText.getText();
	}

	@Override
	public String getSyncConfigName() {
		String configName = fSelectedConnection.getName();
		// Fix for bug 463534
		if (configName.equals(SyncConfigManager.getLocalConfigName())) {
			configName += "-Sync";  //$NON-NLS-1$
		}
		return configName;
	}

	/**
	 * @return
	 */
	private IRemoteUIConnectionService getUIConnectionService() {
		if (fSelectedConnection != null) {
			return fSelectedConnection.getConnectionType().getService(IRemoteUIConnectionService.class);
		}
		return null;
	}

	/**
	 * Handle new connection selected
	 */
	private void handleConnectionSelected() {
		fSelectedConnection = fRemoteConnectionWidget.getConnection();
		fLocationText.setText(getDefaultPathDisplayString());
		update();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ptp.rdt.sync.ui.ISynchronizeParticipant#isConfigComplete()
	 */
	@Override
	public boolean isConfigComplete() {
		return getErrorMessage() == null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ptp.rdt.sync.ui.ISynchronizeParticipant#setProjectName(String projectName)
	 */
	@Override
	public void setProjectName(String projectName) {
		fProjectName = projectName;
		// If remote directory field is empty
		if(fLocationText.getText().equals(EMPTY_STRING)) {
			fRemoteDirSelected = false;
		}
		// If remote directory was not selected yet
		if (!fRemoteDirSelected) {
			fLocationText.setText(getDefaultPathDisplayString());
		}
	}

	private void update() {
		container.updateMessage();
		// updateButtons() may fail if current page is null. This can happen if update() is called during wizard/page creation.
		if (container.getCurrentPage() == null) {
			return;
		}
		container.updateButtons();
	}
}

/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     John Eblen - Oak Ridge National Laboratory - change to use sync manager
 *                  and move to ui package
 *******************************************************************************/
package org.eclipse.ptp.internal.rdt.sync.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ptp.internal.rdt.sync.ui.handlers.CommonSyncExceptionHandler;
import org.eclipse.ptp.internal.rdt.sync.ui.messages.Messages;
import org.eclipse.ptp.rdt.sync.core.SyncConfig;
import org.eclipse.ptp.rdt.sync.core.SyncConfigManager;
import org.eclipse.ptp.rdt.sync.core.SyncFlag;
import org.eclipse.ptp.rdt.sync.core.SyncManager;
import org.eclipse.ptp.rdt.sync.core.SyncManager.SyncMode;
import org.eclipse.ptp.rdt.sync.core.resources.RemoteSyncNature;

public class ResourceChangeListener {
	// Avoid sync'ing on Eclipse-related deltas, such as changes to file markers.
	static int DELTA_OPS_TO_SYNC = IResourceDelta.CONTENT      | IResourceDelta.MOVED_FROM | IResourceDelta.MOVED_TO |
								   IResourceDelta.COPIED_FROM  | IResourceDelta.OPEN       | IResourceDelta.REPLACED |
								   IResourceDelta.LOCAL_CHANGED;
	private ResourceChangeListener() {
	}

	public static void startListening() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceListener,
				IResourceChangeEvent.POST_CHANGE | IResourceChangeEvent.PRE_DELETE);
	}

	public static void stopListening() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceListener);
	}

	private static IResourceChangeListener resourceListener = new IResourceChangeListener() {
		@Override
		public void resourceChanged(IResourceChangeEvent event) {
			// Turn off sync'ing for a project before deleting it and close repository - see bug 360170
			// Note that event.getDelta() returns null, so this event cannot be handled inside the loop below.
			if (event.getType() == IResourceChangeEvent.PRE_DELETE) {
				IProject project = (IProject) event.getResource();
				if (!RemoteSyncNature.hasNature(project)) {
					return;
				}
				SyncManager.setSyncMode(project, SyncMode.UNAVAILABLE);
				String currentSyncServiceId = SyncConfigManager.getActive(project).getSyncProviderId();
				try {
					SyncManager.getSyncService(currentSyncServiceId).close(project);
				} catch (CoreException e) {
					RDTSyncUIPlugin.log(e);
				}
				return;
			}
			if(!SyncManager.getSyncAuto()) {
				return;
			}
			IResourceDeltaVisitor deltaVisitor = new IResourceDeltaVisitor() {
				@Override
				public boolean visit(IResourceDelta delta) throws CoreException {
					IProject project = delta.getResource().getProject();

					// Only interested in leaf nodes of sync projects
					// A null project probably indicates the workspace root
					if (project == null || (delta.getAffectedChildren().length > 0)) {
						return true;
					}

					if ((!RemoteSyncNature.hasNature(project))) {
						return false;
					}

					int kind = delta.getKind();
					int flags = delta.getFlags();
					// Always sync adds or removes.
					// Sync changes only of certain types as specified in DELTA_OPS_TO_SYNC.
					if ((( kind & ( IResourceDelta.ADDED | IResourceDelta.REMOVED )) != 0) ||
						(((kind & IResourceDelta.CHANGED) != 0) && ((flags & DELTA_OPS_TO_SYNC) != 0))) {

						SyncConfig syncConfig = SyncConfigManager.getActive(project);
						// syncConfig can be null when sync nature is added to the project as this generates a resource change event
						if (syncConfig != null && syncConfig.isSyncOnSave()) {
							SyncMode syncMode = SyncManager.getSyncMode(project);
							try {
								// Do a local-to-remote sync to update any changes reported in delta.
								if (syncMode == SyncMode.ALL) {
									SyncManager.syncAll(delta, project, SyncFlag.LR_ONLY, new CommonSyncExceptionHandler(true,false));
								} else if (syncMode == SyncMode.ACTIVE) {
									SyncManager.sync(delta, project, SyncFlag.LR_ONLY, new CommonSyncExceptionHandler(true, false));
								}
							} catch (CoreException e) {
								// This should never happen because only a blocking sync can throw a core exception, and all syncs here
								// are
								// non-blocking.
								RDTSyncUIPlugin.log(Messages.ResourceChangeListener_0, e);
							}
						}
					}
					return true;
				}
			};
			try {
				event.getDelta().accept(deltaVisitor);
			} catch (CoreException e) {
				RDTSyncUIPlugin.log(Messages.ResourceChangeListener_1, e);
			}
		}
	};
}

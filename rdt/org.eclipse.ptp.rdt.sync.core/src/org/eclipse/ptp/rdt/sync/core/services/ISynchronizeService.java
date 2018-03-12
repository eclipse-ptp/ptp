/*******************************************************************************
 * Copyright (c) 2008, 2010, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.ptp.rdt.sync.core.services;

import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ptp.rdt.sync.core.AbstractSyncFileFilter;
import org.eclipse.ptp.rdt.sync.core.RemoteLocation;
import org.eclipse.ptp.rdt.sync.core.SyncFlag;

/**
 * Provides synchronization services.
 * 
 * @since 4.0
 */
public interface ISynchronizeService extends ISynchronizeServiceDescriptor {

	/**
	 * Replace the current contents of the given paths with the previous versions in the repository
	 * 
	 * @param project
	 * @param path
	 * @throws CoreException
	 * @since 4.0
	 */
	public void checkout(IProject project, IPath[] paths) throws CoreException;

	/**
	 * Replace the current contents of the given paths with the current local copies of the remote (not necessarily the same as what
	 * is on the remote site). This is useful in merge-conflict resolution.
	 * 
	 * @param project
	 * @param path
	 * @throws CoreException
	 * @since 4.0
	 */
	public void checkoutRemoteCopy(IProject project, IPath[] paths) throws CoreException;

	/**
	 * Close any resources (files, sockets) that were open by the sync provider for the given project. Resources not open by the
	 * provider should not be touched. This is called, for example, when a project is about to be deleted.
	 * 
	 * @throws CoreException
	 */
	public void close(IProject project) throws CoreException;

	/**
	 * Get the current list of merge-conflicted files for the passed project and build scenario
	 * 
	 * @param project
	 * @return set of files as project-relative IPaths. This may be an empty set but never null.
	 * @throws CoreException
	 *             for system-level problems retrieving merge information
	 * @since 4.0
	 */
	public Set<IPath> getMergeConflictFiles(IProject project) throws CoreException;

	/**
	 * Get the three parts of the merge-conflicted file (left, right, and ancestor, respectively)
	 * 
	 * @param project
	 * @param file
	 * @return the three parts as strings. Either three strings (some may be empty) or null if file is not merge-conflicted.
	 * @throws CoreException
	 *             for system-level problems retrieving merge information
	 * @since 4.0
	 */
	public String[] getMergeConflictParts(IProject project, IFile file) throws CoreException;

	/**
	 * @since 4.0
	 * Set the given file paths as resolved (merge conflict does not exist)
	 * 
	 * @param project
	 * @param path
	 * @throws CoreException
	 *             for system-level problems setting the state
	 * @since 4.0
	 */
	public void setMergeAsResolved(IProject project, IPath[] paths) throws CoreException;

	/**
	 * @since 4.0
	 * Perform synchronization
	 * 
	 * @param project
	 *            project to sync - cannot be null
	 * @param remoteLoc
	 *			  remote sync target - cannot be null.
	 *			  Warning: Recommended that both clients and implementors make a copy to prevent subtle bugs if object is modified
	 * @param delta
	 *            resources requiring synchronization
	 * @param monitor
	 *            progress monitor for monitoring or canceling sync
	 * @param syncFlags
	 *            Various flags for the sync call. For example, the sync can be
	 *            forced, either to local (from remote) or to remote (from
	 *            local). If forced, it is guaranteed to happen before
	 *            returning. Otherwise, it may happen at any time.
	 * @throws CoreException
	 *             if synchronization fails
	 * @since 4.0
	 */
	public void synchronize(IProject project, RemoteLocation remoteLoc, IResourceDelta delta,
			IProgressMonitor monitor, Set<SyncFlag> syncFlags) throws CoreException;

	/**
	 * Get SyncFileFilter for given project
	 * 
	 * @param project
	 * 
	 * @return file filter
	 * @throws CoreException 
	 */
	public AbstractSyncFileFilter getSyncFileFilter(IProject project) throws CoreException;

	/**
	 * Set sync file filter for the given project
	 * 
	 * @param project
	 *            - cannot be null
	 * @param filter
	 *            generic file filter - cannot be null
	 * @throws CoreException
	 */
	public void setSyncFileFilter(IProject project, AbstractSyncFileFilter filter) throws CoreException;
}

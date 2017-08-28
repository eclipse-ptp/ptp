/*******************************************************************************
 * Copyright (c) 2013 The University of Tennessee and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Roland Schulz - initial implementation
 *******************************************************************************/

package org.eclipse.ptp.rdt.sync.core;

import java.util.NoSuchElementException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ptp.internal.rdt.sync.core.RDTSyncCorePlugin;
import org.eclipse.ptp.internal.rdt.sync.core.SyncUtils;
import org.eclipse.ptp.internal.rdt.sync.core.messages.Messages;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * Class for storing file filter in eclipse preferences. Does not implement any filtering.
 * 
 * @since 3.0
 */
public class PreferenceSyncFileFilterStorage extends AbstractSyncFileFilter {
	private class PreferenceIgnoreRule extends AbstractIgnoreRule {
		String pattern;
		boolean exclude;

		PreferenceIgnoreRule(String p, boolean e) {
			pattern = p;
			exclude = e;
		}

		PreferenceIgnoreRule(String r) {
			pattern = r.substring(1);
			exclude = r.charAt(0) == '-';
		}

		@Override
		public boolean isMatch(IResource target) {
			throw new UnsupportedOperationException(); // only for storage - doesn't support matching
		}

		@Override
		public boolean isMatch(String target, boolean isFolder) {
			throw new UnsupportedOperationException(); // only for storage - doesn't support matching
		}

		@Override
		public boolean getResult() {
			return exclude;
		}

		@Override
		public String toString() {
			return (exclude ? "-" : "+") + pattern; //$NON-NLS-1$//$NON-NLS-2$
		}

		@Override
		public String getPattern() {
			return pattern;
		}
	}

	private static final String PATTERN_NODE_NAME = "pattern"; //$NON-NLS-1$
	private static final String NUM_PATTERNS_KEY = "num-patterns"; //$NON-NLS-1$
	private static final String ATTR_RULE = "rule"; //$NON-NLS-1$

	/**
	 * Create an instance of a PreferenceSyncFileFilterStorage
	 */
	public PreferenceSyncFileFilterStorage() {
	};

	/**
	 * Create an instance of a PreferenceSyncFileFilterStorage
	 * 
	 * @param filter
	 *            persistent storage for filters
	 */
	public PreferenceSyncFileFilterStorage(PreferenceSyncFileFilterStorage filter) {
		rules.addAll(filter.rules);
	}

	/**
	 * Load the default filters
	 */
	public void loadBuiltInDefaultFilter() {
		for (String pattern : java.util.Arrays.asList(new String[] {
                "/.project", "/.cproject", "/.settings/", "coredir.[0-9]*/", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                "core", "!core/", "core.[0-9]*", "!core.[0-9]*/",            //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                "CVS/"})) { //$NON-NLS-1$
			addPattern(pattern, true);
		}
	}

	/**
	 * Load filters from persistent storage
	 * 
	 * @return
	 */
	public boolean loadFilter(IScopeContext context) {
		rules.clear();
		Preferences node = context.getNode(RDTSyncCorePlugin.PLUGIN_ID);
		if (node == null) {
			RDTSyncCorePlugin.log(Messages.SyncManager_6);
			return false;
		}
		try {
			if (!node.nodeExists(PATTERN_NODE_NAME)) {
				return false;
			}
			Preferences prefPatternNode = node.node(PATTERN_NODE_NAME);
			int numPatterns = prefPatternNode.getInt(NUM_PATTERNS_KEY, -1);
			if (numPatterns == -1) {
				RDTSyncCorePlugin.log(Messages.SyncFileFilter_1);
				return false;
			}

			for (int i = 0; i < numPatterns; i++) {
				if (!prefPatternNode.nodeExists(Integer.toString(i))) {
					RDTSyncCorePlugin.log(Messages.SyncFileFilter_1);
					rules.clear();
					return false;
				}

				Preferences prefMatcherNode = prefPatternNode.node(Integer.toString(i));

				String p = prefMatcherNode.get(ATTR_RULE, null);
				if (p == null) {
					rules.clear();
					throw new NoSuchElementException(Messages.PathResourceMatcher_0);
				}
				rules.add(new PreferenceIgnoreRule(p));
			}
			return true;
		} catch (BackingStoreException e) {
			RDTSyncCorePlugin.log(Messages.SyncFileFilter_1, e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.rdt.sync.core.AbstractSyncFileFilter#saveFilter()
	 */
	@Override
	public void saveFilter() {
		IScopeContext context = InstanceScope.INSTANCE;
		Preferences prefRootNode = context.getNode(RDTSyncCorePlugin.PLUGIN_ID);
		if (prefRootNode == null) {
			RDTSyncCorePlugin.log(Messages.SyncManager_6);
			return;
		}
		// To clear pattern information, remove node, flush parent, and then recreate the node
		try {
			prefRootNode.node(PATTERN_NODE_NAME).removeNode();
			prefRootNode.flush();
		} catch (BackingStoreException e) {
			RDTSyncCorePlugin.log(Messages.SyncFileFilter_2, e);
			return;
		}
		Preferences prefPatternNode = prefRootNode.node(PATTERN_NODE_NAME);
		prefPatternNode.putInt(NUM_PATTERNS_KEY, rules.size());
		for (int i = 0; i < rules.size(); i++) {
			Preferences prefRuleNode = prefPatternNode.node(Integer.toString(i));
			// Whether pattern is exclusive or inclusive
			prefRuleNode.put(ATTR_RULE, rules.get(i).toString());
		}
		SyncUtils.flushNode(prefRootNode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.rdt.sync.core.AbstractSyncFileFilter#addPattern(java.lang.String, boolean, int)
	 */
	@Override
	public void addPattern(String pattern, boolean exclude, int index) {
		rules.add(index, new PreferenceIgnoreRule(pattern, exclude));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.rdt.sync.core.AbstractSyncFileFilter#addPattern(org.eclipse.core.resources.IResource, boolean, int)
	 */
	@Override
	public void addPattern(IResource resource, boolean exclude, int index) {
		rules.add(index, new PreferenceIgnoreRule(resource.getProjectRelativePath().toString(), exclude));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.rdt.sync.core.AbstractSyncFileFilter#clone()
	 */
	@Override
	public AbstractSyncFileFilter clone() {
		return new PreferenceSyncFileFilterStorage(this);
	}
}

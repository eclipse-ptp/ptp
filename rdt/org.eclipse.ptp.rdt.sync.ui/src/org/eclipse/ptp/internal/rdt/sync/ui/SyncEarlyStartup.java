package org.eclipse.ptp.internal.rdt.sync.ui;

import org.eclipse.ui.IStartup;

/*
 * This class exists to start synchronized projects on workbench startup (bug 485402).
 */
public class SyncEarlyStartup implements IStartup {
	@Override
	public void earlyStartup() {
		// nothing to do
	}
}

/*******************************************************************************
 * Copyright (c) 2012 IBM and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.ptp.internal.remote.terminal;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

public class TerminalPrefsInitializer extends AbstractPreferenceInitializer {
	public static final String SHELL_STARTUP_COMMAND = "SHELL_STARTUP_COMMAND";//$NON-NLS-1$
	public static final String SHELL_STARTUP_DEFAULT = "source .ptprc.sh"; //$NON-NLS-1$

	@Override
	public void initializeDefaultPreferences() {
		IEclipsePreferences defaultPrefs = DefaultScope.INSTANCE.getNode(Activator.getUniqueIdentifier());
		defaultPrefs.put(SHELL_STARTUP_COMMAND, SHELL_STARTUP_DEFAULT);
	}

}

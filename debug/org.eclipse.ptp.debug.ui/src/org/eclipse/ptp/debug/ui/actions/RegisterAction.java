/*******************************************************************************
 * Copyright (c) 2005 The Regents of the University of California. 
 * This material was produced under U.S. Government contract W-7405-ENG-36 
 * for Los Alamos National Laboratory, which is operated by the University 
 * of California for the U.S. Department of Energy. The U.S. Government has 
 * rights to use, reproduce, and distribute this software. NEITHER THE 
 * GOVERNMENT NOR THE UNIVERSITY MAKES ANY WARRANTY, EXPRESS OR IMPLIED, OR 
 * ASSUMES ANY LIABILITY FOR THE USE OF THIS SOFTWARE. If software is modified 
 * to produce derivative works, such modified software should be clearly marked, 
 * so as not to confuse it with the version available from LANL.
 * 
 * Additionally, this program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * LA-CC 04-115
 *******************************************************************************/
package org.eclipse.ptp.debug.ui.actions;

import org.eclipse.ptp.debug.ui.ImageUtil;
import org.eclipse.ptp.debug.ui.views.DebugParallelView;
import org.eclipse.ptp.ui.model.IElement;
import org.eclipse.ptp.ui.old.UIUtils;
/**
 * @author clement chu
 *
 */
public class RegisterAction extends DebugAction {
	public static final String name = "Register Selected Elements";
	private int NUM_PROCESS_WARNING = 10;
	
	public RegisterAction(DebugParallelView view) {
		super(name, view);
	    setImageDescriptor(ImageUtil.ID_ICON_REGISTER_NORMAL);
	    setDisabledImageDescriptor(ImageUtil.ID_ICON_REGISTER_DISABLE);
	    setEnabled(true);
	}

	public void run(IElement[] elements) {
		if (validation(elements)) {
			if (elements.length > NUM_PROCESS_WARNING) {
				if (!UIUtils.showQuestionDialog("Register Confirmation", "Are you sure you want to register (" + elements.length + ") processes?")) {
					return;
				}
			}
			
			DebugParallelView debugView = (DebugParallelView)view;
			debugView.registerSelectedElements();
			debugView.redraw();
		}
	}	
}

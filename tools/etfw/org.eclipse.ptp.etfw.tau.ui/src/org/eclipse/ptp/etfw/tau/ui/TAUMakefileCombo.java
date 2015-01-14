/****************************************************************************
 *			Tuning and Analysis Utilities
 *			http://www.cs.uoregon.edu/research/paracomp/tau
 ****************************************************************************
 * Copyright (c) 1997-2006
 *    Department of Computer and Information Science, University of Oregon
 *    Advanced Computing Laboratory, Los Alamos National Laboratory
 *    Research Center Juelich, ZAM Germany	
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Wyatt Spear - initial API and implementation
 *    Chris Navarro (Illinois/NCSA) - Design and implementation
 ****************************************************************************/
package org.eclipse.ptp.etfw.tau.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.ptp.etfw.tau.ui.messages.Messages;
import org.eclipse.ptp.internal.etfw.RemoteBuildLaunchUtils;
import org.eclipse.ptp.internal.rm.jaxb.core.JAXBCoreConstants;
import org.eclipse.ptp.rm.jaxb.control.ui.AbstractWidget;
import org.eclipse.ptp.rm.jaxb.control.ui.IWidgetDescriptor;
import org.eclipse.ptp.rm.jaxb.core.IVariableMap;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * This class is based on the work by Wyatt Spear. It creates a special UI widget for use with the JAXB ETFw workflows and parses
 * the available TAU Makefiles so the user can select only available TAU Makefiles on the target machine.
 * 
 * @see TAUMakefileTab
 * 
 * @author Chris Navarro
 * 
 */
public class TAUMakefileCombo extends AbstractWidget {

	private static final String LIB_FOLDER = "lib"; //$NON-NLS-1$
	private static final String PPROF = "pprof"; //$NON-NLS-1$
	private static final String TAU = "tau"; //$NON-NLS-1$
	private static final String TRUE_SELECTION = "true"; //$NON-NLS-1$
	private static final String MPI_OPTION = "use_mpi"; //$NON-NLS-1$
	private static final String CALLPATH_OPTION = "use_callpath_profiling"; //$NON-NLS-1$
	private static final String OPARI_OPTION = "use_opari"; //$NON-NLS-1$
	private static final String OpenMP_OPTION = "use_openmp"; //$NON-NLS-1$
	private static final String PAPI_OPTION = "use_papi_library"; //$NON-NLS-1$
	private static final String TRACE_OPTION = "use_tau_tracing"; //$NON-NLS-1$
	private static final String PDT_OPTION = "use_tau_with_PDT"; //$NON-NLS-1$
	private static final String TAU_LOCATION = "tau_location"; //$NON-NLS-1$
	private static final String TAU_MAKEFILE_LIST = "tau_makefile_list"; //$NON-NLS-1$
	private static final String TAU_LIB_DIR = "tau_library_directory"; //$NON-NLS-1$

	private final Map<String, String> translateBoolean;
	private final IRemoteConnection remoteConnection;
	private final Combo combo;
	private final Button reload;
	private final RemoteBuildLaunchUtils blt;
	private IVariableMap map;
	private String selectedMakefile;
	//When running the refreshMakefilesJob, only reupdate the makefile list if this is true, don't bother with updating anything else.
	//private boolean updateOnly=true;
	//Flag set to true when doing init makefiles and we want to check the remote system, even if we already have the makefile list cached. Also checks the system default tau location if the location value is empty.
	private boolean reloadMakefiles=false;

	/**
	 * The list of all available options found among all available TAU makefiles
	 */
	protected LinkedHashSet<String> allopts = null;

	/**
	 * The list of all selected makefile options
	 */
	protected LinkedHashSet<String> selopts = null;
	/**
	 * The path to the TAU lib directory
	 */
	private String tauLibDir = null;
	private Set<String> allmakefiles = null;;
	boolean refreshing = false;

	final Job refreshMakefilesJob = new Job(Messages.TAUMakefileCombo_UpdatingMakefileList) {
		@Override
		protected IStatus run(IProgressMonitor monitor) {
			refreshing = true;
			initMakefiles();
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					//Why was this initMakefiles necessary if we already did it up above?
//					if(updateOnly) 
//					{
//						initMakefiles();
//					}
					updateMakefileCombo();
					refreshing = false;
//					updateOnly=false;
				}
			});

			return Status.OK_STATUS;
		}
	};

	public TAUMakefileCombo(Composite parent, IWidgetDescriptor wd) {
		super(parent, wd);
		// This translates UI selections to strings for parsing the makefile list
		translateBoolean = new HashMap<String, String>();
		translateBoolean.put(MPI_OPTION, "mpi"); //$NON-NLS-1$
		translateBoolean.put(CALLPATH_OPTION, "callpath"); //$NON-NLS-1$
		translateBoolean.put(OPARI_OPTION, "opari"); //$NON-NLS-1$
		translateBoolean.put(OpenMP_OPTION, "openmp"); //$NON-NLS-1$
		translateBoolean.put(PAPI_OPTION, "papi"); //$NON-NLS-1$
		translateBoolean.put(TRACE_OPTION, "trace"); //$NON-NLS-1$
		translateBoolean.put(PDT_OPTION, "pdt"); //$NON-NLS-1$

		this.remoteConnection = wd.getRemoteConnection();
		blt = new RemoteBuildLaunchUtils(remoteConnection);

		setLayout(new GridLayout(2, false));
		combo = new Combo(this, SWT.READ_ONLY);
		combo.setItems(new String[] { Messages.TAUMakefileCombo_BuildingMakefileList });
		combo.select(0);
		combo.setEnabled(false);
		
		

		combo.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				refreshMakefilesJob.cancel();
			}
		});
		
		reload = new Button(this, SWT.NONE);
		reload.setText("Reload");
		reload.setEnabled(false);

		reload.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!refreshing) {
					reloadMakefiles=true;
					refreshMakefilesJob.setUser(true);
					refreshMakefilesJob.schedule();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			
		});

//		if (allmakefiles == null) {
//			job.setUser(true);
//			job.schedule();
//		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (!refreshing) {
			//updateOnly=true;
			refreshMakefilesJob.setUser(true);
			refreshMakefilesJob.schedule();
		}
		
		
		
//		if (!refreshing) {
//			refreshing = true;
//			Display.getDefault().asyncExec(new Runnable() {
//				@Override
//				public void run() {
//					initMakefiles();
//					//String[] items = allmakefiles.toArray(new String[0]);
//					//combo.setItems(items);
//					//getParent().layout(true);
//					updateMakefileCombo();
//					refreshing = false;
//				}
//			});
//		}
	}

	public void setSelectedMakefile(String makefile) {
		this.selectedMakefile = makefile;
	}

	public String getSelectedMakefile() {
		return selectedMakefile;
	}

	private void updateMakefileCombo() {
		if (combo == null || combo.isDisposed()) {
			return;
		}
		List<String> options = populateOptions();
		int preDex = -1;
		List<String> makefiles = new ArrayList<String>();
		makefiles.add(JAXBCoreConstants.ZEROSTR);
		int i = 1;
		for (String name : allmakefiles) {
			int optionTypes = 0;
			for (String option : options) {
				if (name.contains(option)) {
					optionTypes++;
				}
			}

			if (optionTypes == options.size()) {
				makefiles.add(name);
				if (selectedMakefile != null && selectedMakefile.endsWith(name)) {
					preDex = i;
				}
				i++;
			}
		}

		String[] items = makefiles.toArray(new String[0]);
		combo.setItems(items);
		combo.setEnabled(true);
		reload.setEnabled(true);
		if (items.length > 1) {
			if (preDex > 0) {
				combo.select(preDex);
			} else {
				combo.select(1);
			}
			//if(!refreshing)
			//{
				combo.notifyListeners(SWT.Selection, null);
			//}
		}
		getParent().layout(true);
	}

	private List<String> populateOptions() {
		List<String> options = new ArrayList<String>();

		addOption(MPI_OPTION, options);
		addOption(CALLPATH_OPTION, options);
		addOption(OPARI_OPTION, options);
		addOption(OpenMP_OPTION, options);
		addOption(PAPI_OPTION, options);
		addOption(TRACE_OPTION, options);
		addOption(PDT_OPTION, options);

		return options;
	}

	private void addOption(String variable, List<String> options) {
		if (map != null) {
			// Find the selected options and add them so we can filter the makefile selection
			if (map.get(variable) != null) {
				String option = map.get(variable).getValue().toString();
				if (option.equals(TRUE_SELECTION)) {
					options.add(translateBoolean.get(variable));
				}
			}
		}
	}


	IFileStore bindir=null;
	List<IFileStore> mfiles=null;
	boolean noMakeFilesFound=false;
	/**
	 * The location of the tau install
	 */
	String tauLoc=null;
	
	/**
	 * Collects the list of TAU makefiles available at the specified TAU installation (asking the user to specify one if necessary)
	 * Adds the list of all available makefiles to allmakefiles and all available makefiles options to allopts
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void initMakefiles() {
		
		Object testMakefiles = null;

		if(map!=null){
			testMakefiles=map.getValue(TAU_MAKEFILE_LIST);
			tauLibDir=(String) map.getValue(TAU_LIB_DIR);
			tauLoc=(String) map.getValue(TAU_LOCATION);//TODO: Use this instead of checking for the other stuff.
		}
		
		if(testMakefiles==null||tauLibDir==null||tauLibDir.length()==0||tauLoc==null||tauLoc.length()==0){
			reloadMakefiles=true;
		}
		
		
		if(!reloadMakefiles){
		
		//if(testMakefiles!=null)//&&testMakefiles instanceof Set<?>)
		//{
			allmakefiles=(Set<String>)testMakefiles;
			allopts = new LinkedHashSet<String>();
			for(String name:allmakefiles){
				allopts.addAll(Arrays.asList(name.split(JAXBCoreConstants.HYPH)));
			}
			
		//}
		
		}
		else
		{	

			retrieveTauBinDir();
			
			retrieveTauMakefiles();
		}
		
		reloadMakefiles=false;
		
	}
	
	
	private void retrieveTauBinDir(){

			if(tauLoc==null||tauLoc.length()==0)
			{
				String binpath = blt.getToolPath(TAU);
				bindir = null;
				if (binpath == null || binpath.length() == 0) {
					binpath = blt.checkToolEnvPath(PPROF);
					if (binpath != null && binpath.length() > 0) {
						tauLoc=binpath;
						if(map!=null)
						{
							map.putValue(TAU_LOCATION, tauLoc);
						}
						bindir = blt.getFile(binpath);
					}
				} else {
					bindir = blt.getFile(binpath);
				}
				
			}
			else{
				bindir=blt.getFile(tauLoc);
			}
		
	}
	
	
	private void retrieveTauMakefiles(){
		allmakefiles = new LinkedHashSet<String>();
		//If the makefile list is not initialized and we haven't failed to initialize it already
		//if(mfiles==null&&!noMakeFilesFound)
		//{
			mfiles = testTAUEnv(bindir);
		//}

		if (mfiles == null) {
			noMakeFilesFound=true;
			return;
		}
		String name = null;
		allopts = new LinkedHashSet<String>();
		for (int i = 0; i < mfiles.size(); i++) {
			name = mfiles.get(i).getName();
			allmakefiles.add(name);
			allopts.addAll(Arrays.asList(name.split(JAXBCoreConstants.HYPH)));
		}
		map.putValue(TAU_MAKEFILE_LIST, allmakefiles);
		map.putValue(TAU_LIB_DIR, tauLibDir);
		allopts.remove(ITauConstants.TAU_MAKEFILE_PREFIX);
	}

	/**
	 * Given a directory (presumably a tau arch directory) this looks in the lib subdirectory and returns a list of all
	 * Makefile.tau... files with -pdt
	 * */
	private List<IFileStore> testTAUEnv(IFileStore bindir) {
		if (bindir == null || !bindir.fetchInfo().exists()) {
			return null;
		}

		IFileStore taulib = bindir.getParent().getChild(LIB_FOLDER);
		IFileStore[] mfiles = null;
		ArrayList<IFileStore> tmfiles = null;
		if (taulib.fetchInfo().exists()) {
			try {
				mfiles = taulib.childStores(EFS.NONE, null);
				tauLibDir = taulib.toURI().getPath();
				tmfiles = new ArrayList<IFileStore>();
				for (IFileStore mfile : mfiles) {
					IFileInfo finf = mfile.fetchInfo();
					if (!finf.isDirectory() && finf.getName().startsWith(ITauConstants.TAU_MAKEFILE_PREFIX)) {
						tmfiles.add(mfile);
					}
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}

		}

		return tmfiles;
	}

	public Combo getCombo() {
		return this.combo;
	}

	public String getSelection() {
		String selection = JAXBCoreConstants.ZEROSTR;

		String makefilePath = JAXBCoreConstants.ZEROSTR;
		if (tauLibDir != null && combo.getSelectionIndex() != -1) {
			selection = this.combo.getItem(combo.getSelectionIndex());
			makefilePath = tauLibDir + JAXBCoreConstants.REMOTE_PATH_SEP + selection;
		}
		return makefilePath;
	}

	public void setConfiguration(ILaunchConfiguration configuration) {
		if (blt != null && blt.getConfig() == null) {
			blt.setConfig(configuration);
			if (!refreshing) {
				refreshMakefilesJob.setUser(true);
				refreshMakefilesJob.schedule();
			}
		}
		
	}

	public void setVariableMap(IVariableMap map) {
		this.map = map;
		if(tauLoc!=null){
			map.putValue(TAU_LOCATION, tauLoc);
		}
	}
}

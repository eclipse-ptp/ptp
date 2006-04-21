package org.eclipse.ptp.pldt.openmp.core.prefs;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ptp.pldt.openmp.core.OpenMPPlugin;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Preference page based on FieldEditorPreferencePage
 * 
 */

public class OpenMPPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{
    private static final String INCLUDES_PREFERENCE_LABEL  = "OpenMP include paths:";
    private static final String INCLUDES_PREFERENCE_BROWSE = "Please choose a directory:";

    public OpenMPPreferencePage()
    {
        super(GRID);
        initPreferenceStore();
    }

    public OpenMPPreferencePage(int style)
    {
        super(style);
        initPreferenceStore();
    }

    public OpenMPPreferencePage(String title, ImageDescriptor image, int style)
    {
        super(title, image, style);
        initPreferenceStore();
    }

    public OpenMPPreferencePage(String title, int style)
    {
        super(title, style);
        initPreferenceStore();
    }

    /**
     * Init preference store and set the preference store for the preference page
     */
    private void initPreferenceStore()
    {
        IPreferenceStore store = OpenMPPlugin.getDefault().getPreferenceStore();
        setPreferenceStore(store);
    }

    public void init(IWorkbench workbench)
    {
    }

    protected void createFieldEditors()
    {
        PathEditor pathEditor = new PathEditor(OpenMPPlugin.OPEN_MP_INCLUDES, INCLUDES_PREFERENCE_LABEL,
                INCLUDES_PREFERENCE_BROWSE, getFieldEditorParent());
        addField(pathEditor);
    }
}

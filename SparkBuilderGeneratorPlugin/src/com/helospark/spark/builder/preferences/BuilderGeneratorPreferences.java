package com.helospark.spark.builder.preferences;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.helospark.spark.builder.Activator;

/**
 * Preferences page for this plugin.
 *
 * @author helospark
 */
public class BuilderGeneratorPreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    @Override
    public void init(IWorkbench arg0) {
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }

    @Override
    protected void createFieldEditors() {
        for (PluginPreference<?> pluginPreference : PluginPreferenceList.getAllPreferences()) {
            FieldEditor fieldEditor = pluginPreference.createFieldEditor(getFieldEditorParent());
            addField(fieldEditor);
        }
    }
}

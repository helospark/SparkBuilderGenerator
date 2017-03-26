package com.helospark.spark.builder.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
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
        boolean isFirstCategory = true;
        for (PluginPreferenceGroup pluginPreferenceGroup : PluginPreferenceList.getAllPreferences()) {
            addGroupSeparator(pluginPreferenceGroup.getName(), isFirstCategory);
            pluginPreferenceGroup.getPreferences()
                    .stream()
                    .map(pluginPreference -> pluginPreference.createFieldEditor(getFieldEditorParent()))
                    .forEach(fieldEditor -> addField(fieldEditor));
            isFirstCategory = false;
        }
    }

    private void addGroupSeparator(String name, boolean isFirstCategory) {
        Composite parent = getFieldEditorParent();
        if (!isFirstCategory) {
            createTopVerticalMargin(parent);
        }
        createLabel(name, parent);
        createSeparatorLine(parent);
        setGridLayoutToParent(parent);
    }

    private void createTopVerticalMargin(Composite parent) {
        Label emptyLineLabel = new Label(parent, SWT.NONE);
        emptyLineLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1));
    }

    private void createLabel(String name, Composite parent) {
        Label categoryTextLabel = new Label(parent, SWT.NONE);
        Font boldFont = getBoldFontFor(categoryTextLabel);
        categoryTextLabel.setFont(boldFont);
        categoryTextLabel.setText(name);
        categoryTextLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1));
    }

    private Font getBoldFontFor(Label categoryTextLabel) {
        FontDescriptor boldDescriptor = FontDescriptor.createFrom(categoryTextLabel.getFont()).setStyle(SWT.BOLD);
        Font boldFont = boldDescriptor.createFont(categoryTextLabel.getDisplay());
        return boldFont;
    }

    private void createSeparatorLine(Composite parent) {
        Label label = new Label(parent, SWT.SEPARATOR | SWT.SHADOW_OUT | SWT.HORIZONTAL);
        label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1));
    }

    private void setGridLayoutToParent(Composite parent) {
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.horizontalSpacing = 10;
        parent.setLayout(layout);
    }

}

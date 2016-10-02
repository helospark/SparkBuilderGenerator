package com.helospark.spark.converter.handlers.dialog.uicomponent;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionDialog;

public class PackageSelector {
    private JavaProjectSelectorStrategy javaProjectSelectorStrategy;
    private String packageFragment;
    private String label;
    private Composite container;
    private Shell parent;

    public PackageSelector(JavaProjectSelectorStrategy javaProjectSelectorStrategy, String label, Composite container, Shell parent) {
        this.javaProjectSelectorStrategy = javaProjectSelectorStrategy;
        this.label = label;
        this.container = container;
        this.parent = parent;
        show();
    }

    public Text show() {
        Label lbtFirstName = new Label(container, SWT.NONE);
        lbtFirstName.setText(label);

        GridData dataFirstName = new GridData();
        dataFirstName.grabExcessHorizontalSpace = true;
        dataFirstName.horizontalAlignment = GridData.FILL;
        Text inputField = new Text(container, SWT.BORDER);
        inputField.setLayoutData(dataFirstName);
        Button button = new Button(container, SWT.NONE);
        button.setText("Browse");
        button.addListener(SWT.Selection, data -> openPackageSelectDialog(parent, inputField));

        return inputField;
    }

    private void openPackageSelectDialog(Shell parent, Text inputField) {
        try {
            IJavaProject selectedProject = javaProjectSelectorStrategy.selectProject();
            if (selectedProject == null) {
                MessageDialog.openInformation(parent, "Information", "Please select the domain objects first");
            } else {
                SelectionDialog createPackageDialog = JavaUI.createPackageDialog(parent, selectedProject, SWT.NONE, "*");
                createPackageDialog.open();
                Object[] result = createPackageDialog.getResult();
                for (Object o : result) {
                    if (o instanceof PackageFragment) {
                        packageFragment = ((PackageFragment) o).getElementName();
                        inputField.setText(packageFragment);
                    }
                }
            }
        } catch (JavaModelException e) {
            e.printStackTrace();
        }
    }

    public String getPackageFragment() {
        return packageFragment;
    }

}

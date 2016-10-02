package com.helospark.spark.converter.handlers.dialog.uicomponent;

import java.util.Optional;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.core.JavaElement;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionDialog;

public class ClassSelector {
    private String label;
    private IJavaProject javaProject;
    private Text valueInput;
    private Composite container;
    private Shell shell;
    private ICompilationUnit defaultValue;
    private ICompilationUnit resultCompilationUnit;

    public ClassSelector(String label, Composite container, Shell shell, ICompilationUnit defaultValue) {
        this.label = label;
        this.container = container;
        this.shell = shell;
        this.defaultValue = defaultValue;
        resultCompilationUnit = defaultValue;
        show();
    }

    public String getName(ICompilationUnit icompilationUnit) {
        Optional<String> result = Optional.empty();
        try {
            IType[] types = icompilationUnit.getTypes();
            if (types != null && types.length > 0) {
                IType type = types[0];
                result = Optional.ofNullable(type.getFullyQualifiedName());
            }
        } catch (JavaModelException e) {
            System.out.println("Set input field failed. Silently ignored");
        }
        return result.orElse("");
    }

    private void show() {
        Label lbtFirstName = new Label(container, SWT.NONE);
        lbtFirstName.setText(label);

        GridData dataFirstName = new GridData();
        dataFirstName.grabExcessHorizontalSpace = true;
        dataFirstName.horizontalAlignment = GridData.FILL;

        Text inputField = new Text(container, SWT.BORDER);
        inputField.setLayoutData(dataFirstName);
        inputField.setEnabled(false);

        if (defaultValue != null) {
            inputField.setText(getName(defaultValue));
            javaProject = defaultValue.getJavaProject();

        }

        Button button = new Button(container, SWT.NONE);
        button.setText("Browse");
        button.addListener(SWT.Selection, data -> openTypeSelectionDialog(shell, inputField));
    }

    private void openTypeSelectionDialog(Shell parent, Text resultField) {
        SelectionDialog dialog;
        try {
            dialog = JavaUI.createTypeDialog(
                    parent, new ProgressMonitorDialog(parent),
                    SearchEngine.createWorkspaceScope(),
                    IJavaElementSearchConstants.CONSIDER_ALL_TYPES, false);

            dialog.setTitle("Find type");
            if (dialog.open() == IDialogConstants.OK_ID) {
                Object[] result = dialog.getResult();
                for (Object o : result) {
                    if (o instanceof JavaElement) {
                        JavaElement sourceType = (JavaElement) o;
                        javaProject = sourceType.getJavaProject();
                        resultCompilationUnit = sourceType.getCompilationUnit();
                        resultField.setText(resultCompilationUnit.getTypes()[0].getFullyQualifiedName());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public IJavaProject getJavaProject() {
        return javaProject;
    }

    public String getLabel() {
        return label;
    }

    public Text getValueInput() {
        return valueInput;
    }

    public ICompilationUnit getCompilationUnit() {
        return resultCompilationUnit;
    }

}

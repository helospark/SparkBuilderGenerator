package com.helospark.spark.converter.handlers.dialog;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.helospark.spark.converter.handlers.dialog.uicomponent.ClassSelector;
import com.helospark.spark.converter.handlers.dialog.uicomponent.JavaProjectSelectorStrategy;
import com.helospark.spark.converter.handlers.dialog.uicomponent.PackageSelector;

public class GenerateConverterDialog extends TitleAreaDialog {
    private PackageSelector packageSelector;

    private Button sourceProject;
    private Button destinationProject;

    private ICompilationUnit sourceDefaultValue;
    private ICompilationUnit destinationDefaultValue;

    private ClassSelector source;
    private ClassSelector destination;

    private Button recursiveGeneration;

    private Boolean recursiveGenerationValue;

    private IJavaProject destinationJavaProject;
    private boolean isSourceProjectSelectedAsDestination;

    public GenerateConverterDialog(Shell parentShell) {
        super(parentShell);
    }

    @Override
    public void create() {
        super.create();
        setTitle("Generate converters...");
        setMessage("More options under preferences -> Java -> Spark converter generator.", IMessageProvider.INFORMATION);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout layout = new GridLayout(3, false);
        container.setLayout(layout);

        source = new ClassSelector("Convert from", container, getShell(), sourceDefaultValue);
        destination = new ClassSelector("Convert to", container, getShell(), destinationDefaultValue);

        createProjectSelectRadio(container);

        JavaProjectSelectorStrategy strategy = new JavaProjectSelectorStrategy(source, destination, sourceProject, destinationProject);
        packageSelector = new PackageSelector(strategy, "Select destination pacakge", container, getShell());

        recursiveGeneration = new Button(container, SWT.CHECK);
        recursiveGeneration.setText("Recursively create converters");
        recursiveGeneration.addListener(SWT.Selection, data -> System.out.println("Clicked"));

        return area;
    }

    private void createProjectSelectRadio(Composite container) {
        Label lbtFirstName = new Label(container, SWT.NONE);
        lbtFirstName.setText("Destination project");

        sourceProject = new Button(container, SWT.RADIO);
        sourceProject.setText("Source's project");
        sourceProject.setSelection(true);
        sourceProject.addListener(SWT.Selection, data -> isSourceProjectSelectedAsDestination = true);

        destinationProject = new Button(container, SWT.RADIO);
        destinationProject.setText("Destination's project");
        sourceProject.addListener(SWT.Selection, data -> isSourceProjectSelectedAsDestination = false);
    }

    @Override
    protected boolean isResizable() {
        return true;
    }

    @Override
    protected void okPressed() {
        backupValues();
        super.okPressed();
    }

    private void backupValues() {
        recursiveGenerationValue = recursiveGeneration.getSelection();
    }

    @Override
    public boolean isHelpAvailable() {
        return false;
    }

    public void setSourceDefaultValue(ICompilationUnit icompilationUnit) {
        this.sourceDefaultValue = icompilationUnit;
    }

    public void setDestinationDefaultValue(ICompilationUnit icompilationUnit) {
        this.destinationDefaultValue = icompilationUnit;
    }

    public ICompilationUnit getDestinationCompilationUnit() {
        return destination.getCompilationUnit();
    }

    public ICompilationUnit getSourceCompilationUnit() {
        return source.getCompilationUnit();
    }

    public String getDestinationPackageFragment() {
        return packageSelector.getPackageFragment();
    }

    public Boolean getRecursiveGeneration() {
        return recursiveGenerationValue;
    }

    public IJavaProject getJavaProject() {
        return isSourceProjectSelectedAsDestination ? source.getJavaProject() : destination.getJavaProject();
    }
}

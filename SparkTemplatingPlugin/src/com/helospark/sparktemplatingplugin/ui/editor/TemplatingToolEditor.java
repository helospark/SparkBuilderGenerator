package com.helospark.sparktemplatingplugin.ui.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.internal.ui.text.JavaColorManager;
import org.eclipse.jdt.ui.text.IColorManager;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;

import com.helospark.sparktemplatingplugin.DiContainer;
import com.helospark.sparktemplatingplugin.repository.ScriptRepository;

public class TemplatingToolEditor extends TextEditor {
    private ScriptRepository scriptRepository;
    private IColorManager colorManager;
    boolean commandNameModified = false;

    public TemplatingToolEditor() {
        colorManager = new JavaColorManager();
        scriptRepository = DiContainer.getDependency(ScriptRepository.class);
        setSourceViewerConfiguration(new TemplatingToolEditorConfiguration(colorManager));
        setDocumentProvider(new TemplatingToolDocumentProvider());
    }

    @Override
    public void dispose() {
        colorManager.dispose();
        super.dispose();
    }

    @Override
    public void doSave(IProgressMonitor progressMonitor) {
        super.doSave(progressMonitor);
        // String scriptBody =
        // getDocumentProvider().getDocument(getEditorInput()).get();
        // progressMonitor.beginTask("Saving script", 1);
        // scriptRepository.saveNewScript(new ScriptEntity(commandName,
        // scriptBody));
        setDirty(false);
        // progressMonitor.worked(1);
        // progressMonitor.done();
    }

    @Override
    public void doSaveAs() {
        doSave(getProgressMonitor());
    }

    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        super.init(site, input);
    }

    // @Override
    // public void createPartControl(Composite parent) {
    // FillLayout layout = new FillLayout();
    // Composite child = new Composite(parent, SWT.NONE);
    // child.setLayout(layout);
    //
    // Composite grid = new Composite(child, SWT.NONE);
    // GridLayout gl = new GridLayout();
    // gl.numColumns = 2;
    // gl.marginBottom = 0;
    // gl.marginTop = 0;
    // gl.marginLeft = 0;
    // gl.marginRight = 0;
    // gl.marginWidth = 0;
    // gl.marginHeight = 0;
    // grid.setLayout(gl);
    // grid.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    //
    // Composite left = new Composite(grid, SWT.NONE);
    // left.setLayout(new FillLayout());
    // left.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    // Composite right = new Composite(grid, SWT.NONE);
    // right.setLayout(new FillLayout());
    // right.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
    //
    // Composite editor = new Composite(left, SWT.NONE);
    // editor.setLayout(new FillLayout());
    //
    // super.createPartControl(editor);
    //
    // Composite rightPanel = new Composite(right, SWT.NONE);
    // rightPanel.setLayout(new RowLayout(SWT.VERTICAL));
    //
    // Label mainLabel = new Label(rightPanel, SWT.NONE);
    // mainLabel.setText("EDITOR");
    // mainLabel.setFont(new Font(Display.getCurrent(), "Arial", 16, 0));
    //
    // new Label(rightPanel, SWT.NONE).setText("Command name:");
    // commandNameText = new Text(rightPanel, SWT.BORDER);
    // commandNameText.setMessage("Command name");
    // commandNameText.addModifyListener(ads -> setDirty(true));
    //
    // setPartName("tabName");
    // firePropertyChange(IWorkbenchPartConstants.PROP_PART_NAME);
    // }

    public void setDirty(boolean dirty) {
        if (commandNameModified != dirty) {
            commandNameModified = dirty;
            firePropertyChange(IEditorPart.PROP_DIRTY);
            firePropertyChange(IEditorPart.PROP_TITLE);
        }
    }

    @Override
    public String getTitleToolTip() {
        return "title";
    }

    @Override
    public boolean isDirty() {
        return super.isDirty() || commandNameModified;
    }

    @Override
    public String getTitle() {
        return "ASD";
    }

}

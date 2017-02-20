package com.helospark.sparktemplatingplugin.ui.dialog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;

import com.helospark.sparktemplatingplugin.Activator;
import com.helospark.sparktemplatingplugin.repository.ScriptRepository;
import com.helospark.sparktemplatingplugin.repository.domain.ScriptEntity;

public class TemplateBrowseDialog extends FilteredItemsSelectionDialog {
    private ScriptRepository scriptRepository;
    private static final String DIALOG_SETTINGS = "FilteredResourcesSelectionDialogExampleSettings";
    private static List<ScriptEntity> resources = new ArrayList<>();
    private Text extendedAreaText;
    private String message;

    public TemplateBrowseDialog(Shell shell, ScriptRepository scriptRepository, String message) {
        super(shell);
        this.scriptRepository = scriptRepository;
        setInitialPattern("?");
        this.message = message;
        super.setListLabelProvider(new TemplateBrowseDialogListLabelProvider());
    }

    @Override
    protected void handleSelected(StructuredSelection selection) {
        super.handleSelected(selection);
        Object object = selection.getFirstElement();
        if (object instanceof ScriptEntity) {
            extendedAreaText.setText(((ScriptEntity) object).getScript());
        } else {
            extendedAreaText.setText("#ERROR#");
        }
    }

    @Override
    protected Control createExtendedContentArea(Composite parent) {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;

        GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gridData.heightHint = 100;

        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(gridLayout);
        composite.setLayoutData(gridData);

        GridData textGridData = new GridData(SWT.FILL, SWT.FILL, true, true);

        extendedAreaText = new Text(composite, SWT.READ_ONLY | SWT.V_SCROLL);
        extendedAreaText.setLayoutData(textGridData);
        extendedAreaText.setText(
                "some text\n some more\n more and more \n and mre\nsome text\n some more\n more and more \n and mre\nsome text\n some more\n more and more \n and mre\nsome text\n some more\n more and more \n and mre\nsome text\n some more\n more and more \n and mre\n");
        return composite;
    }

    @Override
    protected IDialogSettings getDialogSettings() {
        IDialogSettings settings = Activator.getDefault().getDialogSettings()
                .getSection(DIALOG_SETTINGS);
        if (settings == null) {
            settings = Activator.getDefault().getDialogSettings()
                    .addNewSection(DIALOG_SETTINGS);
        }
        settings.put("ShowStatusLine", false);
        return settings;
    }

    @Override
    protected IStatus validateItem(Object item) {
        return Status.OK_STATUS;
    }

    @Override
    protected ItemsFilter createFilter() {
        return new ItemsFilter() {
            @Override
            public boolean matchItem(Object item) {
                return matches(item.toString());
            }

            @Override
            public boolean isConsistentItem(Object item) {
                return true;
            }
        };
    }

    @Override
    protected Comparator getItemsComparator() {
        return new Comparator() {
            @Override
            public int compare(Object arg0, Object arg1) {
                return arg0.toString().compareTo(arg1.toString());
            }
        };
    }

    @Override
    protected void fillContentProvider(AbstractContentProvider contentProvider, ItemsFilter itemsFilter, IProgressMonitor progressMonitor) throws CoreException {
        progressMonitor.beginTask("Filling contents", 1);
        resources = scriptRepository.loadAll();
        resources.stream()
                .forEach(resource -> contentProvider.add(resource, itemsFilter));
        progressMonitor.worked(1);
        progressMonitor.done();

    }

    @Override
    public String getElementName(Object item) {
        return item.toString();
    }

    @Override
    protected String getMessage() {
        return message + " (? = any character, * = any string)";
    }

}

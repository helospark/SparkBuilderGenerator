package com.helospark.spark.builder.dialogs;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Generated;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.helospark.spark.builder.dialogs.domain.RegularBuilderDialogData;
import com.helospark.spark.builder.dialogs.domain.RegularBuilderFieldIncludeFieldIncludeDomain;

/**
 * Dialog to select user preferences during builder generation for regular builders.
 * @author helospark
 */
@Generated("WindowBuilder")
public class RegularBuilderUserPreferenceDialog extends Dialog {

    protected Object result;
    protected Shell shell;
    private Table table;
    private CheckboxTableViewer checkboxTableViewer;
    private Button copyBuilderMethodGenerateButton;
    private Button addJacksonDeserializer;
    private Button createDefaultConstructor;
    private RegularBuilderDialogData dialogData;

    /**
     * Create the dialog.
     * @param parent
     * @param style
     */
    public RegularBuilderUserPreferenceDialog(Shell parent, RegularBuilderDialogData dialogData) {
        super(parent);
        setText("Select fields for builder generation");
        this.dialogData = dialogData;
    }

    /**
     * Open the dialog.
     * @return the result
     */
    public Object open() {
        createContents();
        shell.open();
        shell.layout();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return result;
    }

    /**
     * Create contents of the dialog.
     */
    private void createContents() {
        shell = new Shell(getParent(), SWT.SHELL_TRIM | SWT.BORDER | SWT.PRIMARY_MODAL | SWT.SHEET);
        shell.setSize(418, 444);
        shell.setText("Select fields for builder");

        Label lblNewLabel = new Label(shell, SWT.WRAP);
        lblNewLabel.setBounds(10, 10, 396, 54);
        lblNewLabel.setText("Uncheck fields you do not wish to include in the builder");

        checkboxTableViewer = CheckboxTableViewer.newCheckList(shell, SWT.BORDER | SWT.FULL_SELECTION);
        table = checkboxTableViewer.getTable();
        table.setBounds(10, 33, 396, 228);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        checkboxTableViewer.setCheckStateProvider(new ICheckStateProvider() {

            @Override
            public boolean isGrayed(Object object) {
                return false;
            }

            @Override
            public boolean isChecked(Object object) {
                return ((RegularBuilderFieldIncludeFieldIncludeDomain) object).isIncludeField();
            }
        });
        checkboxTableViewer.addCheckStateListener(new ICheckStateListener() {

            @Override
            public void checkStateChanged(CheckStateChangedEvent checkStateChangedEvent) {
                RegularBuilderFieldIncludeFieldIncludeDomain domain = (RegularBuilderFieldIncludeFieldIncludeDomain) checkStateChangedEvent
                        .getElement();
                domain.setIncludeField(checkStateChangedEvent.getChecked());
            }
        });

        TableViewerColumn tableViewerColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
        TableColumn mandatoryTableColumn = tableViewerColumn.getColumn();
        mandatoryTableColumn.setWidth(100);
        mandatoryTableColumn.setText("Include");
        tableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object o) {
                return "";
            }
        });

        TableViewerColumn fieldnameTableViewerColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
        TableColumn fieldNameTableColumn = fieldnameTableViewerColumn.getColumn();
        fieldNameTableColumn.setWidth(200);
        fieldNameTableColumn.setText("Field");
        fieldnameTableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object o) {
                return ((RegularBuilderFieldIncludeFieldIncludeDomain) o).getFieldName();
            }
        });
        Button generateButton = new Button(shell, SWT.NONE);
        generateButton.setBounds(305, 376, 101, 29);
        generateButton.setText("Generate");
        generateButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                result = getResult();

                shell.close();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent selectionEvent) {

            }
        });

        Button cancelButton = new Button(shell, SWT.NONE);
        cancelButton.setBounds(10, 376, 101, 29);
        cancelButton.setText("Cancel");

        copyBuilderMethodGenerateButton = new Button(shell, SWT.CHECK);
        copyBuilderMethodGenerateButton.setBounds(10, 277, 396, 22);
        copyBuilderMethodGenerateButton.setText("Add method to create a builder based on an instance");

        cancelButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                result = null;
                shell.close();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent selectionEvent) {

            }
        });

        addJacksonDeserializer = new Button(shell, SWT.CHECK);
        addJacksonDeserializer.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            }
        });
        addJacksonDeserializer.setBounds(10, 305, 384, 22);
        addJacksonDeserializer.setText("Add Jackson deserialize annotation");

        shell.setDefaultButton(generateButton);

        createDefaultConstructor = new Button(shell, SWT.CHECK);
        createDefaultConstructor.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            }
        });
        createDefaultConstructor.setBounds(10, 333, 373, 17);
        createDefaultConstructor.setText("Create public default constructor");
        initializeContents();
    }

    private void initializeContents() {
        dialogData.getRegularBuilderFieldIncludeFieldIncludeDomains()
                .stream()
                .forEach(checkboxTableViewer::add);
        copyBuilderMethodGenerateButton.setSelection(dialogData.isShouldCreateCopyMethod());
        addJacksonDeserializer.setSelection(dialogData.isAddJacksonDeserializeAnnotation());
        createDefaultConstructor.setSelection(dialogData.isCreateDefaultConstructor());
    }

    private RegularBuilderDialogData getResult() {
        return RegularBuilderDialogData.builder()
                .withRegularBuilderFieldIncludeFieldIncludeDomains(getSelectedFields())
                .withShouldCreateCopyMethod(copyBuilderMethodGenerateButton.getSelection())
                .withAddJacksonDeserializeAnnotation(addJacksonDeserializer.getSelection())
                .withCreateDefaultConstructor(createDefaultConstructor.getSelection())
                .build();
    }

    private List<RegularBuilderFieldIncludeFieldIncludeDomain> getSelectedFields() {
        return Arrays.stream(checkboxTableViewer.getTable().getItems())
                .map(item -> (RegularBuilderFieldIncludeFieldIncludeDomain) item.getData())
                .collect(Collectors.toList());
    }
}

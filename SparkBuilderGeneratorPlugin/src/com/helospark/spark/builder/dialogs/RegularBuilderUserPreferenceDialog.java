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
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.helospark.spark.builder.Activator;
import com.helospark.spark.builder.dialogs.domain.RegularBuilderDialogData;
import com.helospark.spark.builder.dialogs.domain.RegularBuilderFieldIncludeFieldIncludeDomain;

/**
 * Dialog to select user preferences during builder generation for regular builders.
 * @author helospark
 */
@Generated("WindowBuilder")
public class RegularBuilderUserPreferenceDialog extends Dialog {

    private static final int MANDATORY_COLUMN_WIDTH = 70;
    protected Object result;
    protected Shell shell;
    private Table table;
    private CheckboxTableViewer checkboxTableViewer;
    private Button copyBuilderMethodGenerateButton;
    private Button addJacksonDeserializer;
    private Button createDefaultConstructor;
    private Button createPublicConstructorWithMandatoryFields;
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
        shell.setSize(550, 400);
        shell.setMinimumSize(550, 500);
        shell.setText("Select fields for builder");
        shell.setLayout(new GridLayout(2, false));
        shell.setImage(Activator.getIcon());

        Label lblNewLabel = new Label(shell, SWT.WRAP);
        lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        lblNewLabel.setBounds(10, 10, 396, 54);
        lblNewLabel.setText("Uncheck fields you do not wish to include in the builder");

        checkboxTableViewer = CheckboxTableViewer.newCheckList(shell, SWT.BORDER | SWT.FULL_SELECTION);
        table = checkboxTableViewer.getTable();
        GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 2);
        gd_table.heightHint = 250;
        gd_table.widthHint = 365;
        table.setLayoutData(gd_table);
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
        TableColumn includeTableColumn = tableViewerColumn.getColumn();
        includeTableColumn.setWidth(70);
        includeTableColumn.setText("Include");
        includeTableColumn.setAlignment(SWT.CENTER);
        tableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object o) {
                return "";
            }
        });

        TableColumn mandatoryTableColumn = new TableColumn(checkboxTableViewer.getTable(), SWT.None);
        mandatoryTableColumn.setWidth(MANDATORY_COLUMN_WIDTH);
        mandatoryTableColumn.setText("Mandatory");
        TableViewerColumn mandatoryTableViewerColumn = new TableViewerColumn(checkboxTableViewer, mandatoryTableColumn);
        mandatoryTableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public void update(ViewerCell cell) {

                TableItem item = (TableItem) cell.getItem();
                Button button = new Button((Composite) cell.getViewerRow().getControl(), SWT.CHECK);
                RegularBuilderFieldIncludeFieldIncludeDomain element = (RegularBuilderFieldIncludeFieldIncludeDomain) cell.getElement();
                button.setSelection(element.isMandatory());
                button.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        element.setMandatory(button.getSelection());
                    }
                });
                TableEditor editor = new TableEditor(item.getParent());
                editor.grabHorizontal = true;
                editor.grabVertical = true;
                editor.setEditor(button, item, cell.getColumnIndex());
                editor.layout();
            }
        });
        setMandatoryColumnVisibility(mandatoryTableColumn, dialogData.isCreatePublicConstructorWithMandatoryFields());

        TableViewerColumn fieldnameTableViewerColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
        TableColumn fieldNameTableColumn = fieldnameTableViewerColumn.getColumn();
        fieldNameTableColumn.setWidth(291);
        fieldNameTableColumn.setText("Field");
        fieldnameTableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object o) {
                return ((RegularBuilderFieldIncludeFieldIncludeDomain) o).getFieldName();
            }
        });

        copyBuilderMethodGenerateButton = new Button(shell, SWT.CHECK);
        copyBuilderMethodGenerateButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        copyBuilderMethodGenerateButton.setBounds(10, 277, 396, 22);
        copyBuilderMethodGenerateButton.setText("Add method to create a builder based on an instance");

        addJacksonDeserializer = new Button(shell, SWT.CHECK);
        addJacksonDeserializer.setBounds(10, 305, 384, 22);
        addJacksonDeserializer.setText("Add Jackson deserialize annotation");
        new Label(shell, SWT.NONE);

        createDefaultConstructor = new Button(shell, SWT.CHECK);
        createDefaultConstructor.setBounds(10, 333, 373, 17);
        createDefaultConstructor.setText("Create public default constructor");
        new Label(shell, SWT.NONE);

        createPublicConstructorWithMandatoryFields = new Button(shell, SWT.CHECK);
        createPublicConstructorWithMandatoryFields.setBounds(10, 366, 450, 17);
        createPublicConstructorWithMandatoryFields.setText("Create public builder constructor with mandatory parameters");
        createPublicConstructorWithMandatoryFields.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setMandatoryColumnVisibility(mandatoryTableColumn, createPublicConstructorWithMandatoryFields.getSelection());
                checkboxTableViewer.getTable().redraw();
            }
        });
        new Label(shell, SWT.NONE);

        Button cancelButton = new Button(shell, SWT.NONE);
        cancelButton.setBounds(10, 450, 101, 29);
        cancelButton.setText("Cancel");

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
        Button generateButton = new Button(shell, SWT.NONE);
        generateButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        generateButton.setBounds(255, 450, 130, 29);
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

        shell.setDefaultButton(generateButton);
        initializeContents();
    }

    private void setMandatoryColumnVisibility(TableColumn mandatoryTableColumn, boolean visible) {
        // yes this is a hack, but it's the simplest way to hide a column :)
        if (visible) {
            mandatoryTableColumn.setWidth(MANDATORY_COLUMN_WIDTH);
        } else {
            mandatoryTableColumn.setWidth(0);
        }
    }

    private void initializeContents() {
        dialogData.getRegularBuilderFieldIncludeFieldIncludeDomains()
                .stream()
                .forEach(checkboxTableViewer::add);
        copyBuilderMethodGenerateButton.setSelection(dialogData.shouldCreateCopyMethod());
        addJacksonDeserializer.setSelection(dialogData.isAddJacksonDeserializeAnnotation());
        createDefaultConstructor.setSelection(dialogData.isCreateDefaultConstructor());
        createPublicConstructorWithMandatoryFields.setSelection(dialogData.isCreatePublicConstructorWithMandatoryFields());
    }

    private RegularBuilderDialogData getResult() {
        return RegularBuilderDialogData.builder()
                .withRegularBuilderFieldIncludeFieldIncludeDomains(getSelectedFields())
                .withShouldCreateCopyMethod(copyBuilderMethodGenerateButton.getSelection())
                .withAddJacksonDeserializeAnnotation(addJacksonDeserializer.getSelection())
                .withCreateDefaultConstructor(createDefaultConstructor.getSelection())
                .withCreatePublicConstructorWithMandatoryFields(createPublicConstructorWithMandatoryFields.getSelection())
                .build();
    }

    private List<RegularBuilderFieldIncludeFieldIncludeDomain> getSelectedFields() {
        return Arrays.stream(checkboxTableViewer.getTable().getItems())
                .map(item -> (RegularBuilderFieldIncludeFieldIncludeDomain) item.getData())
                .collect(Collectors.toList());
    }
}

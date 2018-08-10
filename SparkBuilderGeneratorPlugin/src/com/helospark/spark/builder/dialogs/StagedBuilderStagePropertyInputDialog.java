package com.helospark.spark.builder.dialogs;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.helospark.spark.builder.dialogs.domain.StagedBuilderStagePropertiesDialogResult;

/**
 * Dialog to set order and mandatory parameters for stage builder.
 * Generated with WindowBuilder plugin
 * @author helospark
 */
public class StagedBuilderStagePropertyInputDialog extends Dialog {
    protected Object dialogResult;
    protected Shell shell;
    private Table checkboxTable;
    private CheckboxTableViewer checkboxTableViewer;
    private List<StagedBuilderStagePropertiesDialogResult> stagedBuilderStagePropertiesDialogResult;

    public StagedBuilderStagePropertyInputDialog(Shell parent, List<StagedBuilderStagePropertiesDialogResult> stagedBuilderStagePropertiesDialogResult) {
        super(parent);
        setText("Staged builder generator - stage selection");
        this.stagedBuilderStagePropertiesDialogResult = stagedBuilderStagePropertiesDialogResult;
    }

    /**
     * Open the dialog.
     *
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
        return dialogResult;
    }

    /**
     * Create contents of the dialog.
     */
    private void createContents() {
        shell = new Shell(getParent(), SWT.SHELL_TRIM | SWT.BORDER | SWT.PRIMARY_MODAL | SWT.SHEET);
        shell.setSize(474, 338);
        shell.setMinimumSize(450, 250);
        shell.setText(getText());
        shell.setLayout(new GridLayout(2, false));

        Label usageLabel = new Label(shell, SWT.NONE);
        usageLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        usageLabel.setText("Check fields that are mandatory, organize field build stage order.");

        Label lblNewLabel = new Label(shell, SWT.NONE);
        lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        lblNewLabel.setText("Click on 'Remove' button to leave selected field out of the builder.");

        checkboxTableViewer = CheckboxTableViewer.newCheckList(shell, SWT.BORDER | SWT.FULL_SELECTION);
        checkboxTable = checkboxTableViewer.getTable();
        GridData gd_checkboxTable = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gd_checkboxTable.minimumWidth = 100;
        gd_checkboxTable.minimumHeight = 50;
        checkboxTable.setLayoutData(gd_checkboxTable);
        checkboxTable.setHeaderVisible(true);
        checkboxTable.setLinesVisible(true);
        checkboxTableViewer.setCheckStateProvider(new ICheckStateProvider() {

            @Override
            public boolean isGrayed(Object object) {
                return false;
            }

            @Override
            public boolean isChecked(Object object) {
                return ((StagedBuilderStagePropertiesDialogResult) object).isMandatory();
            }
        });
        checkboxTableViewer.addCheckStateListener(new ICheckStateListener() {

            @Override
            public void checkStateChanged(CheckStateChangedEvent checkStateChangedEvent) {
                StagedBuilderStagePropertiesDialogResult domain = (StagedBuilderStagePropertiesDialogResult) checkStateChangedEvent.getElement();
                domain.setMandatory(checkStateChangedEvent.getChecked());
            }
        });

        TableViewerColumn tableViewerColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
        TableColumn mandatoryTableColumn = tableViewerColumn.getColumn();
        mandatoryTableColumn.setWidth(100);
        mandatoryTableColumn.setText("Mandatory");
        tableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object o) {
                return "";
            }
        });

        TableViewerColumn fieldnameTableViewerColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
        TableColumn fieldNameTableColumn = fieldnameTableViewerColumn.getColumn();
        fieldNameTableColumn.setWidth(240);
        fieldNameTableColumn.setText("Field");
        fieldnameTableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object o) {
                return ((StagedBuilderStagePropertiesDialogResult) o).getFieldName();
            }
        });

        Composite composite = new Composite(shell, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
        composite.setLayout(new RowLayout(SWT.VERTICAL));

        Button moveUpButton = new Button(composite, SWT.NONE);
        moveUpButton.setText("Move up");

        Button moveDownButton = new Button(composite, SWT.NONE);
        moveDownButton.setText("Move down");

        Button removeButton = new Button(composite, SWT.NONE);
        removeButton.setText("Remove");
        removeButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent paramSelectionEvent) {
                Table table = checkboxTableViewer.getTable();
                int selectionIndex = table.getSelectionIndex();
                if (selectionIndex >= 0 && selectionIndex < table.getItemCount()) {
                    Object item = table.getItem(selectionIndex).getData();
                    checkboxTableViewer.remove(item);
                }

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent paramSelectionEvent) {
            }
        });

        moveUpButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                Table table = checkboxTableViewer.getTable();
                int selectionIndex = table.getSelectionIndex();
                int newIndex = selectionIndex - 1;
                if (newIndex >= 0) {
                    Object item = table.getItem(selectionIndex).getData();
                    checkboxTableViewer.remove(item);
                    checkboxTableViewer.insert(item, newIndex);
                    table.setSelection(newIndex);
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent selectionEvent) {

            }
        });

        moveDownButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                Table table = checkboxTableViewer.getTable();
                int selectionIndex = table.getSelectionIndex();
                int newIndex = selectionIndex + 1;
                if (newIndex < table.getItemCount()) {
                    Object item = table.getItem(selectionIndex).getData();
                    checkboxTableViewer.remove(item);
                    checkboxTableViewer.insert(item, newIndex);
                    table.setSelection(newIndex);
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent selectionEvent) {

            }
        });

        Button cancelButton = new Button(shell, SWT.NONE);
        cancelButton.setText("Cancel");

        cancelButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                dialogResult = null;
                shell.close();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent selectionEvent) {

            }
        });

        Button generateButton = new Button(shell, SWT.NONE);
        generateButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        generateButton.setAlignment(SWT.RIGHT);
        generateButton.setText("Generate");
        generateButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                dialogResult = Arrays.stream(checkboxTableViewer.getTable().getItems())
                        .map(item -> (StagedBuilderStagePropertiesDialogResult) item.getData())
                        .collect(Collectors.toList());
                shell.close();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent selectionEvent) {

            }
        });

        shell.setDefaultButton(generateButton);

        initializeContents();
    }

    private void initializeContents() {
        stagedBuilderStagePropertiesDialogResult.stream()
                .forEach(checkboxTableViewer::add);
    }
}

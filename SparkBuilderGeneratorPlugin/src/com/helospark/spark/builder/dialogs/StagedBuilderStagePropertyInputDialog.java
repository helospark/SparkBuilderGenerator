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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
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
@Generated("WindowBuilder")
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
        shell = new Shell(getParent(), SWT.SHELL_TRIM | SWT.BORDER | SWT.PRIMARY_MODAL);
        shell.setSize(474, 338);
        shell.setText(getText());

        Button moveUpButton = new Button(shell, SWT.NONE);
        moveUpButton.setBounds(358, 52, 101, 29);
        moveUpButton.setText("Move up");

        Button moveDownButton = new Button(shell, SWT.NONE);
        moveDownButton.setBounds(358, 87, 101, 29);
        moveDownButton.setText("Move down");

        Label usageLabel = new Label(shell, SWT.NONE);
        usageLabel.setBounds(10, 10, 449, 17);
        usageLabel.setText("Check fields that are mandatory, organize field build stage order.");

        checkboxTableViewer = CheckboxTableViewer.newCheckList(shell, SWT.BORDER | SWT.FULL_SELECTION);
        checkboxTable = checkboxTableViewer.getTable();
        checkboxTable.setBounds(10, 52, 342, 209);
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

        Button generateButton = new Button(shell, SWT.NONE);
        generateButton.setBounds(358, 270, 101, 29);
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

        Button cancelButton = new Button(shell, SWT.NONE);
        cancelButton.setBounds(10, 270, 101, 29);
        cancelButton.setText("Cancel");

        Button removeButton = new Button(shell, SWT.NONE);
        removeButton.setBounds(358, 122, 101, 29);
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

        Label lblNewLabel = new Label(shell, SWT.NONE);
        lblNewLabel.setBounds(10, 29, 459, 17);
        lblNewLabel.setText("Click on 'Remove' button to leave selected field out of the builder.");

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

        initializeContents();
    }

    private void initializeContents() {
        stagedBuilderStagePropertiesDialogResult.stream()
                .forEach(checkboxTableViewer::add);
    }
}

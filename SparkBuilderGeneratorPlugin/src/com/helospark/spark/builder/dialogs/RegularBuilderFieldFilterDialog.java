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

import com.helospark.spark.builder.dialogs.domain.RegularBuilderFieldIncludeFieldIncludeDomain;

@Generated("WindowBuilder")
public class RegularBuilderFieldFilterDialog extends Dialog {

    protected Object result;
    protected Shell shell;
    private Table table;
    private CheckboxTableViewer checkboxTableViewer;
    private List<RegularBuilderFieldIncludeFieldIncludeDomain> fieldIncludeDomains;

    /**
     * Create the dialog.
     * @param parent
     * @param style
     */
    public RegularBuilderFieldFilterDialog(Shell parent, List<RegularBuilderFieldIncludeFieldIncludeDomain> input) {
        super(parent);
        setText("Select fields for builder generation");
        this.fieldIncludeDomains = input;
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
        shell = new Shell(getParent(), SWT.SHELL_TRIM | SWT.BORDER | SWT.PRIMARY_MODAL);
        shell.setSize(338, 346);
        shell.setText("Select fields for builder");

        Label lblNewLabel = new Label(shell, SWT.WRAP);
        lblNewLabel.setBounds(10, 10, 315, 54);
        lblNewLabel.setText("Uncheck fields you do not wish to include in the builder");

        checkboxTableViewer = CheckboxTableViewer.newCheckList(shell, SWT.BORDER | SWT.FULL_SELECTION);
        table = checkboxTableViewer.getTable();
        table.setBounds(10, 47, 315, 214);
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
                RegularBuilderFieldIncludeFieldIncludeDomain domain = (RegularBuilderFieldIncludeFieldIncludeDomain) checkStateChangedEvent.getElement();
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
        generateButton.setBounds(224, 270, 101, 29);
        generateButton.setText("Generate");
        generateButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                result = Arrays.stream(checkboxTableViewer.getTable().getItems())
                        .map(item -> (RegularBuilderFieldIncludeFieldIncludeDomain) item.getData())
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

        initializeContents();

    }

    private void initializeContents() {
        fieldIncludeDomains.stream()
                .forEach(checkboxTableViewer::add);
    }
}

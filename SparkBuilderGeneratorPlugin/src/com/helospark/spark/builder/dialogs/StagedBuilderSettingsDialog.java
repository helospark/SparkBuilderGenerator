package com.helospark.spark.builder.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class StagedBuilderSettingsDialog extends Dialog {
	public StagedBuilderSettingsDialog(final Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite body = (Composite) super.createDialogArea(parent);

		final TableViewer viewer = new TableViewer(body, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

		viewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		viewer.add("Hello");
		viewer.add("world");

		return body;
	}
}
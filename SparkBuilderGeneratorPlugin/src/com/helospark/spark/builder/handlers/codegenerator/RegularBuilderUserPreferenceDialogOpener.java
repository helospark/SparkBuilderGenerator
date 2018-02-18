package com.helospark.spark.builder.handlers.codegenerator;

import java.util.Optional;

import org.eclipse.swt.widgets.Shell;

import com.helospark.spark.builder.dialogs.RegularBuilderUserPreferenceDialog;
import com.helospark.spark.builder.dialogs.domain.RegularBuilderDialogData;
import com.helospark.spark.builder.handlers.CurrentShellProvider;

/**
 * Opens the dialog that filter the fields to include in the builder.
 * @author helospark
 */
public class RegularBuilderUserPreferenceDialogOpener {
    private CurrentShellProvider currentShellProvider;

    public RegularBuilderUserPreferenceDialogOpener(CurrentShellProvider currentShellProvider) {
        this.currentShellProvider = currentShellProvider;
    }

    public Optional<RegularBuilderDialogData> open(RegularBuilderDialogData regularBuilderDialogData) {
        Shell shell = currentShellProvider.provideCurrentShell();
        RegularBuilderUserPreferenceDialog dialog = new RegularBuilderUserPreferenceDialog(shell, regularBuilderDialogData);
        RegularBuilderDialogData result = (RegularBuilderDialogData) dialog.open();
        return Optional.ofNullable(result);
    }

}

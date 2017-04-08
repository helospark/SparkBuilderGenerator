package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import static java.util.Optional.ofNullable;

import java.util.List;
import java.util.Optional;

import org.eclipse.swt.widgets.Shell;

import com.helospark.spark.builder.dialogs.StagedBuilderStagePropertyInputDialog;
import com.helospark.spark.builder.dialogs.domain.StagedBuilderStagePropertiesDialogResult;
import com.helospark.spark.builder.handlers.CurrentShellProvider;

/**
 * Opens a dialog to get stage properties.
 * @author helospark
 */
public class StagedBuilderStagePropertyInputDialogOpener {
    private CurrentShellProvider currentShellProvider;

    public StagedBuilderStagePropertyInputDialogOpener(CurrentShellProvider currentShellProvider) {
        this.currentShellProvider = currentShellProvider;
    }

    public Optional<List<StagedBuilderStagePropertiesDialogResult>> open(List<StagedBuilderStagePropertiesDialogResult> request) {
        Shell shell = currentShellProvider.provideCurrentShell();
        StagedBuilderStagePropertyInputDialog stagedBuilderStagePropertyInputDialog = new StagedBuilderStagePropertyInputDialog(shell, request);
        List<StagedBuilderStagePropertiesDialogResult> result = (List<StagedBuilderStagePropertiesDialogResult>) stagedBuilderStagePropertyInputDialog.open();
        return ofNullable(result);
    }
}

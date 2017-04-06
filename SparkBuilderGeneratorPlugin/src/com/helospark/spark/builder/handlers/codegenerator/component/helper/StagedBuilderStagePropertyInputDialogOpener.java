package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import java.util.List;

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

    public List<StagedBuilderStagePropertiesDialogResult> open(List<StagedBuilderStagePropertiesDialogResult> request) {
        Shell shell = currentShellProvider.provideCurrentShell();
        StagedBuilderStagePropertyInputDialog stagedBuilderStagePropertyInputDialog = new StagedBuilderStagePropertyInputDialog(shell, request);
        return (List<StagedBuilderStagePropertiesDialogResult>) stagedBuilderStagePropertyInputDialog.open();
    }
}

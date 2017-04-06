package com.helospark.spark.builder.handlers.codegenerator;

import java.util.List;
import java.util.Optional;

import org.eclipse.swt.widgets.Shell;

import com.helospark.spark.builder.dialogs.RegularBuilderFieldFilterDialog;
import com.helospark.spark.builder.dialogs.domain.RegularBuilderFieldIncludeFieldIncludeDomain;
import com.helospark.spark.builder.handlers.CurrentShellProvider;

public class RegularBuilderFieldFilterDialogOpener {
    private CurrentShellProvider currentShellProvider;

    public RegularBuilderFieldFilterDialogOpener(CurrentShellProvider currentShellProvider) {
        this.currentShellProvider = currentShellProvider;
    }

    public Optional<List<RegularBuilderFieldIncludeFieldIncludeDomain>> open(List<RegularBuilderFieldIncludeFieldIncludeDomain> request) {
        Shell shell = currentShellProvider.provideCurrentShell();
        RegularBuilderFieldFilterDialog regularBuilderStagePropertyInputDialog = new RegularBuilderFieldFilterDialog(shell, request);
        List<RegularBuilderFieldIncludeFieldIncludeDomain> result = (List<RegularBuilderFieldIncludeFieldIncludeDomain>) regularBuilderStagePropertyInputDialog.open();
        return Optional.ofNullable(result);
    }

}

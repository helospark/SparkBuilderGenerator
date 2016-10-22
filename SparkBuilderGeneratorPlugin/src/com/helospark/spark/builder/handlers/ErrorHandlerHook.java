package com.helospark.spark.builder.handlers;

import com.helospark.spark.builder.handlers.exception.PluginException;

public class ErrorHandlerHook {
    private static final String ERROR_TITLE = "Error";
    private static final String UNEXPECTED_ERROR_MESSAGE = "This error should not have happened!\n" +
            "You can create an issue on https://github.com/helospark/SparkTools with the below stacktrace";

    private DialogWrapper dialogWrapper;

    public ErrorHandlerHook(DialogWrapper dialogWrapper) {
        this.dialogWrapper = dialogWrapper;
    }

    public void onPreviousBuilderRemoveFailure(Exception e) {
        dialogWrapper.openInformationDialog(ERROR_TITLE, "Error removing previous builder, skipping");
    }

    public void onPluginException(PluginException e) {
        dialogWrapper.openInformationDialog(ERROR_TITLE, e.getMessage());
    }

    public void onUnexpectedException(Exception e) {
        dialogWrapper.openErrorDialogWithStacktrace(ERROR_TITLE,
                UNEXPECTED_ERROR_MESSAGE, e);
    }
}

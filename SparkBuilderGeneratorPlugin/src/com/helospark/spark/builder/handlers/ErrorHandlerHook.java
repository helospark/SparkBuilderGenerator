package com.helospark.spark.builder.handlers;

import com.helospark.spark.builder.PluginLogger;
import com.helospark.spark.builder.handlers.exception.PluginException;

public class ErrorHandlerHook {
    private static final String ERROR_TITLE = "Error";
    private static final String UNEXPECTED_ERROR_MESSAGE = "This error should not have happened!\n" +
            "You can create an issue on https://github.com/helospark/SparkTools with the below stacktrace";

    private DialogWrapper dialogWrapper;
    private PluginLogger pluginLogger;

    public ErrorHandlerHook(DialogWrapper dialogWrapper) {
        this.dialogWrapper = dialogWrapper;
        this.pluginLogger = new PluginLogger();
    }

    public void onPreviousBuilderRemoveFailure(Exception e) {
        dialogWrapper.openInformationDialog(ERROR_TITLE, "Error removing previous builder, skipping");
        pluginLogger.warn("Error removing previous builder", e);
    }

    public void onPluginException(PluginException e) {
        dialogWrapper.openInformationDialog(ERROR_TITLE, e.getMessage());
        pluginLogger.warn(e.getMessage(), e);
    }

    public void onUnexpectedException(Exception e) {
        dialogWrapper.openErrorDialogWithStacktrace(ERROR_TITLE,
                UNEXPECTED_ERROR_MESSAGE, e);
        pluginLogger.error(UNEXPECTED_ERROR_MESSAGE, e);
    }

    public void onCalledWithoutActiveJavaFile() {
        dialogWrapper.openInformationDialog("No active Java editor",
                "To generate builder execute this command in an active Java editor");
    }
}

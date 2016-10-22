package com.helospark.spark.builder.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

public class HandlerUtilWrapper {

    public IEditorPart getActiveEditor(ExecutionEvent event) {
        return HandlerUtil.getActiveEditor(event);
    }

    public String getActivePartId(ExecutionEvent event) {
        return HandlerUtil.getActivePartId(event);
    }
}

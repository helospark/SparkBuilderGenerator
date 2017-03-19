package com.helospark.spark.builder.handlers;

import java.util.function.Predicate;

import org.eclipse.core.commands.ExecutionEvent;

public class IsEventOnJavaFilePredicate implements Predicate<ExecutionEvent> {
    private static final String JAVA_TYPE = "org.eclipse.jdt.ui.CompilationUnitEditor";
    private HandlerUtilWrapper handlerUtilWrapper;

    public IsEventOnJavaFilePredicate(HandlerUtilWrapper handlerUtilWrapper) {
        this.handlerUtilWrapper = handlerUtilWrapper;
    }

    @Override
    public boolean test(ExecutionEvent event) {
        String activePartId = handlerUtilWrapper.getActivePartId(event);
        return JAVA_TYPE.equals(activePartId);
    }
}

package com.helospark.spark.builder.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

/**
 * Generates a builder with the given type.
 * @author helospark
 */
public interface GenerateBuilderExecutor {

    void execute(ExecutionEvent event, BuilderType staged) throws ExecutionException;

}
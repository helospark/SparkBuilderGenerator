package com.helospark.spark.builder.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public interface GenerateBuilderExecutor {

	void execute(ExecutionEvent event, BuilderType staged) throws ExecutionException;

}
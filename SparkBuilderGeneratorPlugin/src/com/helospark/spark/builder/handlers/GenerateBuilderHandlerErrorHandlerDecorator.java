package com.helospark.spark.builder.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.helospark.spark.builder.handlers.exception.PluginException;

public class GenerateBuilderHandlerErrorHandlerDecorator implements GenerateBuilderExecutor {
	private GenerateBuilderExecutor generateBuilderHandlerDelegate;
	private ErrorHandlerHook errorHandlerHook;

	public GenerateBuilderHandlerErrorHandlerDecorator(GenerateBuilderExecutor generateBuilderHandlerDelegate,
			ErrorHandlerHook errorHandlerHook) {
		this.generateBuilderHandlerDelegate = generateBuilderHandlerDelegate;
		this.errorHandlerHook = errorHandlerHook;
	}

	@Override
	public void execute(ExecutionEvent event, BuilderType builderType) throws ExecutionException {
		try {
			generateBuilderHandlerDelegate.execute(event, builderType);
		} catch (PluginException e) {
			errorHandlerHook.onPluginException(e);
		} catch (Exception e) {
			errorHandlerHook.onUnexpectedException(e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}

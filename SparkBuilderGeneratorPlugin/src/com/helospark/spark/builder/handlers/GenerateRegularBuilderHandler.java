package com.helospark.spark.builder.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.helospark.spark.builder.DiContainer;

public class GenerateRegularBuilderHandler extends AbstractHandler {
	private GenerateBuilderExecutor generateBuilderExecutor;

	public GenerateRegularBuilderHandler() {
		this(DiContainer.getDependency(StateInitializerGenerateBuilderExecutorDecorator.class));
	}

	public GenerateRegularBuilderHandler(GenerateBuilderExecutor generateBuilderExecutor) {
		this.generateBuilderExecutor = generateBuilderExecutor;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		generateBuilderExecutor.execute(event, BuilderType.REGULAR);
		return null;
	}

}

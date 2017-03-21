package com.helospark.spark.builder.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.helospark.spark.builder.DiContainer;

public class GenerateStagedBuilderHandler extends AbstractHandler {
    private GenerateBuilderExecutor generateBuilderExecutor;

    public GenerateStagedBuilderHandler() {
        this(DiContainer.getDependency(StateInitializerGenerateBuilderExecutorDecorator.class));
    }

    public GenerateStagedBuilderHandler(GenerateBuilderExecutor generateBuilderExecutor) {
        this.generateBuilderExecutor = generateBuilderExecutor;
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        generateBuilderExecutor.execute(event, BuilderType.STAGED);
        return null;
    }

}

package com.helospark.spark.builder.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

/**
 * Decorator to initialize plugin state when an event occurres.
 * @deprecated builder generator should be stateless eventually, at that point this can be deleted
 * @author helospark
 */
@Deprecated
public class StateInitializerGenerateBuilderExecutorDecorator implements GenerateBuilderExecutor {
    private GenerateBuilderExecutor delegate;
    private StatefulBeanHandler statefulBeanHandler;

    public StateInitializerGenerateBuilderExecutorDecorator(GenerateBuilderExecutor delegate,
            StatefulBeanHandler statefulBeanHandler) {
        this.delegate = delegate;
        this.statefulBeanHandler = statefulBeanHandler;
    }

    @Override
    public void execute(ExecutionEvent event, BuilderType builderType) throws ExecutionException {
        statefulBeanHandler.initializeState(event);
        try {
            delegate.execute(event, builderType);
        } finally {
            statefulBeanHandler.clearState();
        }
    }

}

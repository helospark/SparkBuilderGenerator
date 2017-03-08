package com.helospark.SparkTemplatingPlugin.integration.execution;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.MockitoAnnotations.initMocks;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IProject;
import org.mockito.Mock;

import com.helospark.SparkTemplatingPlugin.integration.execution.support.BufferBackedTestTemplatingResult;
import com.helospark.sparktemplatingplugin.DiContainer;
import com.helospark.sparktemplatingplugin.execute.templater.Templater;
import com.helospark.sparktemplatingplugin.execute.templater.TemplatingResultFactory;
import com.helospark.sparktemplatingplugin.execute.templater.provider.CompilationUnitProvider;
import com.helospark.sparktemplatingplugin.execute.templater.provider.CurrentClassProvider;
import com.helospark.sparktemplatingplugin.execute.templater.provider.CurrentProjectProvider;
import com.helospark.sparktemplatingplugin.wrapper.SttCompilationUnit;
import com.helospark.sparktemplatingplugin.wrapper.SttType;

public class ExecutionITSetup {
    protected Templater underTest;
    @Mock
    protected SttCompilationUnit compilationUnit;
    @Mock
    protected CompilationUnitProvider mockCompilationUnitProvider;
    @Mock
    protected TemplatingResultFactory templatingResultFactory;
    @Mock
    protected CurrentProjectProvider currentProjectProvider;
    @Mock
    protected CurrentClassProvider currentClassProvider;
    @Mock
    protected IProject project;
    @Mock
    protected SttType type;

    protected ExecutionEvent dummyExecutionEvent = new ExecutionEvent();
    protected BufferBackedTestTemplatingResult templatingResult = new BufferBackedTestTemplatingResult();

    public void initialize() {
        initMocks(this);
        DiContainer.clearDiContainer();

        // Override real dependencies with mocks
        DiContainer.addDependency(mockCompilationUnitProvider);
        DiContainer.addDependency(templatingResultFactory);
        DiContainer.addDependency(currentProjectProvider);
        DiContainer.addDependency(currentClassProvider);
        // end of overrides

        DiContainer.initializeDiContainer();

        underTest = DiContainer.getDependency(Templater.class);

        given(mockCompilationUnitProvider.provide(any(ExecutionEvent.class))).willReturn(compilationUnit);
        given(mockCompilationUnitProvider.getExposedName()).willReturn(CompilationUnitProvider.EXPOSED_NAME);

        given(templatingResultFactory.createTemplatingResult(any(ExecutionEvent.class))).willReturn(templatingResult);
        given(templatingResultFactory.getExposedName()).willReturn(TemplatingResultFactory.SCRIPT_EXPOSED_NAME);

        given(currentProjectProvider.provide(any(ExecutionEvent.class))).willReturn(project);
        given(currentProjectProvider.getExposedName()).willReturn(CurrentProjectProvider.EXPOSED_NAME);

        given(currentClassProvider.provide(any(ExecutionEvent.class))).willReturn(type);
        given(currentClassProvider.getExposedName()).willReturn(CurrentClassProvider.EXPOSED_NAME);
    }
}

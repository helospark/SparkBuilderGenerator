package com.helospark.sparktemplatingplugin.execute.templater.provider;

import org.eclipse.core.commands.ExecutionEvent;

import com.helospark.sparktemplatingplugin.execute.templater.CodeFormatterService;
import com.helospark.sparktemplatingplugin.execute.templater.ScriptExposedProvider;

public class CodeFormatterServiceProvider implements ScriptExposedProvider {
    public static final String SCRIPT_EXPOSED_NAME = "codeFormatter";
    private CompilationUnitProvider compilationUnitProvider;

    public CodeFormatterServiceProvider(CompilationUnitProvider compilationUnitProvider) {
        this.compilationUnitProvider = compilationUnitProvider;
    }

    @Override
    public Object provide(ExecutionEvent executionEvent) {
        return new CodeFormatterService(compilationUnitProvider, executionEvent);
    }

    @Override
    public Class<?> getExposedObjectType() {
        return CodeFormatterService.class;
    }

    @Override
    public String getExposedName() {
        return SCRIPT_EXPOSED_NAME;
    }

}

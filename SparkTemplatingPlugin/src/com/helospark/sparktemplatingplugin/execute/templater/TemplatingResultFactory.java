package com.helospark.sparktemplatingplugin.execute.templater;

import org.eclipse.core.commands.ExecutionEvent;

import com.helospark.sparktemplatingplugin.execute.templater.helper.CompilationUnitCreator;
import com.helospark.sparktemplatingplugin.execute.templater.helper.PackageRootFinder;
import com.helospark.sparktemplatingplugin.execute.templater.provider.CompilationUnitProvider;

public class TemplatingResultFactory {
    public static final String SCRIPT_EXPOSED_NAME = "result";
    private CompilationUnitProvider compilationUnitProvider;
    private CompilationUnitCreator compilationUnitCreator;
    private PackageRootFinder packageRootFinder;

    public TemplatingResultFactory(CompilationUnitProvider compilationUnitProvider, CompilationUnitCreator compilationUnitCreator, PackageRootFinder packageRootFinder) {
        this.compilationUnitProvider = compilationUnitProvider;
        this.compilationUnitCreator = compilationUnitCreator;
        this.packageRootFinder = packageRootFinder;
    }

    public ITemplatingResult createTemplatingResult(ExecutionEvent event) {
        return new StringBufferBackedTemplatingResult(compilationUnitProvider, compilationUnitCreator, packageRootFinder, event);
    }

    public String getExposedName() {
        return SCRIPT_EXPOSED_NAME;
    }

    public Class<?> getExposedObjectType() {
        return ITemplatingResult.class;
    }
}

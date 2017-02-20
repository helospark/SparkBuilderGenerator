package com.helospark.sparktemplatingplugin.execute.templater;

import org.eclipse.core.commands.ExecutionEvent;

import com.helospark.sparktemplatingplugin.execute.templater.helper.CompilationUnitCreator;
import com.helospark.sparktemplatingplugin.execute.templater.helper.PackageRootFinder;
import com.helospark.sparktemplatingplugin.execute.templater.provider.CompilationUnitProvider;

public class TemplatingResultFactory {
    private CompilationUnitProvider compilationUnitProvider;
    private CompilationUnitCreator compilationUnitCreator;
    private PackageRootFinder packageRootFinder;

    public TemplatingResultFactory(CompilationUnitProvider compilationUnitProvider, CompilationUnitCreator compilationUnitCreator, PackageRootFinder packageRootFinder) {
        this.compilationUnitProvider = compilationUnitProvider;
        this.compilationUnitCreator = compilationUnitCreator;
        this.packageRootFinder = packageRootFinder;
    }

    public TemplatingResult createTemplatingResult(ExecutionEvent event) {
        return new TemplatingResult(compilationUnitProvider, compilationUnitCreator, packageRootFinder, event);
    }

    public String getExposedName() {
        return "result";
    }

    public Class<?> getExposedObjectType() {
        return TemplatingResult.class;
    }
}

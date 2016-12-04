package com.helospark.sparktemplatingplugin.handlers.templater;

import org.eclipse.core.commands.ExecutionEvent;

import com.helospark.sparktemplatingplugin.handlers.templater.helper.CompilationUnitCreator;
import com.helospark.sparktemplatingplugin.handlers.templater.helper.PackageRootFinder;
import com.helospark.sparktemplatingplugin.handlers.templater.provider.CompilationUnitProvider;

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
}

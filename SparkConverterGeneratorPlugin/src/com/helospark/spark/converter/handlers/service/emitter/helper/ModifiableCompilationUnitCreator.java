package com.helospark.spark.converter.handlers.service.emitter.helper;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;

import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;

public class ModifiableCompilationUnitCreator {
    private CompilationUnitParser compilationUnitParser;
    private CompilationUnitCreator compilationUnitCreator;

    public ModifiableCompilationUnitCreator(CompilationUnitParser compilationUnitParser, CompilationUnitCreator compilationUnitCreator) {
        this.compilationUnitParser = compilationUnitParser;
        this.compilationUnitCreator = compilationUnitCreator;
    }

    public CompilationUnitModificationDomain create(IPackageFragmentRoot packageFragmentRoot, String packageName, String className) {
        ICompilationUnit iCompilationUnit = compilationUnitCreator.createCompilationUnit(packageFragmentRoot, packageName, className);
        CompilationUnit compilationUnit = compilationUnitParser.parse(iCompilationUnit);
        AST ast = compilationUnit.getAST();
        return CompilationUnitModificationDomain.builder()
                .withAst(ast)
                .withICompilationUnit(iCompilationUnit)
                .withCompilationUnit(compilationUnit)
                .build();
    }
}

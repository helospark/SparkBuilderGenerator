package com.helospark.spark.converter.handlers.service;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.BadLocationException;

import com.helospark.spark.converter.handlers.domain.ConverterInputParameters;

/**
 * Generates the converter.
 * 
 * @author helospark
 */
public class ConverterGenerator {
    private PackageRootFinder packageRootFinder;
    private CompilationUnitCreator compilationUnitCreator;
    private CompilationUnitParser compilationUnitParser;
    private ClassTypeAppender classTypeAppender;
    private ConverterClassGenerator converterClassGenerator;

    public ConverterGenerator(PackageRootFinder packageRootFinder, CompilationUnitCreator compilationUnitCreator, CompilationUnitParser compilationUnitParser,
            ClassTypeAppender classTypeAppender, ConverterClassGenerator converterClassGenerator) {
        super();
        this.packageRootFinder = packageRootFinder;
        this.compilationUnitCreator = compilationUnitCreator;
        this.compilationUnitParser = compilationUnitParser;
        this.classTypeAppender = classTypeAppender;
        this.converterClassGenerator = converterClassGenerator;
    }

    public void generate(ConverterInputParameters converterInputParameters) throws JavaModelException, BadLocationException {
        IPackageFragmentRoot src = packageRootFinder.findSrcPackageFragmentRoot(converterInputParameters.getJavaProject());
        ICompilationUnit iCompilationUnit = compilationUnitCreator.createCompilationUnit(src, "com.helospark.cool", "CoolClass");
        CompilationUnit compilationUnit = compilationUnitParser.parse(iCompilationUnit);
        AST ast = compilationUnit.getAST();

        TypeDeclaration newType = converterClassGenerator.createConverter(ast);
        newType.bodyDeclarations().add(createMethod(ast));

        classTypeAppender.addType(iCompilationUnit, compilationUnit, newType);
    }

    public MethodDeclaration createMethod(AST ast) {
        MethodDeclaration newMethod = ast.newMethodDeclaration();
        newMethod.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
        newMethod.setName(ast.newSimpleName("convert"));
        newMethod.setBody(ast.newBlock());
        return newMethod;
    }

}

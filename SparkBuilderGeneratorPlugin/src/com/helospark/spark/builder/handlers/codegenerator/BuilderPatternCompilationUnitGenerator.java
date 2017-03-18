package com.helospark.spark.builder.handlers.codegenerator;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import com.helospark.spark.builder.handlers.codegenerator.component.BuilderClassCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.BuilderMethodListRewritePopulator;
import com.helospark.spark.builder.handlers.codegenerator.component.ImportPopulator;
import com.helospark.spark.builder.handlers.codegenerator.component.PrivateConstructorListRewritePopulator;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;
import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

/**
 * Generates the builder to the given compilation unit.
 *
 * @author helospark
 */
public class BuilderPatternCompilationUnitGenerator {
    private ApplicableBuilderFieldExtractor applicableBuilderFieldExtractor;
    private BuilderClassCreator builderClassCreator;
    private PrivateConstructorListRewritePopulator privateConstructorPopulator;
    private BuilderMethodListRewritePopulator builderMethodPopulator;
    private ImportPopulator importPopulator;
    private BuilderOwnerClassFinder builderOwnerClassFinder;;

    public BuilderPatternCompilationUnitGenerator(ApplicableBuilderFieldExtractor applicableBuilderFieldExtractor, BuilderClassCreator builderClassCreator,
            PrivateConstructorListRewritePopulator privateConstructorCreator, BuilderMethodListRewritePopulator builderMethodCreator,
            ImportPopulator importPopulator, BuilderOwnerClassFinder builderOwnerClassFinder) {
        this.applicableBuilderFieldExtractor = applicableBuilderFieldExtractor;
        this.builderClassCreator = builderClassCreator;
        this.privateConstructorPopulator = privateConstructorCreator;
        this.builderMethodPopulator = builderMethodCreator;
        this.importPopulator = importPopulator;
        this.builderOwnerClassFinder = builderOwnerClassFinder;
    }

    public void generateBuilder(AST ast, ASTRewrite rewriter, CompilationUnit compilationUnit) {
        CompilationUnitModificationDomain result = builderOwnerClassFinder.asd(compilationUnit, ast, rewriter);
        TypeDeclaration originalType = result.getOriginalType();
        ListRewrite listRewrite = result.getListRewrite();

        List<NamedVariableDeclarationField> namedVariableDeclarations = applicableBuilderFieldExtractor.findBuilderFields(originalType);
        TypeDeclaration builderType = builderClassCreator.createBuilderClass(ast, originalType, namedVariableDeclarations);
        privateConstructorPopulator.addPrivateConstructorToCompilationUnit(ast, originalType, builderType, listRewrite, namedVariableDeclarations);
        builderMethodPopulator.addBuilderMethodToCompilationUnit(ast, listRewrite, originalType, builderType);
        listRewrite.insertLast(builderType, null);
        importPopulator.populateImports(ast, rewriter, compilationUnit);
    }

}

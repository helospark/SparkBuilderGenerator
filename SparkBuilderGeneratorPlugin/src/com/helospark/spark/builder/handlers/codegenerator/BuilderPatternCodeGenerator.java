package com.helospark.spark.builder.handlers.codegenerator;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import com.helospark.spark.builder.handlers.codegenerator.component.BuilderClassCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.BuilderMethodListRewritePopulator;
import com.helospark.spark.builder.handlers.codegenerator.component.PrivateConstructorListRewritePopulator;
import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

/**
 * Generates the builder to the given compilation unit.
 * 
 * @author helospark
 */
public class BuilderPatternCodeGenerator {
    private ApplicableFieldExtractor applicableFieldExtractor;
    private BuilderClassCreator builderClassCreator;
    private PrivateConstructorListRewritePopulator privateConstructorPopulator;
    private BuilderMethodListRewritePopulator builderMethodPopulator;

    public BuilderPatternCodeGenerator(ApplicableFieldExtractor applicableFieldExtractor, BuilderClassCreator builderClassCreator,
            PrivateConstructorListRewritePopulator privateConstructorCreator, BuilderMethodListRewritePopulator builderMethodCreator) {
        this.applicableFieldExtractor = applicableFieldExtractor;
        this.builderClassCreator = builderClassCreator;
        this.privateConstructorPopulator = privateConstructorCreator;
        this.builderMethodPopulator = builderMethodCreator;
    }

    public void generateBuilder(AST ast, ASTRewrite rewriter, CompilationUnit compilationUnit) {
        TypeDeclaration originalType = (TypeDeclaration) compilationUnit.types().get(0);
        ListRewrite listRewrite = rewriter.getListRewrite(originalType, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
        addBuilderToAst(ast, originalType, listRewrite);
    }

    private void addBuilderToAst(AST ast, TypeDeclaration originalType, ListRewrite listRewrite) {
        List<NamedVariableDeclarationField> namedVariableDeclarations = findBuildableFields(originalType);
        TypeDeclaration builderType = builderClassCreator.createBuilderClass(ast, originalType, namedVariableDeclarations);
        privateConstructorPopulator.addPrivateConstructorToCompilationUnit(ast, originalType, builderType, listRewrite, namedVariableDeclarations);
        builderMethodPopulator.addBuilderMethodToCompilationUnit(ast, listRewrite, originalType, builderType);
        listRewrite.insertLast(builderType, null);
    }

    private List<NamedVariableDeclarationField> findBuildableFields(TypeDeclaration typeDecl) {
        FieldDeclaration[] fields = typeDecl.getFields();
        return applicableFieldExtractor.filterApplicableFields(fields);
    }

}

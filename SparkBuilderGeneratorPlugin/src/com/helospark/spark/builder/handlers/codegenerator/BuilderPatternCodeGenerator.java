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
import com.helospark.spark.builder.handlers.codegenerator.component.ImportPopulator;
import com.helospark.spark.builder.handlers.codegenerator.component.PrivateConstructorListRewritePopulator;
import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;
import com.helospark.spark.builder.handlers.exception.PluginException;

/**
 * Generates the builder to the given compilation unit.
 * 
 * @author helospark
 */
public class BuilderPatternCodeGenerator {
    private ApplicableBuilderFieldConverter applicableBuilderFieldConverter;
    private BuilderClassCreator builderClassCreator;
    private PrivateConstructorListRewritePopulator privateConstructorPopulator;
    private BuilderMethodListRewritePopulator builderMethodPopulator;
    private ImportPopulator importPopulator;

    public BuilderPatternCodeGenerator(ApplicableBuilderFieldConverter applicableBuilderFieldConverter, BuilderClassCreator builderClassCreator,
            PrivateConstructorListRewritePopulator privateConstructorCreator, BuilderMethodListRewritePopulator builderMethodCreator,
            ImportPopulator importPopulator) {
        this.applicableBuilderFieldConverter = applicableBuilderFieldConverter;
        this.builderClassCreator = builderClassCreator;
        this.privateConstructorPopulator = privateConstructorCreator;
        this.builderMethodPopulator = builderMethodCreator;
        this.importPopulator = importPopulator;
    }

    public void generateBuilder(AST ast, ASTRewrite rewriter, CompilationUnit compilationUnit) {
        List types = compilationUnit.types();
        if (types == null || types.size() == 0) {
            throw new PluginException("No types are present in the current java file");
        }
        TypeDeclaration originalType = (TypeDeclaration) types.get(0);
        ListRewrite listRewrite = rewriter.getListRewrite(originalType, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
        addBuilderToAst(ast, originalType, listRewrite);
        importPopulator.populateImports(ast, rewriter, compilationUnit);
    }

    private void addBuilderToAst(AST ast, TypeDeclaration originalType, ListRewrite listRewrite) {
        List<NamedVariableDeclarationField> namedVariableDeclarations = collectBuildableFields(originalType);
        TypeDeclaration builderType = builderClassCreator.createBuilderClass(ast, originalType, namedVariableDeclarations);
        privateConstructorPopulator.addPrivateConstructorToCompilationUnit(ast, originalType, builderType, listRewrite, namedVariableDeclarations);
        builderMethodPopulator.addBuilderMethodToCompilationUnit(ast, listRewrite, originalType, builderType);
        listRewrite.insertLast(builderType, null);
    }

    private List<NamedVariableDeclarationField> collectBuildableFields(TypeDeclaration typeDecl) {
        FieldDeclaration[] fields = typeDecl.getFields();
        return applicableBuilderFieldConverter.convertApplicableFields(fields);
    }

}

package com.helospark.spark.builder.handlers.codegenerator;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import com.helospark.spark.builder.handlers.codegenerator.component.ImportPopulator;
import com.helospark.spark.builder.handlers.codegenerator.component.PrivateInitializingConstructorCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.RegularBuilderBuilderMethodCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.RegularBuilderClassCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.RegularBuilderCopyBuilderMethodCreator;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;

/**
 * Generates a regular builder to the given compilation unit.
 *
 * @author helospark
 */
public class RegularBuilderCompilationUnitGenerator {
    private RegularBuilderClassCreator regularBuilderClassCreator;
    private PrivateInitializingConstructorCreator privateConstructorPopulator;
    private RegularBuilderBuilderMethodCreator builderMethodPopulator;
    private RegularBuilderCopyBuilderMethodCreator copyBuilderMethodPopulator;
    private ImportPopulator importPopulator;
    private BuilderRemover builderRemover;

    public RegularBuilderCompilationUnitGenerator(RegularBuilderClassCreator regularBuilderClassCreator,
            RegularBuilderCopyBuilderMethodCreator copyBuilderMethodPopulator,
            PrivateInitializingConstructorCreator privateInitializingConstructorCreator,
            RegularBuilderBuilderMethodCreator regularBuilderBuilderMethodCreator,
            ImportPopulator importPopulator,
            BuilderRemover builderRemover) {
        this.regularBuilderClassCreator = regularBuilderClassCreator;
        this.copyBuilderMethodPopulator = copyBuilderMethodPopulator;
        this.privateConstructorPopulator = privateInitializingConstructorCreator;
        this.builderMethodPopulator = regularBuilderBuilderMethodCreator;
        this.importPopulator = importPopulator;
        this.builderRemover = builderRemover;
    }

    public void generateBuilder(CompilationUnitModificationDomain compilationUnitModificationDomain, List<BuilderField> builderFields) {
        // TODO: replace parameters, where these go separately with compilation modification domain
        AST ast = compilationUnitModificationDomain.getAst();
        ListRewrite listRewrite = compilationUnitModificationDomain.getListRewrite();
        TypeDeclaration originalType = compilationUnitModificationDomain.getOriginalType();

        builderRemover.removeExistingBuilderWhenNeeded(compilationUnitModificationDomain);

        TypeDeclaration builderType = regularBuilderClassCreator.createBuilderClass(ast, originalType, builderFields);
        privateConstructorPopulator.addPrivateConstructorToCompilationUnit(ast, originalType, builderType, listRewrite, builderFields);
        builderMethodPopulator.addBuilderMethodToCompilationUnit(ast, listRewrite, originalType, builderType);
        copyBuilderMethodPopulator.addCopyBuilderMethodToCompilationUnitIfNeeded(compilationUnitModificationDomain, builderType);

        listRewrite.insertLast(builderType, null);
        importPopulator.populateImports(compilationUnitModificationDomain);
    }

}

package com.helospark.spark.builder.handlers.codegenerator.component;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.empty.BlockWithNewBuilderCreationFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.empty.BuilderMethodDefinitionCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderProperties;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;

/**
 * Adds the builder() method for a staged builder.
 * Generated code is something like:
 * <pre>
 * public static IFirstStage builder() {
 *   return new Builder();
 * }
 * </pre>
 * @author helospark
 */
public class StagedBuilderCreationBuilderMethodAdder {
    private BlockWithNewBuilderCreationFragment blockWithNewBuilderCreationFragment;
    private BuilderMethodDefinitionCreatorFragment builderMethodDefinitionCreatorFragment;

    public StagedBuilderCreationBuilderMethodAdder(BlockWithNewBuilderCreationFragment blockWithNewBuilderCreationFragment,
            BuilderMethodDefinitionCreatorFragment builderMethodDefinitionCreatorFragment) {
        this.blockWithNewBuilderCreationFragment = blockWithNewBuilderCreationFragment;
        this.builderMethodDefinitionCreatorFragment = builderMethodDefinitionCreatorFragment;
    }

    public void addBuilderMethodToCompilationUnit(CompilationUnitModificationDomain modificationDomain, AbstractTypeDeclaration builderType,
            StagedBuilderProperties stagedBuilderStages) {
        AST ast = modificationDomain.getAst();
        AbstractTypeDeclaration typeDeclaration = modificationDomain.getOriginalType();
        ListRewrite listRewrite = modificationDomain.getListRewrite();
        Block builderMethodBlock = blockWithNewBuilderCreationFragment.createReturnBlock(ast, builderType);
        MethodDeclaration builderMethod = builderMethodDefinitionCreatorFragment.createBuilderMethod(ast, typeDeclaration, stagedBuilderStages.getInterfaceName());
        builderMethod.setBody(builderMethodBlock);
        listRewrite.insertLast(builderMethod, null);
    }

}

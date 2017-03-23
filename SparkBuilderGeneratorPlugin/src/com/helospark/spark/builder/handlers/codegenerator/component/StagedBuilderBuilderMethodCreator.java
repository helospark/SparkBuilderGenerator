package com.helospark.spark.builder.handlers.codegenerator.component;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.BlockWithNewBuilderCreationFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.BuilderMethodDefinitionCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;

public class StagedBuilderBuilderMethodCreator {
    private BlockWithNewBuilderCreationFragment blockWithNewBuilderCreationFragment;
    private BuilderMethodDefinitionCreatorFragment builderMethodDefinitionCreatorFragment;

    public StagedBuilderBuilderMethodCreator(BlockWithNewBuilderCreationFragment blockWithNewBuilderCreationFragment,
            BuilderMethodDefinitionCreatorFragment builderMethodDefinitionCreatorFragment) {
        this.blockWithNewBuilderCreationFragment = blockWithNewBuilderCreationFragment;
        this.builderMethodDefinitionCreatorFragment = builderMethodDefinitionCreatorFragment;
    }

    public void addBuilderMethodToCompilationUnit(CompilationUnitModificationDomain modificationDomain, TypeDeclaration builderType, TypeDeclaration stagedInterface) {
        AST ast = modificationDomain.getAst();
        TypeDeclaration typeDeclaration = modificationDomain.getOriginalType();
        ListRewrite listRewrite = modificationDomain.getListRewrite();
        Block builderMethodBlock = blockWithNewBuilderCreationFragment.createReturnBlock(ast, builderType);
        MethodDeclaration builderMethod = builderMethodDefinitionCreatorFragment.createBuilderMethod(ast, typeDeclaration, stagedInterface);
        builderMethod.setBody(builderMethodBlock);
        listRewrite.insertLast(builderMethod, null);
    }

}

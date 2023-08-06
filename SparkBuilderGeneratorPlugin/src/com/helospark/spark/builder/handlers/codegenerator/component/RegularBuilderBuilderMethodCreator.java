package com.helospark.spark.builder.handlers.codegenerator.component;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.empty.BlockWithNewBuilderCreationFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.empty.BuilderMethodDefinitionCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.domain.RegularBuilderUserPreference;

/**
 * Adds the builder() method for a regular builder.
 * Generated code is something like:
 * <pre>
 * public static Builder builder() {
 *   return new Builder();
 * }
 * </pre>
 * @author helospark
 */
public class RegularBuilderBuilderMethodCreator {
    private BlockWithNewBuilderCreationFragment blockWithNewBuilderCreationFragment;
    private BuilderMethodDefinitionCreatorFragment builderMethodDefinitionCreatorFragment;

    public RegularBuilderBuilderMethodCreator(BlockWithNewBuilderCreationFragment blockWithNewBuilderCreationFragment,
            BuilderMethodDefinitionCreatorFragment builderMethodDefinitionCreatorFragment) {
        this.blockWithNewBuilderCreationFragment = blockWithNewBuilderCreationFragment;
        this.builderMethodDefinitionCreatorFragment = builderMethodDefinitionCreatorFragment;
    }

    public void addBuilderMethodToCompilationUnit(AST ast, ListRewrite listRewrite, AbstractTypeDeclaration typeDeclaration, TypeDeclaration builderType, RegularBuilderUserPreference preference) {
        if (builderMethodNeeded(preference)) {
            addBuilderMethod(ast, listRewrite, typeDeclaration, builderType);
        }
    }

    private boolean builderMethodNeeded(RegularBuilderUserPreference preference) {
        return !preference.isCreatePublicConstructorWithMandatoryFields();
    }

    private void addBuilderMethod(AST ast, ListRewrite listRewrite, AbstractTypeDeclaration typeDeclaration, TypeDeclaration builderType) {
        Block builderMethodBlock = blockWithNewBuilderCreationFragment.createReturnBlock(ast, builderType);
        MethodDeclaration builderMethod = builderMethodDefinitionCreatorFragment.createBuilderMethod(ast, typeDeclaration, builderType.getName().toString());
        builderMethod.setBody(builderMethodBlock);
        listRewrite.insertLast(builderMethod, null);
    }

}

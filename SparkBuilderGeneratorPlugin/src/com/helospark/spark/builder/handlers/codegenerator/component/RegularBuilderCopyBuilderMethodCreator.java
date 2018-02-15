package com.helospark.spark.builder.handlers.codegenerator.component;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.copy.BlockWithNewCopyBuilderCreationFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.copy.CopyBuilderMethodDefinitionCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TypeDeclarationToVariableNameConverter;
import com.helospark.spark.builder.preferences.PluginPreferenceList;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Adds the builder() method for a regular builder when copying a domain object.
 * Generated code is something like:
 * <pre>
 * public static Builder builder(OriginalType originalType) {
 *   return new Builder(originalType);
 * }
 * </pre>
 * @author helospark
 */
public class RegularBuilderCopyBuilderMethodCreator {
    private BlockWithNewCopyBuilderCreationFragment blockWithNewCopyBuilderCreationFragment;
    private CopyBuilderMethodDefinitionCreatorFragment copyBuilderMethodDefinitionCreatorFragment;
    private TypeDeclarationToVariableNameConverter typeDeclarationToVariableNameConverter;
    private PreferencesManager preferencesManager;

    public RegularBuilderCopyBuilderMethodCreator(BlockWithNewCopyBuilderCreationFragment blockWithNewCopyBuilderCreationFragment,
            CopyBuilderMethodDefinitionCreatorFragment copyBuilderMethodDefinitionCreatorFragment,
            TypeDeclarationToVariableNameConverter typeDeclarationToVariableNameConverter,
            PreferencesManager preferencesManager) {
        this.blockWithNewCopyBuilderCreationFragment = blockWithNewCopyBuilderCreationFragment;
        this.copyBuilderMethodDefinitionCreatorFragment = copyBuilderMethodDefinitionCreatorFragment;
        this.typeDeclarationToVariableNameConverter = typeDeclarationToVariableNameConverter;
        this.preferencesManager = preferencesManager;
    }

    public void addCopyBuilderMethodToCompilationUnitIfNeeded(AST ast, ListRewrite listRewrite, TypeDeclaration typeDeclaration, TypeDeclaration builderType) {
        if (preferencesManager.getPreferenceValue(PluginPreferenceList.CREATE_BUILDER_COPY_METHOD)) {
            addCopyBuilderMethod(ast, listRewrite, typeDeclaration, builderType);
        }
    }

    private void addCopyBuilderMethod(AST ast, ListRewrite listRewrite, TypeDeclaration typeDeclaration, TypeDeclaration builderType) {
        String parameterName = typeDeclarationToVariableNameConverter.convert(typeDeclaration);
        Block builderMethodBlock = blockWithNewCopyBuilderCreationFragment.createReturnBlock(ast, builderType, parameterName);
        MethodDeclaration builderMethod = copyBuilderMethodDefinitionCreatorFragment.createBuilderMethod(ast, typeDeclaration, builderType.getName().toString(), parameterName);
        builderMethod.setBody(builderMethodBlock);
        listRewrite.insertLast(builderMethod, null);
    }

}

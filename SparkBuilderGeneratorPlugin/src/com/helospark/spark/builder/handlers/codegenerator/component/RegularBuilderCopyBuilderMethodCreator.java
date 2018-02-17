package com.helospark.spark.builder.handlers.codegenerator.component;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.CREATE_BUILDER_COPY_METHOD;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.copy.BlockWithNewCopyBuilderCreationFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.copy.CopyBuilderMethodDefinitionCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TypeDeclarationToVariableNameConverter;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;
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

    public void addCopyBuilderMethodToCompilationUnitIfNeeded(CompilationUnitModificationDomain compilationUnitModificationDomain, TypeDeclaration builderType) {
        if (preferencesManager.getPreferenceValue(CREATE_BUILDER_COPY_METHOD)) {
            addCopyBuilderMethod(compilationUnitModificationDomain, builderType);
        }
    }

    private void addCopyBuilderMethod(CompilationUnitModificationDomain compilationUnitModificationDomain, TypeDeclaration builderType) {
        TypeDeclaration originalType = compilationUnitModificationDomain.getOriginalType();
        AST ast = compilationUnitModificationDomain.getAst();
        String parameterName = typeDeclarationToVariableNameConverter.convert(originalType);

        MethodDeclaration builderMethod = copyBuilderMethodDefinitionCreatorFragment.createBuilderMethod(ast, originalType, builderType.getName().toString(), parameterName);

        Block methodBody = blockWithNewCopyBuilderCreationFragment.createReturnBlock(ast, builderType, parameterName);
        builderMethod.setBody(methodBody);

        compilationUnitModificationDomain.getListRewrite().insertLast(builderMethod, null);
    }

}

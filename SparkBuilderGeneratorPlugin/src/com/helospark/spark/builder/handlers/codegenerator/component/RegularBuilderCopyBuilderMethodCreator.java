package com.helospark.spark.builder.handlers.codegenerator.component;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.copy.BlockWithNewCopyBuilderCreationFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.copy.CopyBuilderMethodDefinitionCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.IsRegularBuilderCopyMethodEnabledPredicate;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TypeDeclarationToVariableNameConverter;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;
import com.helospark.spark.builder.handlers.codegenerator.domain.RegularBuilderUserPreference;

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
    private IsRegularBuilderCopyMethodEnabledPredicate isRegularBuilderCopyMethodEnabledPredicate;

    public RegularBuilderCopyBuilderMethodCreator(BlockWithNewCopyBuilderCreationFragment blockWithNewCopyBuilderCreationFragment,
            CopyBuilderMethodDefinitionCreatorFragment copyBuilderMethodDefinitionCreatorFragment,
            TypeDeclarationToVariableNameConverter typeDeclarationToVariableNameConverter,
            IsRegularBuilderCopyMethodEnabledPredicate isRegularBuilderCopyMethodEnabledPredicate) {
        this.blockWithNewCopyBuilderCreationFragment = blockWithNewCopyBuilderCreationFragment;
        this.copyBuilderMethodDefinitionCreatorFragment = copyBuilderMethodDefinitionCreatorFragment;
        this.typeDeclarationToVariableNameConverter = typeDeclarationToVariableNameConverter;
        this.isRegularBuilderCopyMethodEnabledPredicate = isRegularBuilderCopyMethodEnabledPredicate;
    }

    public void addCopyBuilderMethodToCompilationUnitIfNeeded(CompilationUnitModificationDomain compilationUnitModificationDomain, TypeDeclaration builderType,
            RegularBuilderUserPreference preference) {
        if (isRegularBuilderCopyMethodEnabledPredicate.test(preference)) {
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

package com.helospark.spark.builder.handlers.codegenerator.component;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.copy.BlockWithNewCopyInstanceConstructorCreationFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.copy.CopyInstanceBuilderMethodDefinitionCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.IsRegularBuilderInstanceCopyEnabledPredicate;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TypeDeclarationToVariableNameConverter;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;
import com.helospark.spark.builder.handlers.codegenerator.domain.RegularBuilderUserPreference;

/**
 * Adds a static builder() method for a regular builder which copies the given instance to the builder.
 * Generated code is something like:
 * <pre>
 * public static Builder builder(OriginalType originalType) {
 *   return new Builder(originalType);
 * }
 * </pre>
 * @author helospark
 */
public class RegularBuilderCopyInstanceBuilderMethodCreator {
    private BlockWithNewCopyInstanceConstructorCreationFragment blockWithNewCopyInstanceConstructorCreationFragment;
    private CopyInstanceBuilderMethodDefinitionCreatorFragment copyInstanceBuilderMethodDefinitionCreatorFragment;
    private TypeDeclarationToVariableNameConverter typeDeclarationToVariableNameConverter;
    private IsRegularBuilderInstanceCopyEnabledPredicate isRegularBuilderInstanceCopyEnabledPredicate;

    public RegularBuilderCopyInstanceBuilderMethodCreator(BlockWithNewCopyInstanceConstructorCreationFragment blockWithNewCopyInstanceConstructorCreationFragment,
            CopyInstanceBuilderMethodDefinitionCreatorFragment copyInstanceBuilderMethodDefinitionCreatorFragment,
            TypeDeclarationToVariableNameConverter typeDeclarationToVariableNameConverter,
            IsRegularBuilderInstanceCopyEnabledPredicate isRegularBuilderInstanceCopyEnabledPredicate) {
        this.blockWithNewCopyInstanceConstructorCreationFragment = blockWithNewCopyInstanceConstructorCreationFragment;
        this.copyInstanceBuilderMethodDefinitionCreatorFragment = copyInstanceBuilderMethodDefinitionCreatorFragment;
        this.typeDeclarationToVariableNameConverter = typeDeclarationToVariableNameConverter;
        this.isRegularBuilderInstanceCopyEnabledPredicate = isRegularBuilderInstanceCopyEnabledPredicate;
    }

    public void addInstanceCopyBuilderMethodToCompilationUnitIfNeeded(CompilationUnitModificationDomain compilationUnitModificationDomain, TypeDeclaration builderType,
            RegularBuilderUserPreference preference) {
        if (isRegularBuilderInstanceCopyEnabledPredicate.test(preference)) {
            addInstanceCopyBuilderMethod(compilationUnitModificationDomain, builderType);
        }
    }

    private void addInstanceCopyBuilderMethod(CompilationUnitModificationDomain compilationUnitModificationDomain, TypeDeclaration builderType) {
        AbstractTypeDeclaration originalType = compilationUnitModificationDomain.getOriginalType();
        AST ast = compilationUnitModificationDomain.getAst();
        String parameterName = typeDeclarationToVariableNameConverter.convert(originalType);

        MethodDeclaration builderMethod = copyInstanceBuilderMethodDefinitionCreatorFragment.createBuilderMethod(ast, originalType, builderType.getName().toString(),
                parameterName);

        Block methodBody = blockWithNewCopyInstanceConstructorCreationFragment.createReturnBlock(ast, builderType, parameterName);
        builderMethod.setBody(methodBody);

        compilationUnitModificationDomain.getListRewrite().insertLast(builderMethod, null);
    }

}

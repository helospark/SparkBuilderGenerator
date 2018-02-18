package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.constructor;

import static org.eclipse.jdt.core.dom.Modifier.ModifierKeyword.PRIVATE_KEYWORD;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.fragment.constructor.FieldSetterAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.IsRegularBuilderInstanceCopyEnabledPredicate;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TypeDeclarationToVariableNameConverter;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.RegularBuilderUserPreference;

/**
 * Creates a private constructor that copies all fields from the given class.
 * <pre>
 * private Builder(OriginalType originalType) {
 *   this.field1 = originalType.field1;
 *   this.field2 = originalType.field2;
 * }
 * </pre>
 * @author helospark
 */
public class RegularBuilderCopyInstanceConstructorAdderFragment {
    private FieldSetterAdderFragment fieldSetterAdderFragment;
    private TypeDeclarationToVariableNameConverter typeDeclarationToVariableNameConverter;
    private IsRegularBuilderInstanceCopyEnabledPredicate isRegularBuilderInstanceCopyEnabledPredicate;

    public RegularBuilderCopyInstanceConstructorAdderFragment(FieldSetterAdderFragment fieldSetterAdderFragment,
            TypeDeclarationToVariableNameConverter typeDeclarationToVariableNameConverter,
            IsRegularBuilderInstanceCopyEnabledPredicate isRegularBuilderInstanceCopyEnabledPredicate) {
        this.fieldSetterAdderFragment = fieldSetterAdderFragment;
        this.typeDeclarationToVariableNameConverter = typeDeclarationToVariableNameConverter;
        this.isRegularBuilderInstanceCopyEnabledPredicate = isRegularBuilderInstanceCopyEnabledPredicate;
    }

    public void addCopyConstructorIfNeeded(AST ast, TypeDeclaration builderType, TypeDeclaration originalType, RegularBuilderUserPreference preference) {
        if (isRegularBuilderInstanceCopyEnabledPredicate.test(preference)) {
            createCopyConstructor(ast, builderType, originalType, preference.getBuilderFields());
        }
    }

    private void createCopyConstructor(AST ast, TypeDeclaration builderType, TypeDeclaration originalType, List<BuilderField> builderFields) {
        String originalTypeParameterName = typeDeclarationToVariableNameConverter.convert(originalType);

        Block methodBody = createCopyConstructorBody(ast, builderFields, originalTypeParameterName);
        MethodDeclaration copyConstructor = createCopyConstructorWithBody(ast, builderType, originalType, originalTypeParameterName, methodBody);
        builderType.bodyDeclarations().add(copyConstructor);
    }

    private Block createCopyConstructorBody(AST ast, List<BuilderField> builderFields, String originalTypeParameterName) {
        Block methodBody = ast.newBlock();
        fieldSetterAdderFragment.populateBodyWithFieldSetCalls(ast, originalTypeParameterName, methodBody, builderFields);
        return methodBody;
    }

    private MethodDeclaration createCopyConstructorWithBody(AST ast, TypeDeclaration builderType, TypeDeclaration originalType, String parameterName,
            Block body) {
        MethodDeclaration copyConstructor = ast.newMethodDeclaration();
        copyConstructor.setBody(body);
        copyConstructor.setConstructor(true);
        copyConstructor.setName(ast.newSimpleName(builderType.getName().toString()));
        copyConstructor.modifiers().add(ast.newModifier(PRIVATE_KEYWORD));
        copyConstructor.parameters().add(createParameter(ast, originalType, parameterName));
        return copyConstructor;
    }

    private SingleVariableDeclaration createParameter(AST ast, TypeDeclaration originalType, String parameterName) {
        SingleVariableDeclaration methodParameterDeclaration = ast.newSingleVariableDeclaration();
        methodParameterDeclaration.setType(ast.newSimpleType(ast.newName(originalType.getName().toString())));
        methodParameterDeclaration.setName(ast.newSimpleName(parameterName));
        return methodParameterDeclaration;
    }

}

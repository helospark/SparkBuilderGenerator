package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.constructor;

import static org.eclipse.jdt.core.dom.Modifier.ModifierKeyword.PRIVATE_KEYWORD;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

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
    private TypeDeclarationToVariableNameConverter typeDeclarationToVariableNameConverter;
    private IsRegularBuilderInstanceCopyEnabledPredicate isRegularBuilderInstanceCopyEnabledPredicate;

    public RegularBuilderCopyInstanceConstructorAdderFragment(TypeDeclarationToVariableNameConverter typeDeclarationToVariableNameConverter,
            IsRegularBuilderInstanceCopyEnabledPredicate isRegularBuilderInstanceCopyEnabledPredicate) {
        this.typeDeclarationToVariableNameConverter = typeDeclarationToVariableNameConverter;
        this.isRegularBuilderInstanceCopyEnabledPredicate = isRegularBuilderInstanceCopyEnabledPredicate;
    }

    public void addCopyConstructorIfNeeded(AST ast, TypeDeclaration builderType, AbstractTypeDeclaration originalType, RegularBuilderUserPreference preference) {
        if (isRegularBuilderInstanceCopyEnabledPredicate.test(preference)) {
            createCopyConstructor(ast, builderType, originalType, preference.getBuilderFields());
        }
    }

    private void createCopyConstructor(AST ast, TypeDeclaration builderType, AbstractTypeDeclaration originalType, List<BuilderField> builderFields) {
        String originalTypeParameterName = typeDeclarationToVariableNameConverter.convert(originalType);

        Block methodBody = createCopyConstructorBody(ast, builderFields, originalTypeParameterName);
        MethodDeclaration copyConstructor = createCopyConstructorWithBody(ast, builderType, originalType, originalTypeParameterName, methodBody);
        builderType.bodyDeclarations().add(copyConstructor);
    }

    private Block createCopyConstructorBody(AST ast, List<BuilderField> builderFields, String originalTypeParameterName) {
        Block methodBody = ast.newBlock();

        for (BuilderField field : builderFields) {
            Assignment assignment = ast.newAssignment();

            FieldAccess fieldAccess = createThisFieldAccess(ast, field);
            assignment.setLeftHandSide(fieldAccess);

            Expression builderFieldAccess;
            if (field.getOriginalFieldAccessStrategy().isPresent()) {
                builderFieldAccess = field.getOriginalFieldAccessStrategy().get().createFieldAccessExpression(ast, originalTypeParameterName);
            } else {
                builderFieldAccess = ast.newSimpleName("CANNOT_ACCESS_FIELD");
            }
            assignment.setRightHandSide(builderFieldAccess);
            methodBody.statements().add(ast.newExpressionStatement(assignment));
        }

        return methodBody;
    }

    private FieldAccess createThisFieldAccess(AST ast, BuilderField field) {
        FieldAccess fieldAccess = ast.newFieldAccess();
        fieldAccess.setExpression(ast.newThisExpression());
        fieldAccess.setName(ast.newSimpleName(field.getOriginalFieldName()));
        return fieldAccess;
    }

    private MethodDeclaration createCopyConstructorWithBody(AST ast, TypeDeclaration builderType, AbstractTypeDeclaration originalType, String parameterName,
            Block body) {
        MethodDeclaration copyConstructor = ast.newMethodDeclaration();
        copyConstructor.setBody(body);
        copyConstructor.setConstructor(true);
        copyConstructor.setName(ast.newSimpleName(builderType.getName().toString()));
        copyConstructor.modifiers().add(ast.newModifier(PRIVATE_KEYWORD));
        copyConstructor.parameters().add(createParameter(ast, originalType, parameterName));
        return copyConstructor;
    }

    private SingleVariableDeclaration createParameter(AST ast, AbstractTypeDeclaration originalType, String parameterName) {
        SingleVariableDeclaration methodParameterDeclaration = ast.newSingleVariableDeclaration();
        methodParameterDeclaration.setType(ast.newSimpleType(ast.newName(originalType.getName().toString())));
        methodParameterDeclaration.setName(ast.newSimpleName(parameterName));
        return methodParameterDeclaration;
    }

}

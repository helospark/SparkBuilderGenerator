package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.constructor;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;

/**
 * Creates public constructor for builder with mandatory parameters, generates something like:
 * 
 * public Builder(String value1, String value2) {
 *      this.value1 = value1;
 *      this.value2 = value2;
 * }
 * 
 * @author helospark
 */
public class PublicConstructorWithMandatoryFieldsAdderFragment {

    public void addPublicConstructor(AST ast, AbstractTypeDeclaration builderType, List<BuilderField> fields) {
        Block methodBody = createMethodBody(ast, fields);
        MethodDeclaration constructor = createPublicConstructor(ast, methodBody, builderType, fields);
        builderType.bodyDeclarations().add(constructor);
    }

    private MethodDeclaration createPublicConstructor(AST ast, Block body, AbstractTypeDeclaration builderType, List<BuilderField> fields) {
        MethodDeclaration publicConstructorMethod = ast.newMethodDeclaration();
        publicConstructorMethod.setBody(body);
        publicConstructorMethod.setConstructor(true);
        publicConstructorMethod.setName(ast.newSimpleName(builderType.getName().toString()));
        publicConstructorMethod.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));

        for (BuilderField field : fields) {
            SingleVariableDeclaration parameter = createParameter(ast, field.getFieldType(), field.getBuilderFieldName());
            publicConstructorMethod.parameters().add(parameter);
        }

        return publicConstructorMethod;

    }

    private SingleVariableDeclaration createParameter(AST ast, Type type, String parameterName) {
        SingleVariableDeclaration methodParameterDeclaration = ast.newSingleVariableDeclaration();
        methodParameterDeclaration.setType((Type) ASTNode.copySubtree(ast, type));
        methodParameterDeclaration.setName(ast.newSimpleName(parameterName));
        return methodParameterDeclaration;
    }

    public Block createMethodBody(AST ast, List<? extends BuilderField> builderFields) {
        Block body = ast.newBlock();
        for (BuilderField field : builderFields) {

            FieldAccess thisFieldAccess = createThisFieldAccess(ast, field);
            SimpleName parameterName = ast.newSimpleName(field.getBuilderFieldName());

            Assignment assignment = ast.newAssignment();
            assignment.setLeftHandSide(thisFieldAccess);
            assignment.setRightHandSide(parameterName);

            body.statements().add(ast.newExpressionStatement(assignment));
        }
        return body;
    }

    private FieldAccess createThisFieldAccess(AST ast, BuilderField field) {
        FieldAccess fieldAccess = ast.newFieldAccess();
        fieldAccess.setExpression(ast.newThisExpression());
        fieldAccess.setName(ast.newSimpleName(field.getOriginalFieldName()));
        return fieldAccess;
    }

}

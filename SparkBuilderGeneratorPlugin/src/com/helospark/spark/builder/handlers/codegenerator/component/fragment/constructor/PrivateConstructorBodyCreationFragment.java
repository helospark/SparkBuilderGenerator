package com.helospark.spark.builder.handlers.codegenerator.component.fragment.constructor;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.CamelCaseConverter;
import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

public class PrivateConstructorBodyCreationFragment {
    private CamelCaseConverter camelCaseConverter;

    public PrivateConstructorBodyCreationFragment(CamelCaseConverter camelCaseConverter) {
        this.camelCaseConverter = camelCaseConverter;
    }

    public Block createBody(AST ast, TypeDeclaration builderType, List<NamedVariableDeclarationField> namedVariableDeclarations) {
        Block body = ast.newBlock();
        for (NamedVariableDeclarationField field : namedVariableDeclarations) {
            Assignment assignment = ast.newAssignment();
            FieldAccess fieldAccess = ast.newFieldAccess();
            fieldAccess.setExpression(ast.newThisExpression());
            fieldAccess.setName(ast.newSimpleName(field.getOriginalFieldName()));
            assignment.setLeftHandSide(fieldAccess);

            FieldAccess builderFieldAccess = ast.newFieldAccess();
            builderFieldAccess.setExpression(ast.newSimpleName(camelCaseConverter.toLowerCamelCase(builderType.getName().toString())));
            builderFieldAccess.setName(ast.newSimpleName(field.getOriginalFieldName()));
            assignment.setRightHandSide(builderFieldAccess);

            body.statements().add(ast.newExpressionStatement(assignment));
        }
        return body;
    }
}

package com.helospark.spark.builder.handlers.codegenerator.component;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.ClassNameToVariableNameConverter;
import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

public class PrivateConstructorListRewritePopulator {
    private ClassNameToVariableNameConverter classNameToVariableNameConverter;

    public PrivateConstructorListRewritePopulator(ClassNameToVariableNameConverter classNameToVariableNameConverter) {
        this.classNameToVariableNameConverter = classNameToVariableNameConverter;
    }

    public void addPrivateConstructorToCompilationUnit(AST ast, TypeDeclaration originalType, TypeDeclaration builderType, ListRewrite listRewrite,
            List<NamedVariableDeclarationField> namedVariableDeclarations) {
        MethodDeclaration privateConstructor = createPrivateConstructor(ast, originalType, builderType, namedVariableDeclarations);
        FieldDeclaration[] fields = originalType.getFields();
        if (fields == null || fields.length == 0) {
            listRewrite.insertFirst(privateConstructor, null);
        } else {
            listRewrite.insertAfter(privateConstructor, fields[fields.length - 1], null);
        }
    }

    @SuppressWarnings("unchecked")
    private MethodDeclaration createPrivateConstructor(AST ast, TypeDeclaration originalType, TypeDeclaration builderType, List<NamedVariableDeclarationField> v) {
        Block body = ast.newBlock();
        for (NamedVariableDeclarationField field : v) {
            Assignment assignment = ast.newAssignment();
            FieldAccess fieldAccess = ast.newFieldAccess();
            fieldAccess.setExpression(ast.newThisExpression());
            fieldAccess.setName(ast.newSimpleName(field.getFieldName()));
            assignment.setLeftHandSide(fieldAccess);

            FieldAccess builderFieldAccess = ast.newFieldAccess();
            builderFieldAccess.setExpression(ast.newSimpleName(classNameToVariableNameConverter.convert(builderType.getName().toString())));
            builderFieldAccess.setName(ast.newSimpleName(field.getFieldName()));
            assignment.setRightHandSide(builderFieldAccess);

            body.statements().add(ast.newExpressionStatement(assignment));
        }

        MethodDeclaration method = ast.newMethodDeclaration();
        method.setConstructor(true);
        method.setName(ast.newSimpleName(originalType.getName().toString()));
        method.modifiers().add(ast.newModifier(ModifierKeyword.PRIVATE_KEYWORD));
        method.setBody(body);

        SingleVariableDeclaration methodParameterDeclaration = ast.newSingleVariableDeclaration();
        methodParameterDeclaration.setType(ast.newSimpleType(ast.newName(builderType.getName().toString())));
        methodParameterDeclaration.setName(ast.newSimpleName(classNameToVariableNameConverter.convert(builderType.getName().toString())));

        method.parameters().add(methodParameterDeclaration);
        return method;
    }
}

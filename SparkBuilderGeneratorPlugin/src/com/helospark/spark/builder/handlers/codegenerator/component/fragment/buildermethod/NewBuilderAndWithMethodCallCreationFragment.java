package com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class NewBuilderAndWithMethodCallCreationFragment {

    public Block createReturnBlock(AST ast, TypeDeclaration builderType, String withName, String parameterName) {
        Block builderMethodBlock = ast.newBlock();
        ReturnStatement returnStatement = ast.newReturnStatement();
        ClassInstanceCreation newClassInstanceCreation = ast.newClassInstanceCreation();
        newClassInstanceCreation.setType(ast.newSimpleType(ast.newName(builderType.getName().toString())));

        MethodInvocation withMethodInvocation = ast.newMethodInvocation();
        withMethodInvocation.setExpression(newClassInstanceCreation);
        withMethodInvocation.setName(ast.newSimpleName(withName));
        withMethodInvocation.arguments().add(ast.newSimpleName(parameterName));

        returnStatement.setExpression(withMethodInvocation);
        builderMethodBlock.statements().add(returnStatement);
        return builderMethodBlock;
    }

}

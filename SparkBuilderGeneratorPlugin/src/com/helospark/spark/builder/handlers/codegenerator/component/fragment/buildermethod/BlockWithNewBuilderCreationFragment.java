package com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class BlockWithNewBuilderCreationFragment {

    public Block createReturnBlock(AST ast, TypeDeclaration builderType) {
        Block builderMethodBlock = ast.newBlock();
        ReturnStatement returnStatement = ast.newReturnStatement();
        ClassInstanceCreation newClassInstanceCreation = ast.newClassInstanceCreation();
        newClassInstanceCreation.setType(ast.newSimpleType(ast.newName(builderType.getName().toString())));
        returnStatement.setExpression(newClassInstanceCreation);
        builderMethodBlock.statements().add(returnStatement);
        return builderMethodBlock;
    }

}

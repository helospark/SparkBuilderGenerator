package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.buildmethod;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class BuildMethodBodyCreator {

	public Block createBody(AST ast, TypeDeclaration originalType) {
		ClassInstanceCreation newClassInstanceCreation = ast.newClassInstanceCreation();
		newClassInstanceCreation.setType(ast.newSimpleType(ast.newName(originalType.getName().toString())));
		newClassInstanceCreation.arguments().add(ast.newThisExpression());

		ReturnStatement statement = ast.newReturnStatement();
		statement.setExpression(newClassInstanceCreation);

		Block block = ast.newBlock();
		block.statements().add(statement);
		return block;
	}
}

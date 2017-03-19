package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.buildmethod;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class BuildMethodAdderFragment {
	private BuildMethodDeclarationCreatorFragment buildMethodDeclarationCreatorFragment;
	private BuildMethodBodyCreator buildMethodBodyCreator;

	public BuildMethodAdderFragment(BuildMethodDeclarationCreatorFragment buildMethodDeclarationCreatorFragment,
			BuildMethodBodyCreator buildMethodBodyCreator) {
		this.buildMethodDeclarationCreatorFragment = buildMethodDeclarationCreatorFragment;
		this.buildMethodBodyCreator = buildMethodBodyCreator;
	}

	public void addBuildMethodToBuilder(AST ast, TypeDeclaration originalType, TypeDeclaration newType) {
		Block block = buildMethodBodyCreator.createBody(ast, originalType);
		MethodDeclaration method = buildMethodDeclarationCreatorFragment.createMethod(ast, originalType);
		method.setBody(block);
		newType.bodyDeclarations().add(method);
	}

}

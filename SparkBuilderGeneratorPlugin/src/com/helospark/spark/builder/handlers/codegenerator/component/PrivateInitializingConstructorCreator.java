package com.helospark.spark.builder.handlers.codegenerator.component;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import com.helospark.spark.builder.handlers.codegenerator.component.fragment.constructor.PrivateConstructorBodyCreationFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.constructor.PrivateConstructorInsertionFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.constructor.PrivateConstructorMethodDefinitionCreationFragment;
import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

public class PrivateInitializingConstructorCreator {
	private PrivateConstructorMethodDefinitionCreationFragment privateConstructorMethodDefinitionCreationFragment;
	private PrivateConstructorBodyCreationFragment privateConstructorBodyCreationFragment;
	private PrivateConstructorInsertionFragment privateConstructorInsertionFragment;

	public PrivateInitializingConstructorCreator(PrivateConstructorMethodDefinitionCreationFragment privateConstructorMethodDefinitionCreationFragment,
			PrivateConstructorBodyCreationFragment privateConstructorBodyCreationFragment, PrivateConstructorInsertionFragment privateConstructorInsertionFragment) {
		this.privateConstructorMethodDefinitionCreationFragment = privateConstructorMethodDefinitionCreationFragment;
		this.privateConstructorBodyCreationFragment = privateConstructorBodyCreationFragment;
		this.privateConstructorInsertionFragment = privateConstructorInsertionFragment;
	}

	public void addPrivateConstructorToCompilationUnit(AST ast, TypeDeclaration originalType, TypeDeclaration builderType, ListRewrite listRewrite,
			List<NamedVariableDeclarationField> namedVariableDeclarations) {
		Block body = privateConstructorBodyCreationFragment.createBody(ast, builderType, namedVariableDeclarations);
		MethodDeclaration privateConstructor = privateConstructorMethodDefinitionCreationFragment.createPrivateConstructor(ast, originalType, builderType,
				namedVariableDeclarations);
		privateConstructor.setBody(body);
		privateConstructorInsertionFragment.insertMethodToFirstPlace(originalType, listRewrite, privateConstructor);
	}

}

package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.withmethod;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.ADD_NONNULL_ON_RETURN;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.GENERATE_JAVADOC_ON_EACH_BUILDER_METHOD;
import static com.helospark.spark.builder.preferences.StaticPreferences.RETURN_JAVADOC_TAG_NAME;

import java.util.Collections;
import java.util.Locale;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.BuilderMethodNameBuilder;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.JavadocGenerator;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.NonNullAnnotationAttacher;
import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;
import com.helospark.spark.builder.preferences.PluginPreferenceList;
import com.helospark.spark.builder.preferences.PreferencesManager;

public class RegularBuilderWithMethodAdderFragment {
	private PreferencesManager preferencesManager;
	private JavadocGenerator javadocGenerator;
	private NonNullAnnotationAttacher nonNullAnnotationAttacher;
	private BuilderMethodNameBuilder builderClassMethodNameGeneratorService;

	public RegularBuilderWithMethodAdderFragment(PreferencesManager preferencesManager,
			JavadocGenerator javadocGenerator, NonNullAnnotationAttacher nonNullAnnotationAttacher,
			BuilderMethodNameBuilder builderClassMethodNameGeneratorService) {
		this.preferencesManager = preferencesManager;
		this.javadocGenerator = javadocGenerator;
		this.nonNullAnnotationAttacher = nonNullAnnotationAttacher;
		this.builderClassMethodNameGeneratorService = builderClassMethodNameGeneratorService;
	}

	public void addWithMethodToBuilder(AST ast, TypeDeclaration newType,
			NamedVariableDeclarationField namedVariableDeclarationField) {
		String originalFieldName = namedVariableDeclarationField.getOriginalFieldName();
		String builderFieldName = namedVariableDeclarationField.getBuilderFieldName();
		Block newBlock = createWithMethodBody(ast, originalFieldName, builderFieldName);
		SingleVariableDeclaration methodParameterDeclaration = createMethodParameter(ast,
				namedVariableDeclarationField.getFieldDeclaration(), builderFieldName);
		MethodDeclaration newMethod = createNewWithMethod(ast, builderFieldName, newBlock, methodParameterDeclaration,
				newType, namedVariableDeclarationField);
		newType.bodyDeclarations().add(newMethod);
	}

	private Block createWithMethodBody(AST ast, String originalFieldName, String builderFieldName) {
		Block newBlock = ast.newBlock();
		ReturnStatement builderReturnStatement = ast.newReturnStatement();
		builderReturnStatement.setExpression(ast.newThisExpression());

		Assignment newAssignment = ast.newAssignment();

		FieldAccess fieldAccess = ast.newFieldAccess();
		fieldAccess.setExpression(ast.newThisExpression());
		fieldAccess.setName(ast.newSimpleName(originalFieldName));
		newAssignment.setLeftHandSide(fieldAccess);
		newAssignment.setRightHandSide(ast.newSimpleName(builderFieldName));

		newBlock.statements().add(ast.newExpressionStatement(newAssignment));
		newBlock.statements().add(builderReturnStatement);
		return newBlock;
	}

	private MethodDeclaration createNewWithMethod(AST ast, String fieldName, Block newBlock,
			SingleVariableDeclaration methodParameterDeclaration, TypeDeclaration builderType,
			NamedVariableDeclarationField namedVariableDeclarationField) {
		MethodDeclaration builderMethod = ast.newMethodDeclaration();
		builderMethod.setName(ast.newSimpleName(builderClassMethodNameGeneratorService.build(fieldName)));
		builderMethod.setReturnType2(ast.newSimpleType(
				ast.newName(builderType.getName().getIdentifier())));
		builderMethod.setBody(newBlock);
		builderMethod.parameters().add(methodParameterDeclaration);

		if (preferencesManager.getPreferenceValue(GENERATE_JAVADOC_ON_EACH_BUILDER_METHOD)) {
			Javadoc javadoc = javadocGenerator.generateJavadoc(ast,
					String.format(Locale.ENGLISH, "Builder method for %s parameter.", fieldName),
					Collections.singletonMap(RETURN_JAVADOC_TAG_NAME, "builder"));
			builderMethod.setJavadoc(javadoc);
		}
		if (preferencesManager.getPreferenceValue(ADD_NONNULL_ON_RETURN)) {
			nonNullAnnotationAttacher.attachNonNull(ast, builderMethod);
		}
		builderMethod.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));

		return builderMethod;
	}

	private SingleVariableDeclaration createMethodParameter(AST ast, FieldDeclaration field, String fieldName) {
		SingleVariableDeclaration methodParameterDeclaration = ast.newSingleVariableDeclaration();
		methodParameterDeclaration.setType((Type) ASTNode.copySubtree(ast, field.getType()));
		methodParameterDeclaration.setName(ast.newSimpleName(fieldName));
		if (preferencesManager.getPreferenceValue(PluginPreferenceList.ADD_NONNULL_ON_PARAMETERS)) {
			nonNullAnnotationAttacher.attachNonNull(ast, methodParameterDeclaration);
		}
		return methodParameterDeclaration;
	}
}

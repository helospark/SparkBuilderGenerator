package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.buildmethod;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.ADD_NONNULL_ON_RETURN;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.BUILD_METHOD_NAME_PATTERN;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.GENERATE_JAVADOC_ON_EACH_BUILDER_METHOD;
import static com.helospark.spark.builder.preferences.StaticPreferences.RETURN_JAVADOC_TAG_NAME;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.JavadocGenerator;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.NonNullAnnotationAttacher;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TemplateResolver;
import com.helospark.spark.builder.preferences.PreferencesManager;

public class BuildMethodDeclarationCreatorFragment {
	private static final String CLASS_NAME_REPLACEMENT_PATTERN = "className";
	private PreferencesManager preferencesManager;
	private NonNullAnnotationAttacher nonNullAnnotationAttacher;
	private JavadocGenerator javadocGenerator;
	private TemplateResolver templateResolver;

	public BuildMethodDeclarationCreatorFragment(PreferencesManager preferencesManager,
			NonNullAnnotationAttacher nonNullAnnotationAttacher, JavadocGenerator javadocGenerator,
			TemplateResolver templateResolver) {
		this.preferencesManager = preferencesManager;
		this.nonNullAnnotationAttacher = nonNullAnnotationAttacher;
		this.javadocGenerator = javadocGenerator;
		this.templateResolver = templateResolver;
	}

	public MethodDeclaration createMethod(AST ast, TypeDeclaration originalType) {
		MethodDeclaration method = ast.newMethodDeclaration();
		method.setName(ast.newSimpleName(getBuildMethodName(originalType)));
		method.setReturnType2(ast.newSimpleType(ast.newName(originalType.getName().toString())));

		if (preferencesManager.getPreferenceValue(GENERATE_JAVADOC_ON_EACH_BUILDER_METHOD)) {
			Javadoc javadoc = javadocGenerator.generateJavadoc(ast, "Builder method of the builder.",
					Collections.singletonMap(RETURN_JAVADOC_TAG_NAME, "built class"));
			method.setJavadoc(javadoc);
		}
		if (preferencesManager.getPreferenceValue(ADD_NONNULL_ON_RETURN)) {
			nonNullAnnotationAttacher.attachNonNull(ast, method);
		}

		method.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
		return method;
	}

	private String getBuildMethodName(TypeDeclaration originalType) {
		Map<String, String> replacementMap = new HashMap<>();
		replacementMap.put(CLASS_NAME_REPLACEMENT_PATTERN, originalType.getName().toString());
		return templateResolver.resolveTemplate(preferencesManager.getPreferenceValue(BUILD_METHOD_NAME_PATTERN),
				replacementMap);
	}
}

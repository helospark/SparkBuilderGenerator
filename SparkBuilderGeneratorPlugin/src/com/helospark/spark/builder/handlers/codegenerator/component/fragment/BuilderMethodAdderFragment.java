package com.helospark.spark.builder.handlers.codegenerator.component.fragment;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.ADD_NONNULL_ON_RETURN;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.BUILD_METHOD_NAME_PATTERN;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.GENERATE_JAVADOC_ON_EACH_BUILDER_METHOD;
import static com.helospark.spark.builder.preferences.StaticPreferences.RETURN_JAVADOC_TAG_NAME;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.JavadocGenerator;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.NonNullAnnotationAttacher;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TemplateResolver;
import com.helospark.spark.builder.preferences.PreferencesManager;

public class BuilderMethodAdderFragment {
    private static final String CLASS_NAME_REPLACEMENT_PATTERN = "className";
    private PreferencesManager preferencesManager;
    private JavadocGenerator javadocGenerator;
    private NonNullAnnotationAttacher nonNullAnnotationAttacher;
    private TemplateResolver templateResolver;

    public BuilderMethodAdderFragment(PreferencesManager preferencesManager, JavadocGenerator javadocGenerator, NonNullAnnotationAttacher nonNullAnnotationAttacher,
            TemplateResolver templateResolver) {
        this.preferencesManager = preferencesManager;
        this.javadocGenerator = javadocGenerator;
        this.nonNullAnnotationAttacher = nonNullAnnotationAttacher;
        this.templateResolver = templateResolver;
    }

    public void addBuildMethodToBuilder(AST ast, TypeDeclaration typeDecl, TypeDeclaration newType) {
        MethodDeclaration buildMethod = createBuildMethod(ast, newType, typeDecl);
        newType.bodyDeclarations().add(buildMethod);
    }

    private MethodDeclaration createBuildMethod(AST ast, TypeDeclaration builderType, TypeDeclaration originalType) {
        ClassInstanceCreation newClassInstanceCreation = ast.newClassInstanceCreation();
        newClassInstanceCreation.setType(ast.newSimpleType(ast.newName(originalType.getName().toString())));
        newClassInstanceCreation.arguments().add(ast.newThisExpression());

        ReturnStatement statement = ast.newReturnStatement();
        statement.setExpression(newClassInstanceCreation);

        Block block = ast.newBlock();
        block.statements().add(statement);

        MethodDeclaration method = ast.newMethodDeclaration();
        method.setName(ast.newSimpleName(getBuildMethodName(originalType)));
        method.setBody(block);
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
        return templateResolver.resolveTemplate(preferencesManager.getPreferenceValue(BUILD_METHOD_NAME_PATTERN), replacementMap);
    }

}

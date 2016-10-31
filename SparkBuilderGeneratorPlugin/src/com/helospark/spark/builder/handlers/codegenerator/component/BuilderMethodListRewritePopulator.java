package com.helospark.spark.builder.handlers.codegenerator.component;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.ADD_GENERATED_ANNOTATION;
import static com.helospark.spark.builder.preferences.StaticPreferences.RETURN_JAVADOC_TAG_NAME;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.GeneratedAnnotationPopulator;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.JavadocGenerator;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TemplateResolver;
import com.helospark.spark.builder.preferences.PluginPreferenceList;
import com.helospark.spark.builder.preferences.PreferencesManager;

public class BuilderMethodListRewritePopulator {
    private TemplateResolver templateResolver;
    private PreferencesManager preferenceManager;
    private JavadocGenerator javadocGenerator;
    private GeneratedAnnotationPopulator generatedAnnotationPopulator;
    private PreferencesManager preferencesManager;

    public BuilderMethodListRewritePopulator(TemplateResolver templateResolver, PreferencesManager preferenceManager, JavadocGenerator javadocGenerator,
            GeneratedAnnotationPopulator generatedAnnotationPopulator, PreferencesManager preferencesManager) {
        this.templateResolver = templateResolver;
        this.preferenceManager = preferenceManager;
        this.javadocGenerator = javadocGenerator;
        this.generatedAnnotationPopulator = generatedAnnotationPopulator;
        this.preferencesManager = preferencesManager;
    }

    public void addBuilderMethodToCompilationUnit(AST ast, ListRewrite listRewrite, TypeDeclaration typeDeclaration, TypeDeclaration builderType) {
        MethodDeclaration builderBuilderMethod = createBuilderMethod(ast, typeDeclaration, builderType);
        listRewrite.insertLast(builderBuilderMethod, null);
    }

    @SuppressWarnings("unchecked")
    private MethodDeclaration createBuilderMethod(AST ast, TypeDeclaration originalType, TypeDeclaration builderType) {
        Block builderMethodBlock = createReturnBlock(ast, builderType);
        MethodDeclaration builderMethod = ast.newMethodDeclaration();
        builderMethod.setName(ast.newSimpleName(getBuilderMethodName(originalType)));
        builderMethod.setBody(builderMethodBlock);
        addGenerateAnnotationIfNeeded(ast, builderMethod);
        builderMethod.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
        builderMethod.modifiers().add(ast.newModifier(ModifierKeyword.STATIC_KEYWORD));
        builderMethod.setReturnType2(ast.newSimpleType(ast.newName(builderType.getName().toString())));

        if (preferenceManager.getPreferenceValue(PluginPreferenceList.GENERATE_JAVADOC_ON_BUILDER_METHOD)) {
            Javadoc javadoc = javadocGenerator.generateJavadoc(ast, String.format(Locale.ENGLISH, "Creates builder to build {@link %s}.", originalType.getName().toString()),
                    Collections.singletonMap(RETURN_JAVADOC_TAG_NAME, "created builder"));
            builderMethod.setJavadoc(javadoc);
        }

        return builderMethod;
    }

    private void addGenerateAnnotationIfNeeded(AST ast, MethodDeclaration builderMethod) {
        if (preferencesManager.getPreferenceValue(ADD_GENERATED_ANNOTATION)) {
            generatedAnnotationPopulator.addGeneratedAnnotation(ast, builderMethod);
        }
    }

    private Block createReturnBlock(AST ast, TypeDeclaration builderType) {
        Block builderMethodBlock = ast.newBlock();
        ReturnStatement returnStatement = ast.newReturnStatement();
        ClassInstanceCreation newClassInstanceCreation = ast.newClassInstanceCreation();
        newClassInstanceCreation.setType(ast.newSimpleType(ast.newName(builderType.getName().toString())));
        returnStatement.setExpression(newClassInstanceCreation);
        builderMethodBlock.statements().add(returnStatement);
        return builderMethodBlock;
    }

    private String getBuilderMethodName(TypeDeclaration originalType) {
        Map<String, String> replacements = new HashMap<>();
        replacements.put("className", originalType.getName().toString());
        return templateResolver.resolveTemplate(preferenceManager.getPreferenceValue(PluginPreferenceList.CREATE_BUILDER_METHOD_PATTERN), replacements);
    }

}

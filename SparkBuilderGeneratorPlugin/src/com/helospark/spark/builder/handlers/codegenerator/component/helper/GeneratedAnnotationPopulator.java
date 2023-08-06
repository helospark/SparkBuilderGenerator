package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.STAGED_BUILDER_ADD_GENERATED_ANNOTATION_ON_STAGE_INTERFACE;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;

import com.helospark.spark.builder.preferences.PreferencesManager;
import com.helospark.spark.builder.preferences.StaticPreferences;

/**
 * Adds generated annotation.
 *
 * @author helospark
 */
public class GeneratedAnnotationPopulator {
    private PreferencesManager preferencesManager;

    public GeneratedAnnotationPopulator(PreferencesManager preferencesManager) {
        this.preferencesManager = preferencesManager;
    }

    public void addGeneratedAnnotationOnStagedBuilderInterface(AST ast, AbstractTypeDeclaration type) {
        if (preferencesManager.getPreferenceValue(STAGED_BUILDER_ADD_GENERATED_ANNOTATION_ON_STAGE_INTERFACE)) {
            addGeneratedAnnotation(ast, type);
        }
    }

    public void addGeneratedAnnotation(AST ast, AbstractTypeDeclaration builderType) {
        SingleMemberAnnotation generatedAnnotation = createGeneratedAnnotation(ast);
        builderType.modifiers().add(0, generatedAnnotation);
    }

    public void addGeneratedAnnotation(AST ast, MethodDeclaration methodDeclaration) {
        SingleMemberAnnotation generatedAnnotation = createGeneratedAnnotation(ast);
        methodDeclaration.modifiers().add(0, generatedAnnotation);
    }

    private SingleMemberAnnotation createGeneratedAnnotation(AST ast) {
        SingleMemberAnnotation generatedAnnotation = ast.newSingleMemberAnnotation();
        generatedAnnotation.setTypeName(ast.newSimpleName("Generated"));
        StringLiteral annotationValue = ast.newStringLiteral();
        annotationValue.setLiteralValue(StaticPreferences.PLUGIN_GENERATED_ANNOTATION_NAME);
        generatedAnnotation.setValue(annotationValue);
        return generatedAnnotation;
    }
}

package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.preferences.StaticPreferences;

/**
 * Adds generated annotation.
 * 
 * @author helospark
 */
public class GeneratedAnnotationPopulator {
    public void addGeneratedAnnotation(AST ast, TypeDeclaration builderType) {
        SingleMemberAnnotation generatedAnnotation = createGeneratedAnnotation(ast);
        builderType.modifiers().add(generatedAnnotation);
    }

    public void addGeneratedAnnotation(AST ast, MethodDeclaration methodDeclaration) {
        SingleMemberAnnotation generatedAnnotation = createGeneratedAnnotation(ast);
        methodDeclaration.modifiers().add(generatedAnnotation);
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

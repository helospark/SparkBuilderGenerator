package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

/**
 * Adds Nonnull annotation.
 *
 * @author helospark
 */
public class MarkerAnnotationAttacher {
    public static final String NONNUL_ANNOTATION = "Nonnull";
    public static final String OVERRIDE_ANNOTATION = "Override";

    public void attachNonNull(AST ast, MethodDeclaration method) {
        attachAnnotation(ast, method, NONNUL_ANNOTATION);
    }

    public void attachNonNull(AST ast, SingleVariableDeclaration methodParameterDeclaration) {
        attachAnnotation(ast, methodParameterDeclaration, NONNUL_ANNOTATION);
    }

    public void attachAnnotation(AST ast, MethodDeclaration method, String annotationName) {
        MarkerAnnotation nonNullAnnotation = createMarkerAnnotation(ast, annotationName);
        method.modifiers().add(0, nonNullAnnotation);
    }

    public void attachAnnotation(AST ast, SingleVariableDeclaration methodParameterDeclaration, String annotationName) {
        MarkerAnnotation nonNullAnnotation = createMarkerAnnotation(ast, annotationName);
        methodParameterDeclaration.modifiers().add(0, nonNullAnnotation);
    }

    private MarkerAnnotation createMarkerAnnotation(AST ast, String annotation) {
        MarkerAnnotation markerAnnotation = ast.newMarkerAnnotation();
        markerAnnotation.setTypeName(ast.newSimpleName(annotation));
        return markerAnnotation;
    }
}

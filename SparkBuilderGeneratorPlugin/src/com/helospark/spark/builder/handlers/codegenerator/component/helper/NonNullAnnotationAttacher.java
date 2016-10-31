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
public class NonNullAnnotationAttacher {

    public void attachNonNull(AST ast, MethodDeclaration method) {
        MarkerAnnotation nonNullAnnotation = createNonnullAnnotation(ast);
        method.modifiers().add(nonNullAnnotation);
    }

    public void attachNonNull(AST ast, SingleVariableDeclaration methodParameterDeclaration) {
        MarkerAnnotation nonNullAnnotation = createNonnullAnnotation(ast);
        methodParameterDeclaration.modifiers().add(nonNullAnnotation);
    }

    private MarkerAnnotation createNonnullAnnotation(AST ast) {
        MarkerAnnotation nonNullAnnotation = ast.newMarkerAnnotation();
        nonNullAnnotation.setTypeName(ast.newSimpleName("NonNull"));
        return nonNullAnnotation;
    }
}

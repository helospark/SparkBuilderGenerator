package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

/**
 * Adds Nonnull annotation.
 * 
 * @author helospark
 */
public class NonNullAnnotationAttacher {

    public void attachNonNull(AST ast, MethodDeclaration method) {
        NormalAnnotation nonNullAnnotation = ast.newNormalAnnotation();
        nonNullAnnotation.setTypeName(ast.newSimpleName("Nonnull"));
        method.modifiers().add(nonNullAnnotation);
    }

    public void attachNonNull(AST ast, SingleVariableDeclaration methodParameterDeclaration) {
        NormalAnnotation nonNullAnnotation = ast.newNormalAnnotation();
        nonNullAnnotation.setTypeName(ast.newSimpleName("NonNull"));
        methodParameterDeclaration.modifiers().add(nonNullAnnotation);
    }
}

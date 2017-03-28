package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import java.util.Optional;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Extracts IType from the given TypeDeclaration.
 * @author helospark
 */
public class ITypeExtractor {

    public Optional<IType> extract(TypeDeclaration typeDeclaration) {
        return Optional.ofNullable(typeDeclaration.resolveBinding())
                .map(binding -> binding.getSuperclass())
                .map(superclass -> superclass.getJavaElement())
                .filter(javaElement -> javaElement instanceof IType)
                .map(javaElement -> (IType) javaElement);
    }
}

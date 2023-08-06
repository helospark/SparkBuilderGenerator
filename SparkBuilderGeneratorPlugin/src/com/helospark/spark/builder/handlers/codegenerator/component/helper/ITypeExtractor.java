package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import java.util.Optional;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;

/**
 * Extracts IType from the given AbstractTypeDeclaration.
 * @author helospark
 */
public class ITypeExtractor {

    public Optional<IType> extract(AbstractTypeDeclaration typeDeclaration) {
        return Optional.ofNullable(typeDeclaration.resolveBinding())
                .map(binding -> binding.getSuperclass())
                .map(superclass -> superclass.getJavaElement())
                .filter(javaElement -> javaElement instanceof IType)
                .map(javaElement -> (IType) javaElement);
    }
}

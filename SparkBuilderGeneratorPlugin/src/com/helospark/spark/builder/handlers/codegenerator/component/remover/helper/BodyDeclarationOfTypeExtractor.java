package com.helospark.spark.builder.handlers.codegenerator.component.remover.helper;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Extracts body declarations that has the same type as the given class.
 * @author helospark
 */
public class BodyDeclarationOfTypeExtractor {

    public <T extends BodyDeclaration> List<T> extractBodyDeclaration(TypeDeclaration typeDeclaration, Class<T> expectedReturnType) {
        return ((List<BodyDeclaration>) typeDeclaration.bodyDeclarations())
                .stream()
                .filter(value -> value.getClass().equals(expectedReturnType))
                .map(value -> (T) value)
                .collect(Collectors.toList());
    }

}

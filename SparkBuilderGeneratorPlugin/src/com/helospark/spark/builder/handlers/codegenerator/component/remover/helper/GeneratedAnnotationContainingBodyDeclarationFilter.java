package com.helospark.spark.builder.handlers.codegenerator.component.remover.helper;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.BodyDeclaration;

/**
 * Filters the given list and return only those body declarations that contain Generated annotation.
 * @author helospark
 */
public class GeneratedAnnotationContainingBodyDeclarationFilter {
    private GeneratedAnnotationPredicate generatedAnnotationPredicate;

    public GeneratedAnnotationContainingBodyDeclarationFilter(GeneratedAnnotationPredicate generatedAnnotationPredicate) {
        this.generatedAnnotationPredicate = generatedAnnotationPredicate;
    }

    public <T extends BodyDeclaration> List<T> filterAnnotatedClasses(List<T> bodyDeclarations) {
        return bodyDeclarations
                .stream()
                .filter(generatedAnnotationPredicate)
                .collect(Collectors.toList());
    }

}

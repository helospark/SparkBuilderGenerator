package com.helospark.spark.builder.handlers.codegenerator.component.remover.helper;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.BodyDeclaration;

public class AnnotatedBodyDeclarationFilter {
    private GeneratedAnnotationPredicate generatedAnnotationPredicate;

    public AnnotatedBodyDeclarationFilter(GeneratedAnnotationPredicate generatedAnnotationPredicate) {
        this.generatedAnnotationPredicate = generatedAnnotationPredicate;
    }

    public <T extends BodyDeclaration> List<T> filterAnnotatedClasses(List<T> bodyDeclarations) {
        return bodyDeclarations
                .stream()
                .filter(generatedAnnotationPredicate)
                .collect(Collectors.toList());
    }

}

package com.helospark.spark.builder.handlers.codegenerator.component.remover;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.AnnotatedBodyDeclarationFilter;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.BodyDeclarationOfTypeExtractor;

public class BuilderClassRemover implements BuilderRemoverChainItem {
    private BodyDeclarationOfTypeExtractor bodyDeclarationOfTypeExtractor;
    private AnnotatedBodyDeclarationFilter annotatedBodyDeclarationFilter;

    public BuilderClassRemover(BodyDeclarationOfTypeExtractor bodyDeclarationOfTypeExtractor, AnnotatedBodyDeclarationFilter annotatedBodyDeclarationFilter) {
        this.bodyDeclarationOfTypeExtractor = bodyDeclarationOfTypeExtractor;
        this.annotatedBodyDeclarationFilter = annotatedBodyDeclarationFilter;
    }

    @Override
    public void remove(ASTRewrite rewriter, TypeDeclaration rootType) {
        List<TypeDeclaration> nestedTypes = getNestedTypes(rootType);
        List<TypeDeclaration> generatedAnnotationAnnotatedClasses = annotatedBodyDeclarationFilter.filterAnnotatedClasses(nestedTypes);

        if (generatedAnnotationAnnotatedClasses.size() > 0) {
            generatedAnnotationAnnotatedClasses.forEach(clazz -> rewriter.remove(clazz, null));
        } else {
            if (nestedTypes.size() == 1) {
                rewriter.remove(nestedTypes.get(0), null);
            }
        }

    }

    public List<TypeDeclaration> getNestedTypes(TypeDeclaration rootType) {
        return bodyDeclarationOfTypeExtractor.extractBodyDeclaration(rootType, TypeDeclaration.class)
                .stream()
                .filter(type -> !type.isInterface())
                .collect(Collectors.toList());
    }
}
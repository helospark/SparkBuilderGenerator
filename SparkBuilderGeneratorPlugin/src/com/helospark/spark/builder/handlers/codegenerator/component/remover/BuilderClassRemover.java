package com.helospark.spark.builder.handlers.codegenerator.component.remover;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.GeneratedAnnotationContainingBodyDeclarationFilter;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.BodyDeclarationOfTypeExtractor;

/**
 * Removes the main builder class.
 * @author helospark
 */
public class BuilderClassRemover implements BuilderRemoverChainItem {
    private BodyDeclarationOfTypeExtractor bodyDeclarationOfTypeExtractor;
    private GeneratedAnnotationContainingBodyDeclarationFilter generatedAnnotationContainingBodyDeclarationFilter;

    public BuilderClassRemover(BodyDeclarationOfTypeExtractor bodyDeclarationOfTypeExtractor, GeneratedAnnotationContainingBodyDeclarationFilter generatedAnnotationContainingBodyDeclarationFilter) {
        this.bodyDeclarationOfTypeExtractor = bodyDeclarationOfTypeExtractor;
        this.generatedAnnotationContainingBodyDeclarationFilter = generatedAnnotationContainingBodyDeclarationFilter;
    }

    @Override
    public void remove(ASTRewrite rewriter, TypeDeclaration rootType) {
        List<TypeDeclaration> nestedTypes = getNestedTypes(rootType);
        List<TypeDeclaration> generatedAnnotationAnnotatedClasses = generatedAnnotationContainingBodyDeclarationFilter.filterAnnotatedClasses(nestedTypes);

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
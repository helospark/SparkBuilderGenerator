package com.helospark.spark.builder.handlers.codegenerator.component.remover;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.AnnotatedBodyDeclarationFilter;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.IsPrivatePredicate;

public class PrivateConstructorRemover implements BuilderRemoverChainItem {
    private IsPrivatePredicate isPrivatePredicate;
    private AnnotatedBodyDeclarationFilter annotatedBodyDeclarationFilter;

    public PrivateConstructorRemover(IsPrivatePredicate isPrivatePredicate,
            AnnotatedBodyDeclarationFilter annotatedBodyDeclarationFilter) {
        this.isPrivatePredicate = isPrivatePredicate;
        this.annotatedBodyDeclarationFilter = annotatedBodyDeclarationFilter;
    }

    @Override
    public void remove(ASTRewrite rewriter, TypeDeclaration mainType) {
        List<MethodDeclaration> privateConstructors = extractPrivateConstructors(mainType);
        List<MethodDeclaration> annotatedConstructors = annotatedBodyDeclarationFilter.filterAnnotatedClasses(privateConstructors);
        if (!annotatedConstructors.isEmpty()) {
            annotatedConstructors.stream()
                    .forEach(constructor -> rewriter.remove(constructor, null));
        } else {
            privateConstructors.stream()
                    .findFirst()
                    .ifPresent(constructor -> rewriter.remove(constructor, null));
        }
    }

    private List<MethodDeclaration> extractPrivateConstructors(TypeDeclaration mainType) {
        return Arrays.stream(mainType.getMethods())
                .filter(method -> method.isConstructor())
                .filter(method -> method.parameters().size() == 1)
                .filter(isPrivatePredicate)
                .collect(Collectors.toList());
    }

}

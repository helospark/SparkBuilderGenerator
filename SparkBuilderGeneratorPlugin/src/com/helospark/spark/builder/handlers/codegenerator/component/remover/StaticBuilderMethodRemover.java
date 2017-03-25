package com.helospark.spark.builder.handlers.codegenerator.component.remover;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.AnnotatedBodyDeclarationFilter;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.IsPublicPredicate;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.IsStaticPredicate;

public class StaticBuilderMethodRemover implements BuilderRemoverChainItem {
    private IsStaticPredicate isStaticPredicate;
    private IsPublicPredicate isPublicPredicate;
    private AnnotatedBodyDeclarationFilter annotatedBodyDeclarationFilter;

    public StaticBuilderMethodRemover(IsStaticPredicate isStaticPredicate, IsPublicPredicate isPublicPredicate, AnnotatedBodyDeclarationFilter annotatedBodyDeclarationFilter) {
        this.isStaticPredicate = isStaticPredicate;
        this.isPublicPredicate = isPublicPredicate;
        this.annotatedBodyDeclarationFilter = annotatedBodyDeclarationFilter;
    }

    @Override
    public void remove(ASTRewrite rewriter, TypeDeclaration mainType) {
        List<MethodDeclaration> publicStaticMethods = extractPublicStaticMethods(mainType);
        List<MethodDeclaration> annotatedMethods = annotatedBodyDeclarationFilter.filterAnnotatedClasses(publicStaticMethods);
        if (!annotatedMethods.isEmpty()) {
            annotatedMethods.stream()
                    .forEach(method -> rewriter.remove(method, null));
        } else {
            publicStaticMethods.stream()
                    .filter(method -> method.parameters().size() == 0 || method.parameters().size() == 1)
                    .filter(method -> !method.getReturnType2().isPrimitiveType())
                    .findFirst()
                    .ifPresent(method -> rewriter.remove(method, null));
        }
    }

    private List<MethodDeclaration> extractPublicStaticMethods(TypeDeclaration mainType) {
        return Arrays.stream(mainType.getMethods())
                .filter(isStaticPredicate)
                .filter(isPublicPredicate)
                .collect(Collectors.toList());
    }

    private boolean isPublic(MethodDeclaration method) {
        return ((List<IExtendedModifier>) method.modifiers())
                .stream()
                .filter(modifier -> modifier instanceof Modifier)
                .filter(modifier -> ((Modifier) modifier).equals(Modifier.PUBLIC))
                .findFirst()
                .isPresent();
    }

}

package com.helospark.spark.builder.handlers.codegenerator.component.remover;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import com.helospark.spark.builder.PluginLogger;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.GeneratedAnnotationContainingBodyDeclarationFilter;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.IsPublicPredicate;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.IsStaticPredicate;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;

/**
 * Removes the previously generated static builder creation method and copy instance builder methods.
 * @author helospark
 */
public class StaticBuilderMethodRemover implements BuilderRemoverChainItem {
    private final PluginLogger pluginLogger = new PluginLogger();
    private IsStaticPredicate isStaticPredicate;
    private IsPublicPredicate isPublicPredicate;
    private GeneratedAnnotationContainingBodyDeclarationFilter generatedAnnotationContainingBodyDeclarationFilter;

    public StaticBuilderMethodRemover(IsStaticPredicate isStaticPredicate, IsPublicPredicate isPublicPredicate,
            GeneratedAnnotationContainingBodyDeclarationFilter generatedAnnotationContainingBodyDeclarationFilter) {
        this.isStaticPredicate = isStaticPredicate;
        this.isPublicPredicate = isPublicPredicate;
        this.generatedAnnotationContainingBodyDeclarationFilter = generatedAnnotationContainingBodyDeclarationFilter;
    }

    @Override
    public void remove(ASTRewrite rewriter, TypeDeclaration mainType, CompilationUnitModificationDomain modificationDomain) {
        List<MethodDeclaration> publicStaticMethods = extractPublicStaticMethods(mainType);
        List<MethodDeclaration> annotatedMethods = generatedAnnotationContainingBodyDeclarationFilter.filterAnnotatedClasses(publicStaticMethods);
        if (!annotatedMethods.isEmpty()) {
            removeMethodsWithGeneratedAnnotations(rewriter, annotatedMethods);
        } else {
            tryRemovingBasedOnSignature(rewriter, publicStaticMethods);
        }
    }

    private void removeMethodsWithGeneratedAnnotations(ASTRewrite rewriter, List<MethodDeclaration> annotatedMethods) {
        annotatedMethods.stream()
                .forEach(method -> rewriter.remove(method, null));
    }

    private void tryRemovingBasedOnSignature(ASTRewrite rewriter, List<MethodDeclaration> publicStaticMethods) {
        List<MethodDeclaration> foundMethods = publicStaticMethods.stream()
                .filter(method -> method.parameters().size() == 0 || method.parameters().size() == 1)
                .filter(method -> !method.getReturnType2().isPrimitiveType())
                .collect(Collectors.toList());
        if (foundMethods.size() > 2) {
            pluginLogger.warn("More than 2 methods matching static builder creator found, removing first two. Consider enable @Generated annotation creation for better matching");
        }
        foundMethods.stream()
                .limit(2)
                .forEach(method -> rewriter.remove(method, null));
    }

    private List<MethodDeclaration> extractPublicStaticMethods(TypeDeclaration mainType) {
        return Arrays.stream(mainType.getMethods())
                .filter(isStaticPredicate)
                .filter(isPublicPredicate)
                .collect(Collectors.toList());
    }

}

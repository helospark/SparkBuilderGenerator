package com.helospark.spark.builder.handlers.codegenerator.component.remover;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.BodyDeclarationOfTypeExtractor;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.GeneratedAnnotationContainingBodyDeclarationFilter;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.IsPrivatePredicate;

/**
 * Removes the main builder class.
 * @author helospark
 */
public class BuilderClassRemover implements BuilderRemoverChainItem {
    private BodyDeclarationOfTypeExtractor bodyDeclarationOfTypeExtractor;
    private GeneratedAnnotationContainingBodyDeclarationFilter generatedAnnotationContainingBodyDeclarationFilter;
    private IsPrivatePredicate isPrivatePredicate;

    public BuilderClassRemover(BodyDeclarationOfTypeExtractor bodyDeclarationOfTypeExtractor,
            GeneratedAnnotationContainingBodyDeclarationFilter generatedAnnotationContainingBodyDeclarationFilter,
            IsPrivatePredicate isPrivatePredicate) {
        this.bodyDeclarationOfTypeExtractor = bodyDeclarationOfTypeExtractor;
        this.generatedAnnotationContainingBodyDeclarationFilter = generatedAnnotationContainingBodyDeclarationFilter;
        this.isPrivatePredicate = isPrivatePredicate;
    }

    @Override
    public void remove(ASTRewrite rewriter, TypeDeclaration rootType) {
        List<TypeDeclaration> nestedTypes = getNestedTypes(rootType);
        List<TypeDeclaration> generatedAnnotationAnnotatedClasses = generatedAnnotationContainingBodyDeclarationFilter.filterAnnotatedClasses(nestedTypes);

        if (generatedAnnotationAnnotatedClasses.size() > 0) {
            generatedAnnotationAnnotatedClasses.forEach(clazz -> rewriter.remove(clazz, null));
        } else {
            nestedTypes.stream()
                    .filter(this::isTypeLooksLikeABuilder)
                    .findFirst()
                    .ifPresent(nestedType -> rewriter.remove(nestedType, null));
        }

    }

    private boolean isTypeLooksLikeABuilder(TypeDeclaration nestedType) {
        if (nestedType.getTypes().length > 0) {
            return false;
        }
        if (nestedType.getMethods().length < 2) {
            return false;
        }
        if (getNumberOfEmptyPrivateConstructors(nestedType) != 1) {
            return false;
        }
        return true;
    }

    private int getNumberOfEmptyPrivateConstructors(TypeDeclaration nestedType) {
        return Arrays.stream(nestedType.getMethods())
                .filter(method -> method.isConstructor())
                .filter(method -> method.parameters().size() == 0)
                .filter(isPrivatePredicate)
                .collect(Collectors.toList())
                .size();
    }

    public List<TypeDeclaration> getNestedTypes(TypeDeclaration rootType) {
        return bodyDeclarationOfTypeExtractor.extractBodyDeclaration(rootType, TypeDeclaration.class)
                .stream()
                .filter(type -> !type.isInterface())
                .collect(Collectors.toList());
    }
}
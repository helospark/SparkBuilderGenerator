package com.helospark.spark.builder.handlers.codegenerator.component.remover;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.GeneratedAnnotationContainingBodyDeclarationFilter;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.BodyDeclarationOfTypeExtractor;

/**
 * Removes the previously generates stage interfaces.
 * @author helospark
 */
public class StagedBuilderInterfaceRemover implements BuilderRemoverChainItem {
    private BodyDeclarationOfTypeExtractor bodyDeclarationOfTypeExtractor;
    private GeneratedAnnotationContainingBodyDeclarationFilter generatedAnnotationContainingBodyDeclarationFilter;

    public StagedBuilderInterfaceRemover(BodyDeclarationOfTypeExtractor bodyDeclarationOfTypeExtractor, GeneratedAnnotationContainingBodyDeclarationFilter generatedAnnotationContainingBodyDeclarationFilter) {
        this.bodyDeclarationOfTypeExtractor = bodyDeclarationOfTypeExtractor;
        this.generatedAnnotationContainingBodyDeclarationFilter = generatedAnnotationContainingBodyDeclarationFilter;
    }

    @Override
    public void remove(ASTRewrite rewriter, TypeDeclaration mainType) {
        List<TypeDeclaration> nestedInterfaces = getNestedInterfaces(mainType);
        List<TypeDeclaration> interfacesWithGeneratedAnnotation = generatedAnnotationContainingBodyDeclarationFilter.filterAnnotatedClasses(nestedInterfaces);

        if (!interfacesWithGeneratedAnnotation.isEmpty()) {
            interfacesWithGeneratedAnnotation.stream()
                    .forEach(interfaceToRemove -> rewriter.remove(interfaceToRemove, null));
        } else {
            fallbackFilterMatchingInterfaces(nestedInterfaces, mainType)
                    .forEach(interfaceToRemove -> rewriter.remove(interfaceToRemove, null));
        }

    }

    private List<TypeDeclaration> fallbackFilterMatchingInterfaces(List<TypeDeclaration> nestedInterfaces, TypeDeclaration mainType) {
        return nestedInterfaces.stream()
                .filter(interfaceToCheck -> isPossibleStagedBuilderInterface(interfaceToCheck, nestedInterfaces, mainType))
                .collect(Collectors.toList());
    }

    private boolean isPossibleStagedBuilderInterface(TypeDeclaration interfaceToCheck, List<TypeDeclaration> nestedInterfaces, TypeDeclaration mainType) {
        return Arrays.stream(interfaceToCheck.getMethods())
                .allMatch(method -> isMethodPossibleInStagedBuilder(method, nestedInterfaces, mainType));
    }

    private boolean isMethodPossibleInStagedBuilder(MethodDeclaration method, List<TypeDeclaration> nestedInterfaces, TypeDeclaration mainType) {
        if (method.parameters().size() == 1) {
            return isReturningReferenceToAnotherInterface(method, nestedInterfaces);
        } else if (method.parameters().size() == 0) {
            return isReturningType(method, mainType);
        } else {
            return false;
        }
    }

    private boolean isReturningReferenceToAnotherInterface(MethodDeclaration method, List<TypeDeclaration> nestedInterfaces) {
        return nestedInterfaces.stream()
                .filter(interfaceToCheck -> isReturningType(method, interfaceToCheck))
                .findFirst()
                .isPresent();
    }

    private boolean isReturningType(MethodDeclaration method, TypeDeclaration mainType) {
        return method.getReturnType2()
                .toString()
                .equals(getTypeName(mainType));
    }

    private String getTypeName(TypeDeclaration builderTypeDeclaration) {
        return builderTypeDeclaration.getName().toString();
    }

    private List<TypeDeclaration> getNestedInterfaces(TypeDeclaration mainType) {
        return bodyDeclarationOfTypeExtractor.extractBodyDeclaration(mainType, TypeDeclaration.class)
                .stream()
                .filter(type -> type.isInterface())
                .collect(Collectors.toList());
    }

}

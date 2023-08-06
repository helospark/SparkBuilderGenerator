package com.helospark.spark.builder.handlers.codegenerator.component.remover;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.MethodExtractor;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.BodyDeclarationOfTypeExtractor;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.GeneratedAnnotationContainingBodyDeclarationFilter;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;

/**
 * Removes the previously generates stage interfaces.
 * @author helospark
 */
public class StagedBuilderInterfaceRemover implements BuilderRemoverChainItem {
    private BodyDeclarationOfTypeExtractor bodyDeclarationOfTypeExtractor;
    private GeneratedAnnotationContainingBodyDeclarationFilter generatedAnnotationContainingBodyDeclarationFilter;

    public StagedBuilderInterfaceRemover(BodyDeclarationOfTypeExtractor bodyDeclarationOfTypeExtractor,
            GeneratedAnnotationContainingBodyDeclarationFilter generatedAnnotationContainingBodyDeclarationFilter) {
        this.bodyDeclarationOfTypeExtractor = bodyDeclarationOfTypeExtractor;
        this.generatedAnnotationContainingBodyDeclarationFilter = generatedAnnotationContainingBodyDeclarationFilter;
    }

    @Override
    public void remove(ASTRewrite rewriter, AbstractTypeDeclaration mainType, CompilationUnitModificationDomain modificationDomain) {
        List<AbstractTypeDeclaration> nestedInterfaces = getNestedInterfaces(mainType);
        List<AbstractTypeDeclaration> interfacesWithGeneratedAnnotation = generatedAnnotationContainingBodyDeclarationFilter.filterAnnotatedClasses(nestedInterfaces);

        if (!interfacesWithGeneratedAnnotation.isEmpty()) {
            interfacesWithGeneratedAnnotation.stream()
                    .forEach(interfaceToRemove -> rewriter.remove(interfaceToRemove, null));
        } else {
            fallbackFilterMatchingInterfaces(nestedInterfaces, mainType)
                    .forEach(interfaceToRemove -> rewriter.remove(interfaceToRemove, null));
        }

    }

    private List<AbstractTypeDeclaration> fallbackFilterMatchingInterfaces(List<AbstractTypeDeclaration> nestedInterfaces, AbstractTypeDeclaration mainType) {
        return nestedInterfaces.stream()
                .filter(interfaceToCheck -> isPossibleStagedBuilderInterface(interfaceToCheck, nestedInterfaces, mainType))
                .collect(Collectors.toList());
    }

    private boolean isPossibleStagedBuilderInterface(AbstractTypeDeclaration interfaceToCheck, List<AbstractTypeDeclaration> nestedInterfaces, AbstractTypeDeclaration mainType) {
        return Arrays.stream(MethodExtractor.getMethods(interfaceToCheck))
                .allMatch(method -> isMethodPossibleInStagedBuilder(method, nestedInterfaces, mainType));
    }

    private boolean isMethodPossibleInStagedBuilder(MethodDeclaration method, List<AbstractTypeDeclaration> nestedInterfaces, AbstractTypeDeclaration mainType) {
        if (method.parameters().size() == 1) {
            return isReturningReferenceToAnotherInterface(method, nestedInterfaces);
        } else if (method.parameters().size() == 0) {
            return isReturningType(method, mainType);
        } else {
            return false;
        }
    }

    private boolean isReturningReferenceToAnotherInterface(MethodDeclaration method, List<AbstractTypeDeclaration> nestedInterfaces) {
        return nestedInterfaces.stream()
                .filter(interfaceToCheck -> isReturningType(method, interfaceToCheck))
                .findFirst()
                .isPresent();
    }

    private boolean isReturningType(MethodDeclaration method, AbstractTypeDeclaration mainType) {
        return method.getReturnType2()
                .toString()
                .equals(getTypeName(mainType));
    }

    private String getTypeName(AbstractTypeDeclaration builderAbstractTypeDeclaration) {
        return builderAbstractTypeDeclaration.getName().toString();
    }

    private List<AbstractTypeDeclaration> getNestedInterfaces(AbstractTypeDeclaration mainType) {
        return bodyDeclarationOfTypeExtractor.extractBodyDeclaration(mainType, TypeDeclaration.class)
                .stream()
                .filter(type -> type instanceof TypeDeclaration)
                .map(type -> (TypeDeclaration) type)
                .filter(type -> type.isInterface())
                .collect(Collectors.toList());
    }

}

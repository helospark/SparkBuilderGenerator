package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.CompilationUnitParser;

/**
 * Creates TypeDeclaration from the superclass of the given type declaration.
 * @author helospark
 */
public class TypeDeclarationFromSuperclassExtractor {
    private CompilationUnitParser compilationUnitParser;
    private ITypeExtractor iTypeExtractor;

    public TypeDeclarationFromSuperclassExtractor(CompilationUnitParser compilationUnitParser, ITypeExtractor iTypeExtractor) {
        this.compilationUnitParser = compilationUnitParser;
        this.iTypeExtractor = iTypeExtractor;
    }

    public Optional<TypeDeclaration> extractTypeDeclarationFromSuperClass(TypeDeclaration typeDeclaration) {
        try {
            return iTypeExtractor.extract(typeDeclaration)
                    .flatMap(type -> extractTypeDeclaration(type));
        } catch (Exception e) {
            System.out.println("[ERROR] while extracting parent type");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private Optional<TypeDeclaration> extractTypeDeclaration(IType superClassType) {
        return getCompilationUnit(superClassType)
                .map(iCompilationUnit -> ((List<TypeDeclaration>) iCompilationUnit.types()))
                .orElse(emptyList())
                .stream()
                .filter(type -> type.getName().toString().equals(superClassType.getElementName()))
                .findFirst();
    }

    private Optional<CompilationUnit> getCompilationUnit(IType superClassType) {
        Optional<CompilationUnit> result = empty();
        if (superClassType.getCompilationUnit() != null) {
            result = of(compilationUnitParser.parse(superClassType.getCompilationUnit()));
        } else if (superClassType.getClassFile() != null) {
            result = of(compilationUnitParser.parse(superClassType.getClassFile()));
        }
        return result;
    }
}

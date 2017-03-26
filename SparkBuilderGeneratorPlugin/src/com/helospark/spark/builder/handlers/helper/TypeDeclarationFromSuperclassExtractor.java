package com.helospark.spark.builder.handlers.helper;

import java.util.List;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.CompilationUnitParser;

public class TypeDeclarationFromSuperclassExtractor {
    private CompilationUnitParser compilationUnitParser = new CompilationUnitParser();

    public TypeDeclaration extractTypeDeclarationFromSuperClass(TypeDeclaration typeDeclaration) {
        try {
            ITypeBinding superclass = typeDeclaration.resolveBinding().getSuperclass();
            if (superclass != null && superclass.getJavaElement() instanceof IType) {
                return extractTypeDeclaration(superclass);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private TypeDeclaration extractTypeDeclaration(ITypeBinding superclass) {
        TypeDeclaration result = null;
        IType superClassType = (IType) superclass.getJavaElement();
        CompilationUnit compilationUnit = getCompilationUnit(superClassType);
        if (compilationUnit != null) {
            result = ((List<TypeDeclaration>) compilationUnit.types())
                    .stream()
                    .filter(type -> type.getName().toString().equals(superClassType.getElementName()))
                    .findFirst()
                    .orElse(null);
        }
        return result;
    }

    private CompilationUnit getCompilationUnit(IType superClassType) {
        CompilationUnit result = null;
        if (superClassType.getCompilationUnit() != null) {
            result = compilationUnitParser.parse(superClassType.getCompilationUnit());
        } else if (superClassType.getClassFile() != null) {
            result = compilationUnitParser.parse(superClassType.getClassFile());
        }
        return result;
    }
}

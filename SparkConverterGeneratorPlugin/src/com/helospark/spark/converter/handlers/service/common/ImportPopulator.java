package com.helospark.spark.converter.handlers.service.common;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ImportDeclaration;

import com.helospark.spark.converter.handlers.service.domain.CompilationUnitModificationDomain;

/**
 * Add imports.
 * 
 * @author helospark
 */
public class ImportPopulator {

    public String addImport(CompilationUnitModificationDomain compilationUnitModificationDomain, String fullyQualifiedName) {
        if (isImportDeclarationNeeded(compilationUnitModificationDomain, fullyQualifiedName)) {
            AST ast = compilationUnitModificationDomain.getAst();
            ImportDeclaration importDeclaration = ast.newImportDeclaration();
            importDeclaration.setName(ast.newName(fullyQualifiedName));
            compilationUnitModificationDomain.addImport(importDeclaration);
        }
        return extractClassName(fullyQualifiedName);
    }

    private boolean isImportDeclarationNeeded(CompilationUnitModificationDomain compilationUnitModificationDomain, String fullyQualifiedClassName) {
        return !isImportExists(compilationUnitModificationDomain, fullyQualifiedClassName) &&
                !isImportInSamePackage(compilationUnitModificationDomain, fullyQualifiedClassName);
    }

    private boolean isImportExists(CompilationUnitModificationDomain compilationUnit, String fullyQualifiedName) {
        return compilationUnit.getImports()
                .stream()
                .filter(importDeclaration -> importDeclaration.getName().toString().equals(fullyQualifiedName))
                .findFirst()
                .map(found -> Boolean.TRUE)
                .orElse(Boolean.FALSE);
    }

    private boolean isImportInSamePackage(CompilationUnitModificationDomain compilationUnit, String fullyQualifiedClassName) {
        String packageName = extractPackageName(fullyQualifiedClassName);
        return compilationUnit.getCompilationUnit().getPackage().toString().equals(packageName);
    }

    private String extractPackageName(String fullyQualifiedClassName) {
        int lastDotIndex = fullyQualifiedClassName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return fullyQualifiedClassName;
        }
        return fullyQualifiedClassName.substring(0, lastDotIndex);
    }

    private String extractClassName(String fullyQualifiedClassName) {
        int lastDotIndex = fullyQualifiedClassName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return fullyQualifiedClassName;
        }
        return fullyQualifiedClassName.substring(lastDotIndex + 1);
    }

}

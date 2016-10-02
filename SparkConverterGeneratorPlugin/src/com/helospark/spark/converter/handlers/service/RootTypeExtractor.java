package com.helospark.spark.converter.handlers.service;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;

/**
 * Extracts the first root type from CompilationUnit.
 * 
 * @author helospark
 */
public class RootTypeExtractor {

    public IType extractTypeDeclaration(ICompilationUnit compilationUnit) {
        try {
            IType[] types = compilationUnit.getTypes();
            if (types == null || types.length == 0) {
                throw new RuntimeException("No type found in compilation unit");
            }
            return types[0];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

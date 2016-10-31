package com.helospark.spark.builder.handlers;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;

public class CompilationUnitSourceSetter {

    public void setCompilationUnitSource(ICompilationUnit compilationUnit, String source) throws JavaModelException {
        compilationUnit.getBuffer().setContents(source);
    }
}

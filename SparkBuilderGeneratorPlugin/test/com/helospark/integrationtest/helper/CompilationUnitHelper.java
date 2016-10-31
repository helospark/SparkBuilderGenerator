package com.helospark.integrationtest.helper;

import org.eclipse.jdt.core.ICompilationUnit;

public class CompilationUnitHelper {

    public static ICompilationUnit loadTestFile(String filename) {
        return new TestICompilationUnit("class Asd { String abc; }");
    }
}
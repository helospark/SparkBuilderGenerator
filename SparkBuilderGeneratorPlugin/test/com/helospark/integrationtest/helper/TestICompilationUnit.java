package com.helospark.integrationtest.helper;

import static org.mockito.Mockito.mock;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.core.CompilationUnit;

public class TestICompilationUnit extends CompilationUnit {
    private String contents;

    public TestICompilationUnit(String contents) {
        super(null, null, null);
        this.contents = contents;
    }

    @Override
    public char[] getContents() {
        return contents.toCharArray();
    }

    @Override
    public IJavaProject getJavaProject() {
        return mock(IJavaProject.class);
    }

}

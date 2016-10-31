package com.helospark.integrationtest.helper;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.core.NameLookup;

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
        try {
            IProject iProject = mock(IProject.class);
            given(iProject.getDefaultCharset()).willReturn("utf-8");

            NameLookup nameLookup = mock(NameLookup.class);

            JavaProject project = mock(JavaProject.class);
            given(project.newNameLookup(any(ICompilationUnit[].class))).willReturn(nameLookup);
            given(project.getResource()).willReturn(iProject);

            IJavaProject mockProject = mock(JavaProject.class);
            given(mockProject.getJavaProject()).willReturn(project);
            return mockProject;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public char[] getFileName() {
        return "testFile".toCharArray();
    }

}

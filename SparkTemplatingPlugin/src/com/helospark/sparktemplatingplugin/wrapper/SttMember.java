package com.helospark.sparktemplatingplugin.wrapper;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.JavaModelException;

public interface SttMember {
    void delete() throws JavaModelException;

    String getAttachedJavadoc(IProgressMonitor paramIProgressMonitor) throws JavaModelException;

    SttCompilationUnit getCompilationUnit();

    SttType getDeclaringType();

    String getSource() throws JavaModelException;

    void rename(String newName) throws JavaModelException;

    boolean isPresent();
}
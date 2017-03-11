package com.helospark.sparktemplatingplugin.wrapper;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.JavaModelException;

public interface SttLocalVariable {
    SttAnnotation getAnnotation(String annotationName);

    List<SttAnnotation> getAnnotations() throws JavaModelException;

    String getAttachedJavadoc(IProgressMonitor paramIProgressMonitor) throws JavaModelException;

    SttMember getDeclaringMember();

    String getSource() throws JavaModelException;

    String getTypeSignature();

    boolean isParameter();

    boolean isPresent();
}
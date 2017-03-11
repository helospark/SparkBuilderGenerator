package com.helospark.sparktemplatingplugin.wrapper;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.JavaModelException;

public interface SttField {
    SttAnnotation getAnnotation(String paramString);

    List<SttAnnotation> getAnnotations() throws JavaModelException;

    SttCompilationUnit getCompilationUnit();

    SttType getDeclaringType();

    void delete() throws JavaModelException;

    String getAttachedJavadoc(IProgressMonitor paramIProgressMonitor) throws JavaModelException;

    Object getConstant() throws JavaModelException;

    String getElementName();

    String getSource() throws JavaModelException;

    SttType getType(String paramString, int paramInt);

    String getTypeSignature();

    boolean isEnumConstant() throws JavaModelException;

    void rename(String newName) throws JavaModelException;

    boolean isPresent();
}
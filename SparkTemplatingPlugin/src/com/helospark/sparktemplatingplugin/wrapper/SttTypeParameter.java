package com.helospark.sparktemplatingplugin.wrapper;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.JavaModelException;

public interface SttTypeParameter {
    List<String> getBounds() throws JavaModelException;

    List<String> getBoundsSignatures() throws JavaModelException;

    SttMember getDeclaringMember();

    String getAttachedJavadoc(IProgressMonitor paramIProgressMonitor) throws JavaModelException;

    String getSource() throws JavaModelException;

    boolean isPresent();
}
package com.helospark.sparktemplatingplugin.wrapper;

import java.util.List;

import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.JavaModelException;

public interface SttAnnotation {
    String getAttachedJavadoc() throws JavaModelException;

    String getElementName();

    SttJavaProject getJavaProject();

    List<IMemberValuePair> getMemberValuePairs() throws JavaModelException;

    String getSource() throws JavaModelException;

    boolean isPresent();
}
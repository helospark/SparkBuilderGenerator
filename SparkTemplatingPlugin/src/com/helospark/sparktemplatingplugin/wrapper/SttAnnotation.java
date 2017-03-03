package com.helospark.sparktemplatingplugin.wrapper;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.JavaModelException;

public class SttAnnotation {
    private IAnnotation annotation;

    private IProgressMonitor progressMonitor;

    public SttAnnotation(IAnnotation annotation) {
        this.annotation = annotation;
    }

    public String getAttachedJavadoc() throws JavaModelException {
        return annotation.getAttachedJavadoc(progressMonitor);
    }

    public String getElementName() {
        return annotation.getElementName();
    }

    public IJavaProject getJavaProject() {
        return annotation.getJavaProject();
    }

    public IMemberValuePair[] getMemberValuePairs() throws JavaModelException {
        return annotation.getMemberValuePairs();
    }

    public String getSource() throws JavaModelException {
        return annotation.getSource();
    }

}

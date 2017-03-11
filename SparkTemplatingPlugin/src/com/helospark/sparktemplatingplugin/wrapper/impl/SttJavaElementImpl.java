package com.helospark.sparktemplatingplugin.wrapper.impl;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;

import com.helospark.sparktemplatingplugin.wrapper.SttJavaProject;

public abstract class SttJavaElementImpl<T extends IJavaElement> {
    protected T wrappedElement;
    protected IProgressMonitor progressMonitor = new NullProgressMonitor();

    public SttJavaElementImpl(T javaElement) {
        this.wrappedElement = javaElement;
    }

    public String getAttachedJavadoc() throws JavaModelException {
        return wrappedElement.getAttachedJavadoc(progressMonitor);
    }

    public String getElementName() {
        return wrappedElement.getElementName();
    }

    public SttJavaProject getJavaProject() {
        return new SttJavaProjectImpl(wrappedElement.getJavaProject());
    }

    public T getRaw() {
        return wrappedElement;
    }

    public boolean isPresent() {
        return true;
    }
}

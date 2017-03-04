package com.helospark.sparktemplatingplugin.wrapper;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;

public abstract class SttJavaElement<T extends IJavaElement> {
    protected T wrappedElement;
    protected IProgressMonitor progressMonitor = new NullProgressMonitor();

    public SttJavaElement(T javaElement) {
        this.wrappedElement = javaElement;
    }

    public String getAttachedJavadoc() throws JavaModelException {
        return wrappedElement.getAttachedJavadoc(progressMonitor);
    }

    public String getElementName() {
        return wrappedElement.getElementName();
    }

    public SttJavaProject getJavaProject() {
        return new SttJavaProject(wrappedElement.getJavaProject());
    }

    public T getRaw() {
        return wrappedElement;
    }

}

package com.helospark.sparktemplatingplugin.wrapper;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.JavaModelException;

public class SttMember extends SttJavaElement<IMember> {

    public SttMember(IMember javaElement) {
        super(javaElement);
    }

    public void delete() throws JavaModelException {
        wrappedElement.delete(false, progressMonitor);
    }

    public String getAttachedJavadoc(IProgressMonitor paramIProgressMonitor) throws JavaModelException {
        return wrappedElement.getAttachedJavadoc(paramIProgressMonitor);
    }

    public SttCompilationUnit getCompilationUnit() {
        return new SttCompilationUnit(wrappedElement.getCompilationUnit());
    }

    public SttType getDeclaringType() {
        return new SttType(wrappedElement.getDeclaringType());
    }

    public String getSource() throws JavaModelException {
        return wrappedElement.getSource();
    }

    public void rename(String newName) throws JavaModelException {
        wrappedElement.rename(newName, false, progressMonitor);
    }
}

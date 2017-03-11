package com.helospark.sparktemplatingplugin.wrapper.impl;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.JavaModelException;

import com.helospark.sparktemplatingplugin.wrapper.SttCompilationUnit;
import com.helospark.sparktemplatingplugin.wrapper.SttMember;
import com.helospark.sparktemplatingplugin.wrapper.SttType;

public class SttMemberImpl extends SttJavaElementImpl<IMember> implements SttMember {

    public SttMemberImpl(IMember javaElement) {
        super(javaElement);
    }

    @Override
    public void delete() throws JavaModelException {
        wrappedElement.delete(false, progressMonitor);
    }

    @Override
    public String getAttachedJavadoc(IProgressMonitor paramIProgressMonitor) throws JavaModelException {
        return wrappedElement.getAttachedJavadoc(paramIProgressMonitor);
    }

    @Override
    public SttCompilationUnit getCompilationUnit() {
        return new SttCompilationUnitImpl(wrappedElement.getCompilationUnit());
    }

    @Override
    public SttType getDeclaringType() {
        return new SttTypeImpl(wrappedElement.getDeclaringType());
    }

    @Override
    public String getSource() throws JavaModelException {
        return wrappedElement.getSource();
    }

    @Override
    public void rename(String newName) throws JavaModelException {
        wrappedElement.rename(newName, false, progressMonitor);
    }
}

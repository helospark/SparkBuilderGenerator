package com.helospark.sparktemplatingplugin.wrapper.impl;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaModelException;

import com.helospark.sparktemplatingplugin.wrapper.SttMember;
import com.helospark.sparktemplatingplugin.wrapper.SttTypeParameter;

public class SttTypeParameterImpl extends SttJavaElementImpl<ITypeParameter> implements SttTypeParameter {

    public SttTypeParameterImpl(ITypeParameter typeParameter) {
        super(typeParameter);
    }

    @Override
    public List<String> getBounds() throws JavaModelException {
        return Arrays.asList(wrappedElement.getBounds());
    }

    @Override
    public List<String> getBoundsSignatures() throws JavaModelException {
        return Arrays.asList(wrappedElement.getBoundsSignatures());
    }

    @Override
    public SttMember getDeclaringMember() {
        return new SttMemberImpl(wrappedElement.getDeclaringMember());
    }

    @Override
    public String getAttachedJavadoc(IProgressMonitor paramIProgressMonitor) throws JavaModelException {
        return wrappedElement.getAttachedJavadoc(paramIProgressMonitor);
    }

    @Override
    public String getSource() throws JavaModelException {
        return wrappedElement.getSource();
    }

}
package com.helospark.sparktemplatingplugin.wrapper.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.JavaModelException;

import com.helospark.sparktemplatingplugin.wrapper.SttAnnotation;
import com.helospark.sparktemplatingplugin.wrapper.SttLocalVariable;
import com.helospark.sparktemplatingplugin.wrapper.SttMember;

public class SttLocalVariableImpl extends SttJavaElementImpl<ILocalVariable> implements SttLocalVariable {
    public SttLocalVariableImpl(ILocalVariable localVariable) {
        super(localVariable);
    }

    @Override
    public SttAnnotation getAnnotation(String annotationName) {
        return new SttAnnotationImpl(wrappedElement.getAnnotation(annotationName));
    }

    @Override
    public List<SttAnnotation> getAnnotations() throws JavaModelException {
        return Arrays.stream(wrappedElement.getAnnotations())
                .map(SttAnnotationImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getAttachedJavadoc(IProgressMonitor paramIProgressMonitor) throws JavaModelException {
        return wrappedElement.getAttachedJavadoc(paramIProgressMonitor);
    }

    @Override
    public SttMember getDeclaringMember() {
        return new SttMemberImpl(wrappedElement.getDeclaringMember());
    }

    @Override
    public String getSource() throws JavaModelException {
        return wrappedElement.getSource();
    }

    @Override
    public String getTypeSignature() {
        return wrappedElement.getTypeSignature();
    }

    @Override
    public boolean isParameter() {
        return wrappedElement.isParameter();
    }
}

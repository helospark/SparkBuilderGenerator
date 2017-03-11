package com.helospark.sparktemplatingplugin.wrapper.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;

import com.helospark.sparktemplatingplugin.wrapper.SttAnnotation;
import com.helospark.sparktemplatingplugin.wrapper.SttCompilationUnit;
import com.helospark.sparktemplatingplugin.wrapper.SttField;
import com.helospark.sparktemplatingplugin.wrapper.SttType;

public class SttFieldImpl extends SttJavaElementImpl<IField> implements SttField {
    public SttFieldImpl(IField field) {
        super(field);
    }

    @Override
    public SttAnnotation getAnnotation(String paramString) {
        return new SttAnnotationImpl(wrappedElement.getAnnotation(paramString));
    }

    @Override
    public List<SttAnnotation> getAnnotations() throws JavaModelException {
        return Arrays.stream(wrappedElement.getAnnotations())
                .map(SttAnnotationImpl::new)
                .collect(Collectors.toList());
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
    public void delete() throws JavaModelException {
        wrappedElement.delete(false, progressMonitor);
    }

    @Override
    public String getAttachedJavadoc(IProgressMonitor paramIProgressMonitor) throws JavaModelException {
        return wrappedElement.getAttachedJavadoc(paramIProgressMonitor);
    }

    @Override
    public Object getConstant() throws JavaModelException {
        return wrappedElement.getConstant();
    }

    @Override
    public String getElementName() {
        return wrappedElement.getElementName();
    }

    @Override
    public String getSource() throws JavaModelException {
        return wrappedElement.getSource();
    }

    @Override
    public SttType getType(String paramString, int paramInt) {
        return new SttTypeImpl(wrappedElement.getType(paramString, paramInt));
    }

    @Override
    public String getTypeSignature() {
        try {
            return wrappedElement.getTypeSignature();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isEnumConstant() throws JavaModelException {
        return wrappedElement.isEnumConstant();
    }

    @Override
    public void rename(String newName) throws JavaModelException {
        wrappedElement.rename(newName, false, progressMonitor);
    }

}

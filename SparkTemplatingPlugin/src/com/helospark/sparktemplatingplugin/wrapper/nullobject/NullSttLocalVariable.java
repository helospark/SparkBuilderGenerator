package com.helospark.sparktemplatingplugin.wrapper.nullobject;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.JavaModelException;

import com.helospark.sparktemplatingplugin.wrapper.SttAnnotation;
import com.helospark.sparktemplatingplugin.wrapper.SttLocalVariable;
import com.helospark.sparktemplatingplugin.wrapper.SttMember;
import com.helospark.sparktemplatingplugin.wrapper.nullobject.exception.MissingObjectException;

public class NullSttLocalVariable
        implements SttLocalVariable {
    private String reasonMessage;

    public NullSttLocalVariable(String message) {
        this.reasonMessage = message;
    }

    @Override
    public SttAnnotation getAnnotation(String annotationName) {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public List<SttAnnotation> getAnnotations() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public String getAttachedJavadoc(IProgressMonitor paramIProgressMonitor) throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public SttMember getDeclaringMember() {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public String getSource() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public String getTypeSignature() {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public boolean isParameter() {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public boolean isPresent() {
        return false;
    }
}

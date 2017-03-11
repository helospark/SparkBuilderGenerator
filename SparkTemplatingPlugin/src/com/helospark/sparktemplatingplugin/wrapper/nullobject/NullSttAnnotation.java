package com.helospark.sparktemplatingplugin.wrapper.nullobject;

import java.util.List;

import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.JavaModelException;

import com.helospark.sparktemplatingplugin.wrapper.SttAnnotation;
import com.helospark.sparktemplatingplugin.wrapper.SttJavaProject;
import com.helospark.sparktemplatingplugin.wrapper.nullobject.exception.MissingObjectException;

public class NullSttAnnotation
        implements SttAnnotation {
    private String reasonMessage;

    public NullSttAnnotation(String message) {
        this.reasonMessage = message;
    }

    @Override
    public String getAttachedJavadoc() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public String getElementName() {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public SttJavaProject getJavaProject() {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public List<IMemberValuePair> getMemberValuePairs() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public String getSource() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public boolean isPresent() {
        return false;
    }
}

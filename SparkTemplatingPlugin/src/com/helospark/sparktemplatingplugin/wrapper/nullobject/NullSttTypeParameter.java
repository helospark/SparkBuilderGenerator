package com.helospark.sparktemplatingplugin.wrapper.nullobject;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.JavaModelException;

import com.helospark.sparktemplatingplugin.wrapper.SttMember;
import com.helospark.sparktemplatingplugin.wrapper.SttTypeParameter;
import com.helospark.sparktemplatingplugin.wrapper.nullobject.exception.MissingObjectException;

public class NullSttTypeParameter
        implements SttTypeParameter {
    private String reasonMessage;

    public NullSttTypeParameter(String message) {
        this.reasonMessage = message;
    }

    @Override
    public List<String> getBounds() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public List<String> getBoundsSignatures() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public SttMember getDeclaringMember() {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public String getAttachedJavadoc(IProgressMonitor paramIProgressMonitor) throws JavaModelException {
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

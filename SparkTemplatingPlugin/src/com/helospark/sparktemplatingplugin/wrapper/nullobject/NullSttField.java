package com.helospark.sparktemplatingplugin.wrapper.nullobject;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.JavaModelException;

import com.helospark.sparktemplatingplugin.wrapper.SttAnnotation;
import com.helospark.sparktemplatingplugin.wrapper.SttCompilationUnit;
import com.helospark.sparktemplatingplugin.wrapper.SttField;
import com.helospark.sparktemplatingplugin.wrapper.SttType;
import com.helospark.sparktemplatingplugin.wrapper.nullobject.exception.MissingObjectException;

public class NullSttField
        implements SttField {
    private String message;

    public NullSttField(String message) {
        this.message = message;
    }

    @Override
    public SttAnnotation getAnnotation(String paramString) {
        throw new MissingObjectException(message);
    }

    @Override
    public List<SttAnnotation> getAnnotations() throws JavaModelException {
        throw new MissingObjectException(message);
    }

    @Override
    public SttCompilationUnit getCompilationUnit() {
        throw new MissingObjectException(message);
    }

    @Override
    public SttType getDeclaringType() {
        throw new MissingObjectException(message);
    }

    @Override
    public void delete() throws JavaModelException {
        throw new MissingObjectException(message);
    }

    @Override
    public String getAttachedJavadoc(IProgressMonitor paramIProgressMonitor) throws JavaModelException {
        throw new MissingObjectException(message);
    }

    @Override
    public Object getConstant() throws JavaModelException {
        throw new MissingObjectException(message);
    }

    @Override
    public String getElementName() {
        throw new MissingObjectException(message);
    }

    @Override
    public String getSource() throws JavaModelException {
        throw new MissingObjectException(message);
    }

    @Override
    public SttType getType(String paramString, int paramInt) {
        throw new MissingObjectException(message);
    }

    @Override
    public String getTypeSignature() {
        throw new MissingObjectException(message);
    }

    @Override
    public boolean isEnumConstant() throws JavaModelException {
        throw new MissingObjectException(message);
    }

    @Override
    public void rename(String newName) throws JavaModelException {
        throw new MissingObjectException(message);
    }

    @Override
    public boolean isPresent() {
        return false;
    }
}

package com.helospark.sparktemplatingplugin.wrapper.nullobject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.JavaModelException;

import com.helospark.sparktemplatingplugin.wrapper.SttCompilationUnit;
import com.helospark.sparktemplatingplugin.wrapper.SttMember;
import com.helospark.sparktemplatingplugin.wrapper.SttType;
import com.helospark.sparktemplatingplugin.wrapper.nullobject.exception.MissingObjectException;

public class NullSttMember
        implements SttMember {
    private String reasonMessage;

    public NullSttMember(String message) {
        this.reasonMessage = message;
    }

    @Override
    public void delete() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public String getAttachedJavadoc(IProgressMonitor paramIProgressMonitor) throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public SttCompilationUnit getCompilationUnit() {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public SttType getDeclaringType() {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public String getSource() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public void rename(String newName) throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public boolean isPresent() {
        return false;
    }
}

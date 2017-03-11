package com.helospark.sparktemplatingplugin.wrapper.nullobject;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.JavaModelException;

import com.helospark.sparktemplatingplugin.wrapper.SttCompilationUnit;
import com.helospark.sparktemplatingplugin.wrapper.SttPackageFragment;
import com.helospark.sparktemplatingplugin.wrapper.nullobject.exception.MissingObjectException;

public class NullSttPackageFragment
        implements SttPackageFragment {
    private String reasonMessage;

    public NullSttPackageFragment(String message) {
        this.reasonMessage = message;
    }

    @Override
    public boolean containsJavaResources() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public SttCompilationUnit createCompilationUnit(String name, String content) throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public void delete() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public SttCompilationUnit getCompilationUnit(String name) {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public Object[] getNonJavaResources() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public IPath getPath() {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public boolean hasSubpackages() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public boolean isDefaultPackage() {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public void rename(String newName) throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public List<SttCompilationUnit> getCompilationUnits() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public boolean isPresent() {
        return false;
    }
}

package com.helospark.sparktemplatingplugin.wrapper.nullobject;

import org.eclipse.jdt.core.JavaModelException;

import com.helospark.sparktemplatingplugin.wrapper.SttPackageFragment;
import com.helospark.sparktemplatingplugin.wrapper.SttPackageFragmentRoot;
import com.helospark.sparktemplatingplugin.wrapper.nullobject.exception.MissingObjectException;

public class NullSttPackageFragmentRoot
        implements SttPackageFragmentRoot {
    private String reasonMessage;

    public NullSttPackageFragmentRoot(String message) {
        this.reasonMessage = message;
    }

    @Override
    public SttPackageFragment createPackageFragment(String name) throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public void delete() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public Object[] getNonJavaResources() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public SttPackageFragment getPackageFragment(String subPackage) {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public boolean isPresent() {
        return false;
    }
}

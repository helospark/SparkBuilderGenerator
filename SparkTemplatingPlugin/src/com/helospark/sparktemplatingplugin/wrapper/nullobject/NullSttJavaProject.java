package com.helospark.sparktemplatingplugin.wrapper.nullobject;

import java.util.List;

import org.eclipse.jdt.core.JavaModelException;

import com.helospark.sparktemplatingplugin.wrapper.SttJavaProject;
import com.helospark.sparktemplatingplugin.wrapper.SttPackageFragment;
import com.helospark.sparktemplatingplugin.wrapper.SttPackageFragmentRoot;
import com.helospark.sparktemplatingplugin.wrapper.SttType;
import com.helospark.sparktemplatingplugin.wrapper.nullobject.exception.MissingObjectException;

public class NullSttJavaProject
        implements SttJavaProject {
    private String reasonMessage;

    public NullSttJavaProject(String message) {
        this.reasonMessage = message;
    }

    @Override
    public SttType findType(String fullyQualifiedName) throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public SttType findType(String packageName, String typeName) throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public List<SttPackageFragmentRoot> getAllPackageFragmentRoots() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public Object[] getNonJavaResources() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public SttPackageFragmentRoot getPackageFragmentRoot(String paramString) {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public List<SttPackageFragmentRoot> getPackageFragmentRoots() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public List<SttPackageFragment> getPackageFragments() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public boolean isPresent() {
        return false;
    }
}

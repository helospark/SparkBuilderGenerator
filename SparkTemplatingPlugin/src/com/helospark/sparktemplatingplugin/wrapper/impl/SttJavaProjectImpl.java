package com.helospark.sparktemplatingplugin.wrapper.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

import com.helospark.sparktemplatingplugin.wrapper.SttJavaProject;
import com.helospark.sparktemplatingplugin.wrapper.SttPackageFragment;
import com.helospark.sparktemplatingplugin.wrapper.SttPackageFragmentRoot;
import com.helospark.sparktemplatingplugin.wrapper.SttType;

public class SttJavaProjectImpl extends SttJavaElementImpl<IJavaProject> implements SttJavaProject {

    public SttJavaProjectImpl(IJavaProject javaProject) {
        super(javaProject);
    }

    @Override
    public SttType findType(String fullyQualifiedName) throws JavaModelException {
        return new SttTypeImpl(wrappedElement.findType(fullyQualifiedName));
    }

    @Override
    public SttType findType(String packageName, String typeName) throws JavaModelException {
        return new SttTypeImpl(wrappedElement.findType(packageName, typeName));
    }

    @Override
    public List<SttPackageFragmentRoot> getAllPackageFragmentRoots() throws JavaModelException {
        return Arrays.stream(wrappedElement.getAllPackageFragmentRoots())
                .map(SttPackageFragmentRootImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public Object[] getNonJavaResources() throws JavaModelException {
        return wrappedElement.getNonJavaResources();
    }

    @Override
    public SttPackageFragmentRoot getPackageFragmentRoot(String paramString) {
        return new SttPackageFragmentRootImpl(wrappedElement.getPackageFragmentRoot(paramString));
    }

    @Override
    public List<SttPackageFragmentRoot> getPackageFragmentRoots() throws JavaModelException {
        return Arrays.stream(wrappedElement.getPackageFragmentRoots())
                .map(SttPackageFragmentRootImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<SttPackageFragment> getPackageFragments() throws JavaModelException {
        return Arrays.stream(wrappedElement.getPackageFragments())
                .map(SttPackageFragmentImpl::new)
                .collect(Collectors.toList());
    }

}

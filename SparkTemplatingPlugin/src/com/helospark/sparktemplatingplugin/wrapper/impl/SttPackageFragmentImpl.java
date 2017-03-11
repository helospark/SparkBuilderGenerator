package com.helospark.sparktemplatingplugin.wrapper.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;

import com.helospark.sparktemplatingplugin.wrapper.SttCompilationUnit;
import com.helospark.sparktemplatingplugin.wrapper.SttPackageFragment;

public class SttPackageFragmentImpl extends SttJavaElementImpl<IPackageFragment> implements SttPackageFragment {

    public SttPackageFragmentImpl(IPackageFragment javaElement) {
        super(javaElement);
    }

    @Override
    public boolean containsJavaResources() throws JavaModelException {
        return wrappedElement.containsJavaResources();
    }

    @Override
    public SttCompilationUnit createCompilationUnit(String name, String content)
            throws JavaModelException {
        return new SttCompilationUnitImpl(wrappedElement.createCompilationUnit(name, content, false, progressMonitor));
    }

    @Override
    public void delete() throws JavaModelException {
        wrappedElement.delete(false, progressMonitor);
    }

    @Override
    public SttCompilationUnit getCompilationUnit(String name) {
        return new SttCompilationUnitImpl(wrappedElement.getCompilationUnit(name));
    }

    @Override
    public List<SttCompilationUnit> getCompilationUnits() throws JavaModelException {
        return Arrays.stream(wrappedElement.getCompilationUnits())
                .map(SttCompilationUnitImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public Object[] getNonJavaResources() throws JavaModelException {
        return wrappedElement.getNonJavaResources();
    }

    @Override
    public IPath getPath() {
        return wrappedElement.getPath();
    }

    @Override
    public boolean hasSubpackages() throws JavaModelException {
        return wrappedElement.hasSubpackages();
    }

    @Override
    public boolean isDefaultPackage() {
        return wrappedElement.isDefaultPackage();
    }

    @Override
    public void rename(String newName) throws JavaModelException {
        wrappedElement.rename(newName, false, progressMonitor);
    }

}

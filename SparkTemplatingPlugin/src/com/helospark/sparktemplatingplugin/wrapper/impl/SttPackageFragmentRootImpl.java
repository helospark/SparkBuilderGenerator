package com.helospark.sparktemplatingplugin.wrapper.impl;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

import com.helospark.sparktemplatingplugin.wrapper.SttPackageFragment;
import com.helospark.sparktemplatingplugin.wrapper.SttPackageFragmentRoot;

public class SttPackageFragmentRootImpl extends SttJavaElementImpl<IPackageFragmentRoot> implements SttPackageFragmentRoot {
    public SttPackageFragmentRootImpl(IPackageFragmentRoot packageFragmentRoot) {
        super(packageFragmentRoot);
    }

    @Override
    public SttPackageFragment createPackageFragment(String name) throws JavaModelException {
        return new SttPackageFragmentImpl(wrappedElement.createPackageFragment(name, false, progressMonitor));
    }

    @Override
    public void delete() throws JavaModelException {
        wrappedElement.delete(IPackageFragmentRoot.OTHER_REFERRING_PROJECTS_CLASSPATH, IResource.KEEP_HISTORY, progressMonitor);
    }

    @Override
    public Object[] getNonJavaResources() throws JavaModelException {
        return wrappedElement.getNonJavaResources();
    }

    @Override
    public SttPackageFragment getPackageFragment(String subPackage) {
        return new SttPackageFragmentImpl(wrappedElement.getPackageFragment(subPackage));
    }

}

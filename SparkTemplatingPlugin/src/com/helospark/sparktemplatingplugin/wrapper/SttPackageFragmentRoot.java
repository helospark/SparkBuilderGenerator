package com.helospark.sparktemplatingplugin.wrapper;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

public class SttPackageFragmentRoot extends SttJavaElement<IPackageFragmentRoot> {
    public SttPackageFragmentRoot(IPackageFragmentRoot packageFragmentRoot) {
        super(packageFragmentRoot);
    }

    public SttPackageFragment createPackageFragment(String name) throws JavaModelException {
        return new SttPackageFragment(wrappedElement.createPackageFragment(name, false, progressMonitor));
    }

    public void delete() throws JavaModelException {
        wrappedElement.delete(IPackageFragmentRoot.OTHER_REFERRING_PROJECTS_CLASSPATH, IResource.KEEP_HISTORY, progressMonitor);
    }

    public Object[] getNonJavaResources() throws JavaModelException {
        return wrappedElement.getNonJavaResources();
    }

    public SttPackageFragment getPackageFragment(String subPackage) {
        return new SttPackageFragment(wrappedElement.getPackageFragment(subPackage));
    }

}

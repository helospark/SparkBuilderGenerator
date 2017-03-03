package com.helospark.sparktemplatingplugin.wrapper;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

public class SttPackageFragmentRoot {
    private IPackageFragmentRoot packageFragmentRoot;
    private IProgressMonitor progressMonitor = new NullProgressMonitor();

    public SttPackageFragmentRoot(IPackageFragmentRoot packageFragmentRoot) {
        this.packageFragmentRoot = packageFragmentRoot;
    }

    public void copy(IPath arg0, int arg1, int arg2, IClasspathEntry arg3, IProgressMonitor arg4) throws JavaModelException {
        packageFragmentRoot.copy(arg0, arg1, arg2, arg3, arg4);
    }

    public SttPackageFragment createPackageFragment(String name) throws JavaModelException {
        return new SttPackageFragment(packageFragmentRoot.createPackageFragment(name, false, progressMonitor));
    }

    public void delete(int arg0, int arg1, IProgressMonitor arg2) throws JavaModelException {
        packageFragmentRoot.delete(arg0, arg1, arg2);
    }

    public String getElementName() {
        return packageFragmentRoot.getElementName();
    }

    public SttJavaProject getJavaProject() {
        return new SttJavaProject(packageFragmentRoot.getJavaProject());
    }

    public Object[] getNonJavaResources() throws JavaModelException {
        return packageFragmentRoot.getNonJavaResources();
    }

    public SttPackageFragment getPackageFragment(String subPackage) {
        return new SttPackageFragment(packageFragmentRoot.getPackageFragment(subPackage));
    }

    //    public IClasspathEntry getRawClasspathEntry() throws JavaModelException {
    //        return packageFragmentRoot.getRawClasspathEntry();
    //    }
    //
    //    public IClasspathEntry getResolvedClasspathEntry() throws JavaModelException {
    //        return packageFragmentRoot.getResolvedClasspathEntry();
    //    }

    public void move(IPath arg0, int arg1, int arg2, IClasspathEntry arg3, IProgressMonitor arg4) throws JavaModelException {
        packageFragmentRoot.move(arg0, arg1, arg2, arg3, arg4);
    }

}

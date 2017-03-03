package com.helospark.sparktemplatingplugin.wrapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;

public class SttPackageFragment {
    private IPackageFragment packageFragment;

    private IProgressMonitor progressMonitor;

    public SttPackageFragment(IPackageFragment packageFragment) {
        this.packageFragment = packageFragment;
    }

    //    public void copy(IJavaElement paramIJavaElement1, IJavaElement paramIJavaElement2, String paramString, boolean paramBoolean, IProgressMonitor paramIProgressMonitor)
    //            throws JavaModelException {
    //        packageFragment.copy(paramIJavaElement1, paramIJavaElement2, paramString, paramBoolean, paramIProgressMonitor);
    //    }

    public boolean containsJavaResources() throws JavaModelException {
        return packageFragment.containsJavaResources();
    }

    public ICompilationUnit createCompilationUnit(String name, String content)
            throws JavaModelException {
        return packageFragment.createCompilationUnit(name, content, false, progressMonitor);
    }

    public void delete() throws JavaModelException {
        packageFragment.delete(false, progressMonitor);
    }

    public SttCompilationUnit getCompilationUnit(String name) {
        return new SttCompilationUnit(packageFragment.getCompilationUnit(name));
    }

    public List<SttCompilationUnit> getCompilationUnits() throws JavaModelException {
        return Arrays.stream(packageFragment.getCompilationUnits())
                .map(SttCompilationUnit::new)
                .collect(Collectors.toList());
    }

    public String getElementName() {
        return packageFragment.getElementName();
    }

    public Object[] getNonJavaResources() throws JavaModelException {
        return packageFragment.getNonJavaResources();
    }

    public IPath getPath() {
        return packageFragment.getPath();
    }

    public boolean hasSubpackages() throws JavaModelException {
        return packageFragment.hasSubpackages();
    }

    public boolean isDefaultPackage() {
        return packageFragment.isDefaultPackage();
    }

    //    public void move(IJavaElement paramIJavaElement1, IJavaElement paramIJavaElement2, String paramString, boolean paramBoolean, IProgressMonitor paramIProgressMonitor)
    //            throws JavaModelException {
    //        packageFragment.move(paramIJavaElement1, paramIJavaElement2, paramString, paramBoolean, paramIProgressMonitor);
    //    }

    public void rename(String newName) throws JavaModelException {
        packageFragment.rename(newName, false, progressMonitor);
    }

    public IJavaProject getJavaProject() {
        return packageFragment.getJavaProject();
    }

}

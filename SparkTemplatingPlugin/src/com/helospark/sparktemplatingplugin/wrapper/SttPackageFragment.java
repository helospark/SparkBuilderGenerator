package com.helospark.sparktemplatingplugin.wrapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;

public class SttPackageFragment extends SttJavaElement<IPackageFragment> {

    public SttPackageFragment(IPackageFragment javaElement) {
        super(javaElement);
    }

    public boolean containsJavaResources() throws JavaModelException {
        return wrappedElement.containsJavaResources();
    }

    public SttCompilationUnit createCompilationUnit(String name, String content)
            throws JavaModelException {
        return new SttCompilationUnit(wrappedElement.createCompilationUnit(name, content, false, progressMonitor));
    }

    public void delete() throws JavaModelException {
        wrappedElement.delete(false, progressMonitor);
    }

    public SttCompilationUnit getCompilationUnit(String name) {
        return new SttCompilationUnit(wrappedElement.getCompilationUnit(name));
    }

    public List<SttCompilationUnit> getCompilationUnits() throws JavaModelException {
        return Arrays.stream(wrappedElement.getCompilationUnits())
                .map(SttCompilationUnit::new)
                .collect(Collectors.toList());
    }

    public Object[] getNonJavaResources() throws JavaModelException {
        return wrappedElement.getNonJavaResources();
    }

    public IPath getPath() {
        return wrappedElement.getPath();
    }

    public boolean hasSubpackages() throws JavaModelException {
        return wrappedElement.hasSubpackages();
    }

    public boolean isDefaultPackage() {
        return wrappedElement.isDefaultPackage();
    }

    public void rename(String newName) throws JavaModelException {
        wrappedElement.rename(newName, false, progressMonitor);
    }

}

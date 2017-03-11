package com.helospark.sparktemplatingplugin.wrapper;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.JavaModelException;

public interface SttPackageFragment {
    boolean containsJavaResources() throws JavaModelException;

    SttCompilationUnit createCompilationUnit(String name, String content)
            throws JavaModelException;

    void delete() throws JavaModelException;

    SttCompilationUnit getCompilationUnit(String name);

    List<SttCompilationUnit> getCompilationUnits() throws JavaModelException;

    Object[] getNonJavaResources() throws JavaModelException;

    IPath getPath();

    boolean hasSubpackages() throws JavaModelException;

    boolean isDefaultPackage();

    void rename(String newName) throws JavaModelException;

    boolean isPresent();
}
package com.helospark.sparktemplatingplugin.wrapper;

import org.eclipse.jdt.core.JavaModelException;

public interface SttPackageFragmentRoot {
    SttPackageFragment createPackageFragment(String name) throws JavaModelException;

    void delete() throws JavaModelException;

    Object[] getNonJavaResources() throws JavaModelException;

    SttPackageFragment getPackageFragment(String subPackage);

    boolean isPresent();
}
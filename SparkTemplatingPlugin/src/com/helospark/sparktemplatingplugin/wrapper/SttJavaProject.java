package com.helospark.sparktemplatingplugin.wrapper;

import java.util.List;

import org.eclipse.jdt.core.JavaModelException;

public interface SttJavaProject {
    SttType findType(String fullyQualifiedName) throws JavaModelException;

    SttType findType(String packageName, String typeName) throws JavaModelException;

    List<SttPackageFragmentRoot> getAllPackageFragmentRoots() throws JavaModelException;

    Object[] getNonJavaResources() throws JavaModelException;

    SttPackageFragmentRoot getPackageFragmentRoot(String paramString);

    List<SttPackageFragmentRoot> getPackageFragmentRoots() throws JavaModelException;

    List<SttPackageFragment> getPackageFragments() throws JavaModelException;

    boolean isPresent();
}
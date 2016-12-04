package com.helospark.sparktemplatingplugin.handlers.templater.helper;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

public class PackageRootFinder {

    public IPackageFragmentRoot findSrcPackageFragmentRoot(IJavaProject project) {
        return findPackage(project, "src");
    }

    public IPackageFragmentRoot findTestPackageFragmentRoot(IJavaProject project) {
        return findPackage(project, "test");
    }

    private IPackageFragmentRoot findPackage(IJavaProject project, String rootName) {
        IPackageFragmentRoot[] roots;
        try {
            roots = project.getPackageFragmentRoots();

            for (IPackageFragmentRoot root : roots) {
                if (root.getKind() == IPackageFragmentRoot.K_SOURCE &&
                        root.getElementName().equals(rootName)) {
                    return root;
                }
            }
        } catch (JavaModelException e) {
            throw new RuntimeException("Unable to get package", e);
        }
        throw new RuntimeException("No package root found with name " + rootName);
    }
}

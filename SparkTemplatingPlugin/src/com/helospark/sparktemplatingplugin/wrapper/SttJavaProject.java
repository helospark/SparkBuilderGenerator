package com.helospark.sparktemplatingplugin.wrapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

public class SttJavaProject extends SttJavaElement<IJavaProject> {

    public SttJavaProject(IJavaProject javaProject) {
        super(javaProject);
    }

    public SttType findType(String fullyQualifiedName) throws JavaModelException {
        return new SttType(wrappedElement.findType(fullyQualifiedName));
    }

    public SttType findType(String packageName, String typeName) throws JavaModelException {
        return new SttType(wrappedElement.findType(packageName, typeName));
    }

    public List<SttPackageFragmentRoot> getAllPackageFragmentRoots() throws JavaModelException {
        return Arrays.stream(wrappedElement.getAllPackageFragmentRoots())
                .map(SttPackageFragmentRoot::new)
                .collect(Collectors.toList());
    }

    public Object[] getNonJavaResources() throws JavaModelException {
        return wrappedElement.getNonJavaResources();
    }

    public SttPackageFragmentRoot getPackageFragmentRoot(String paramString) {
        return new SttPackageFragmentRoot(wrappedElement.getPackageFragmentRoot(paramString));
    }

    public List<SttPackageFragmentRoot> getPackageFragmentRoots() throws JavaModelException {
        return Arrays.stream(wrappedElement.getPackageFragmentRoots())
                .map(SttPackageFragmentRoot::new)
                .collect(Collectors.toList());
    }

    public List<SttPackageFragment> getPackageFragments() throws JavaModelException {
        return Arrays.stream(wrappedElement.getPackageFragments())
                .map(SttPackageFragment::new)
                .collect(Collectors.toList());
    }

}

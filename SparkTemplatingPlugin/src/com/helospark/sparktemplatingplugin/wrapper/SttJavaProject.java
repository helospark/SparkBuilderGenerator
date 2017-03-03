package com.helospark.sparktemplatingplugin.wrapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

public class SttJavaProject {
    private IJavaProject javaProject;

    //    public IClasspathEntry decodeClasspathEntry(String paramString) {
    //        return javaProject.decodeClasspathEntry(paramString);
    //    }

    //    public String encodeClasspathEntry(IClasspathEntry paramIClasspathEntry) {
    //        return javaProject.encodeClasspathEntry(paramIClasspathEntry);
    //    }

    public SttJavaProject(IJavaProject javaProject) {
        this.javaProject = javaProject;
    }

    public String getElementName() {
        return javaProject.getElementName();
    }

    public SttType findType(String fullyQualifiedName) throws JavaModelException {
        return new SttType(javaProject.findType(fullyQualifiedName));
    }

    public SttType findType(String packageName, String typeName) throws JavaModelException {
        return new SttType(javaProject.findType(packageName, typeName));
    }

    public List<SttPackageFragmentRoot> getAllPackageFragmentRoots() throws JavaModelException {
        return Arrays.stream(javaProject.getAllPackageFragmentRoots())
                .map(SttPackageFragmentRoot::new)
                .collect(Collectors.toList());
    }

    public Object[] getNonJavaResources() throws JavaModelException {
        return javaProject.getNonJavaResources();
    }

    public SttPackageFragmentRoot getPackageFragmentRoot(String paramString) {
        return new SttPackageFragmentRoot(javaProject.getPackageFragmentRoot(paramString));
    }

    public List<SttPackageFragmentRoot> getPackageFragmentRoots() throws JavaModelException {
        return Arrays.stream(javaProject.getPackageFragmentRoots())
                .map(SttPackageFragmentRoot::new)
                .collect(Collectors.toList());
    }

    public List<SttPackageFragment> getPackageFragments() throws JavaModelException {
        return Arrays.stream(javaProject.getPackageFragments())
                .map(SttPackageFragment::new)
                .collect(Collectors.toList());
    }

}

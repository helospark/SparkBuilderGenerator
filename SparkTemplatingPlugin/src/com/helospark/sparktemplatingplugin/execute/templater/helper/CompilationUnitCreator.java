package com.helospark.sparktemplatingplugin.execute.templater.helper;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

public class CompilationUnitCreator {

    public ICompilationUnit createCompilationUnit(IPackageFragmentRoot src, String packageName, String fileName, String program) {
        try {
            IPackageFragment packageFragment = src.createPackageFragment(packageName, false, new NullProgressMonitor());
            ICompilationUnit iCompliationUnit = packageFragment.createCompilationUnit(fileName, program, true, new NullProgressMonitor());
            iCompliationUnit.getBuffer().save(new NullProgressMonitor(), true);
            return iCompliationUnit;
        } catch (JavaModelException e) {
            throw new RuntimeException("Cannot create class " + packageName + "." + fileName);
        }
    }
}

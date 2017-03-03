package com.helospark.sparktemplatingplugin.wrapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.JavaModelException;

public class SttCompilationUnit {
    private ICompilationUnit iCompilationUnit;

    private IProgressMonitor progressMonitor = new NullProgressMonitor();

    public SttCompilationUnit(ICompilationUnit iCompilationUnit) {
        this.iCompilationUnit = iCompilationUnit;
    }

    public void copy(IJavaElement destination)
            throws JavaModelException {
        iCompilationUnit.copy(destination, null, null, false, progressMonitor);
    }

    public IImportDeclaration addImport(String importToAdd) throws JavaModelException {
        return iCompilationUnit.createImport(importToAdd, null, progressMonitor);
    }

    public IImportDeclaration addStaticImport(String importToAdd) throws JavaModelException {
        return iCompilationUnit.createImport(importToAdd, null, Flags.AccStatic, progressMonitor);
    }

    public IPackageDeclaration addPackageDeclaration(String packageDeclaration) throws JavaModelException {
        return iCompilationUnit.createPackageDeclaration(packageDeclaration, progressMonitor);
    }

    public SttType createEmptyClass(String className) throws JavaModelException {
        return createTypeWithContent("class " + className + "{}");
    }

    public SttType createEmptyInterface(String interfaceName) throws JavaModelException {
        return createTypeWithContent("interface " + interfaceName + "{}");
    }

    public SttType createTypeWithContent(String content) throws JavaModelException {
        try {
            return new SttType(iCompilationUnit.createType(content, null, false, progressMonitor));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(boolean force) throws JavaModelException {
        iCompilationUnit.delete(force, progressMonitor);
    }

    //    public IJavaElement[] findElements(IJavaElement paramIJavaElement) {
    //        return iCompilationUnit.findElements(paramIJavaElement);
    //    }

    public SttType findPrimaryType() {
        return new SttType(iCompilationUnit.findPrimaryType());
    }

    public List<SttType> getAllTypes() throws JavaModelException {
        try {
            return Arrays.stream(iCompilationUnit.getAllTypes())
                    .map(SttType::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //    public IJavaElement getElementAt(int paramInt) throws JavaModelException {
    //        return iCompilationUnit.getElementAt(paramInt);
    //    }

    public List<IImportDeclaration> getImports() {
        try {
            return Arrays.asList(iCompilationUnit.getImports());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getStaticImports() {
        try {
            return getImports().stream()
                    .filter(importElement -> {
                        try {
                            return (importElement.getFlags() & Flags.AccStatic) != 0;
                        } catch (JavaModelException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .map(importElement -> importElement.getElementName())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getNonStaticImports() {
        try {
            return getImports().stream()
                    .filter(importElement -> {
                        try {
                            return (importElement.getFlags() & Flags.AccStatic) == 0;
                        } catch (JavaModelException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .map(importElement -> importElement.getElementName())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasImport(String importName) {
        return getImports()
                .stream()
                .filter(importElement -> importElement.equals(importName))
                .findFirst()
                .isPresent();
    }

    public String getPackageDeclarations() throws JavaModelException {
        return Arrays.stream(iCompilationUnit.getPackageDeclarations())
                .map(IPackageDeclaration::getElementName)
                .findFirst()
                .orElse("");
    }

    public IPath getPath() {
        return iCompilationUnit.getPath();
    }

    public String getSource() throws JavaModelException {
        return iCompilationUnit.getSource();
    }

    public SttType getType(String typeName) {
        return new SttType(iCompilationUnit.getType(typeName));
    }

    public List<SttType> getTypes() throws JavaModelException {
        return Arrays.stream(iCompilationUnit.getTypes())
                .map(SttType::new)
                .collect(Collectors.toList());
    }

    public boolean isConsistent() throws JavaModelException {
        return iCompilationUnit.isConsistent();
    }

    public boolean isReadOnly() {
        return iCompilationUnit.isReadOnly();
    }

    public void makeConsistent() throws JavaModelException {
        iCompilationUnit.makeConsistent(progressMonitor);
    }

    public void move(IJavaElement destination)
            throws JavaModelException {
        iCompilationUnit.move(destination, null, null, false, progressMonitor);
    }

    public void rename(String newName) throws JavaModelException {
        iCompilationUnit.rename(newName, false, progressMonitor);
    }

    public ICompilationUnit getRawCompilationUnit() {
        return iCompilationUnit;
    }

}

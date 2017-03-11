package com.helospark.sparktemplatingplugin.wrapper.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.JavaModelException;

import com.helospark.sparktemplatingplugin.wrapper.SttCompilationUnit;
import com.helospark.sparktemplatingplugin.wrapper.SttType;

public class SttCompilationUnitImpl extends SttJavaElementImpl<ICompilationUnit> implements SttCompilationUnit {
    public SttCompilationUnitImpl(ICompilationUnit iCompilationUnit) {
        super(iCompilationUnit);
    }

    @Override
    public IImportDeclaration addImport(String importToAdd) throws JavaModelException {
        return wrappedElement.createImport(importToAdd, null, progressMonitor);
    }

    @Override
    public IImportDeclaration addStaticImport(String importToAdd) throws JavaModelException {
        return wrappedElement.createImport(importToAdd, null, Flags.AccStatic, progressMonitor);
    }

    @Override
    public void addPackageDeclaration(String packageDeclaration) throws JavaModelException {
        wrappedElement.createPackageDeclaration(packageDeclaration, progressMonitor);
    }

    @Override
    public SttType createEmptyClass(String className) throws JavaModelException {
        return createTypeWithContent("class " + className + "{}");
    }

    @Override
    public SttType createEmptyInterface(String interfaceName) throws JavaModelException {
        return createTypeWithContent("interface " + interfaceName + "{}");
    }

    @Override
    public SttType createTypeWithContent(String content) throws JavaModelException {
        try {
            return new SttTypeImpl(wrappedElement.createType(content, null, false, progressMonitor));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(boolean force) throws JavaModelException {
        wrappedElement.delete(force, progressMonitor);
    }

    @Override
    public SttType findPrimaryType() {
        return new SttTypeImpl(wrappedElement.findPrimaryType());
    }

    @Override
    public List<SttType> getAllTypes() throws JavaModelException {
        try {
            return Arrays.stream(wrappedElement.getAllTypes())
                    .map(SttTypeImpl::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<IImportDeclaration> getImports() {
        try {
            return Arrays.asList(wrappedElement.getImports());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
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

    @Override
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

    @Override
    public boolean hasImport(String importName) {
        return getImports()
                .stream()
                .filter(importElement -> importElement.equals(importName))
                .findFirst()
                .isPresent();
    }

    @Override
    public String getPackageDeclarations() throws JavaModelException {
        return Arrays.stream(wrappedElement.getPackageDeclarations())
                .map(IPackageDeclaration::getElementName)
                .findFirst()
                .orElse("");
    }

    @Override
    public String getSource() throws JavaModelException {
        return wrappedElement.getSource();
    }

    @Override
    public SttType getType(String typeName) {
        return new SttTypeImpl(wrappedElement.getType(typeName));
    }

    @Override
    public List<SttType> getTypes() throws JavaModelException {
        return Arrays.stream(wrappedElement.getTypes())
                .map(SttTypeImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public void move(IJavaElement destination)
            throws JavaModelException {
        wrappedElement.move(destination, null, null, false, progressMonitor);
    }

    @Override
    public void rename(String newName) throws JavaModelException {
        wrappedElement.rename(newName, false, progressMonitor);
    }
}

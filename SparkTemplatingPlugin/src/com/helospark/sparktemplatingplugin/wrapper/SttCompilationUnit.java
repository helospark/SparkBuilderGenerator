package com.helospark.sparktemplatingplugin.wrapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.JavaModelException;

public class SttCompilationUnit extends SttJavaElement<ICompilationUnit> {
    public SttCompilationUnit(ICompilationUnit iCompilationUnit) {
        super(iCompilationUnit);
    }

    public IImportDeclaration addImport(String importToAdd) throws JavaModelException {
        return wrappedElement.createImport(importToAdd, null, progressMonitor);
    }

    public IImportDeclaration addStaticImport(String importToAdd) throws JavaModelException {
        return wrappedElement.createImport(importToAdd, null, Flags.AccStatic, progressMonitor);
    }

    public void addPackageDeclaration(String packageDeclaration) throws JavaModelException {
        wrappedElement.createPackageDeclaration(packageDeclaration, progressMonitor);
    }

    public SttType createEmptyClass(String className) throws JavaModelException {
        return createTypeWithContent("class " + className + "{}");
    }

    public SttType createEmptyInterface(String interfaceName) throws JavaModelException {
        return createTypeWithContent("interface " + interfaceName + "{}");
    }

    public SttType createTypeWithContent(String content) throws JavaModelException {
        try {
            return new SttType(wrappedElement.createType(content, null, false, progressMonitor));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(boolean force) throws JavaModelException {
        wrappedElement.delete(force, progressMonitor);
    }

    public SttType findPrimaryType() {
        return new SttType(wrappedElement.findPrimaryType());
    }

    public List<SttType> getAllTypes() throws JavaModelException {
        try {
            return Arrays.stream(wrappedElement.getAllTypes())
                    .map(SttType::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<IImportDeclaration> getImports() {
        try {
            return Arrays.asList(wrappedElement.getImports());
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
        return Arrays.stream(wrappedElement.getPackageDeclarations())
                .map(IPackageDeclaration::getElementName)
                .findFirst()
                .orElse("");
    }

    public String getSource() throws JavaModelException {
        return wrappedElement.getSource();
    }

    public SttType getType(String typeName) {
        return new SttType(wrappedElement.getType(typeName));
    }

    public List<SttType> getTypes() throws JavaModelException {
        return Arrays.stream(wrappedElement.getTypes())
                .map(SttType::new)
                .collect(Collectors.toList());
    }

    public void move(IJavaElement destination)
            throws JavaModelException {
        wrappedElement.move(destination, null, null, false, progressMonitor);
    }

    public void rename(String newName) throws JavaModelException {
        wrappedElement.rename(newName, false, progressMonitor);
    }

}

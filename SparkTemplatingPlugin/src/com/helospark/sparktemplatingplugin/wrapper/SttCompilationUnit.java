package com.helospark.sparktemplatingplugin.wrapper;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;

public interface SttCompilationUnit {
    IImportDeclaration addImport(String importToAdd) throws JavaModelException;

    IImportDeclaration addStaticImport(String importToAdd) throws JavaModelException;

    void addPackageDeclaration(String packageDeclaration) throws JavaModelException;

    SttType createEmptyClass(String className) throws JavaModelException;

    SttType createEmptyInterface(String interfaceName) throws JavaModelException;

    SttType createTypeWithContent(String content) throws JavaModelException;

    void delete(boolean force) throws JavaModelException;

    SttType findPrimaryType();

    List<SttType> getAllTypes() throws JavaModelException;

    List<IImportDeclaration> getImports();

    List<String> getStaticImports();

    List<String> getNonStaticImports();

    boolean hasImport(String importName);

    String getPackageDeclarations() throws JavaModelException;

    String getSource() throws JavaModelException;

    SttType getType(String typeName);

    List<SttType> getTypes() throws JavaModelException;

    void move(IJavaElement destination)
            throws JavaModelException;

    void rename(String newName) throws JavaModelException;

    ICompilationUnit getRaw();

    boolean isPresent();
}
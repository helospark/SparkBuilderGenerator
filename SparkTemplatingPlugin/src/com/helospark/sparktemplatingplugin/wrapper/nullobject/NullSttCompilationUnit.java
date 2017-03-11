package com.helospark.sparktemplatingplugin.wrapper.nullobject;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;

import com.helospark.sparktemplatingplugin.wrapper.SttCompilationUnit;
import com.helospark.sparktemplatingplugin.wrapper.SttType;
import com.helospark.sparktemplatingplugin.wrapper.nullobject.exception.MissingObjectException;

public class NullSttCompilationUnit
        implements SttCompilationUnit {
    private String reasonMessage;

    public NullSttCompilationUnit(String reasonMessage) {
        this.reasonMessage = reasonMessage;
    }

    @Override
    public IImportDeclaration addImport(String importToAdd) throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public IImportDeclaration addStaticImport(String importToAdd) throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public void addPackageDeclaration(String packageDeclaration) throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public SttType createEmptyClass(String className) throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public SttType createEmptyInterface(String interfaceName) throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public SttType createTypeWithContent(String content) throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public void delete(boolean force) throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public SttType findPrimaryType() {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public List<SttType> getAllTypes() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public List<IImportDeclaration> getImports() {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public List<String> getStaticImports() {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public List<String> getNonStaticImports() {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public boolean hasImport(String importName) {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public String getPackageDeclarations() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public String getSource() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public SttType getType(String typeName) {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public List<SttType> getTypes() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public void move(IJavaElement destination) throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public void rename(String newName) throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public ICompilationUnit getRaw() {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public boolean isPresent() {
        return false;
    }
}

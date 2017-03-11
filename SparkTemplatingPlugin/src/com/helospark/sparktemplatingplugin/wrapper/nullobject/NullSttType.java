package com.helospark.sparktemplatingplugin.wrapper.nullobject;

import java.util.List;

import org.eclipse.jdt.core.IInitializer;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;

import com.helospark.sparktemplatingplugin.wrapper.SttAnnotation;
import com.helospark.sparktemplatingplugin.wrapper.SttCompilationUnit;
import com.helospark.sparktemplatingplugin.wrapper.SttField;
import com.helospark.sparktemplatingplugin.wrapper.SttMethod;
import com.helospark.sparktemplatingplugin.wrapper.SttPackageFragment;
import com.helospark.sparktemplatingplugin.wrapper.SttType;
import com.helospark.sparktemplatingplugin.wrapper.SttTypeParameter;
import com.helospark.sparktemplatingplugin.wrapper.nullobject.exception.MissingObjectException;

public class NullSttType
        implements SttType {
    private String reasonMessage;

    public NullSttType(String message) {
        this.reasonMessage = message;
    }

    @Override
    public SttField createField(String typeName, String name) throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public SttMethod createMethod(String content) throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public SttType createType(String content) throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public void delete() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public SttAnnotation getAnnotation(String annotationName) {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public SttCompilationUnit getCompilationUnit() {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public SttType getDeclaringType() {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public SttField getField(String fieldName) {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public String getFullyQualifiedName() {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public String getFullyQualifiedParameterizedName() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public IInitializer getInitializer(int index) {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public IInitializer[] getInitializers() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public SttMethod getMethod(String name, String[] signature) {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public SttPackageFragment getPackageFragment() {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public String getSource() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public List<String> getSuperInterfaceNames() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public List<String> getSuperInterfaceTypeSignatures() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public String getSuperclassName() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public String getSuperclassTypeSignature() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public SttType getType(String name) {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public List<String> getTypeParameterSignatures() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public String getTypeQualifiedName() {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public boolean isAnnotation() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public boolean isAnonymous() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public boolean isClass() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public boolean isEnum() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public boolean isInterface() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public boolean isLambda() {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public boolean isLocal() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public boolean isMember() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public ITypeHierarchy newSupertypeHierarchy() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public void rename(String newName) throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public List<SttMethod> findMethods(IMethod method) {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public List<SttAnnotation> getAnnotations() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public List<SttField> getFields() {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public List<SttMethod> getMethods() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public SttTypeParameter getTypeParameter(String name) {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public List<SttTypeParameter> getTypeParameters() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public List<SttType> getTypes() throws JavaModelException {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public Object getElementName() {
        throw new MissingObjectException(reasonMessage);
    }

    @Override
    public boolean isPresent() {
        return false;
    }
}

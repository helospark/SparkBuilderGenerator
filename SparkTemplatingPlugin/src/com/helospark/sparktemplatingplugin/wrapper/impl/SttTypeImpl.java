package com.helospark.sparktemplatingplugin.wrapper.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.IInitializer;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;

import com.helospark.sparktemplatingplugin.wrapper.SttAnnotation;
import com.helospark.sparktemplatingplugin.wrapper.SttCompilationUnit;
import com.helospark.sparktemplatingplugin.wrapper.SttField;
import com.helospark.sparktemplatingplugin.wrapper.SttMethod;
import com.helospark.sparktemplatingplugin.wrapper.SttPackageFragment;
import com.helospark.sparktemplatingplugin.wrapper.SttType;
import com.helospark.sparktemplatingplugin.wrapper.SttTypeParameter;

public class SttTypeImpl extends SttJavaElementImpl<IType> implements SttType {

    public SttTypeImpl(IType type) {
        super(type);
    }

    @Override
    public SttField createField(String typeName, String name) throws JavaModelException {
        return new SttFieldImpl(wrappedElement.createField(typeName + " " + name + ";", null, false, progressMonitor));
    }

    @Override
    public SttMethod createMethod(String content) throws JavaModelException {
        return new SttMethodImpl(wrappedElement.createMethod(content, null, false, progressMonitor));
    }

    @Override
    public SttType createType(String content) throws JavaModelException {
        return new SttTypeImpl(wrappedElement.createType(content, null, false, progressMonitor));
    }

    @Override
    public void delete() throws JavaModelException {
        wrappedElement.delete(false, progressMonitor);
    }

    @Override
    public List<SttMethod> findMethods(IMethod method) {
        return Arrays.stream(wrappedElement.findMethods(method))
                .map(SttMethodImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public SttAnnotation getAnnotation(String annotationName) {
        return new SttAnnotationImpl(wrappedElement.getAnnotation(annotationName));
    }

    @Override
    public List<SttAnnotation> getAnnotations() throws JavaModelException {
        return Arrays.stream(wrappedElement.getAnnotations())
                .map(SttAnnotationImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public SttCompilationUnit getCompilationUnit() {
        return new SttCompilationUnitImpl(wrappedElement.getCompilationUnit());
    }

    @Override
    public SttType getDeclaringType() {
        return new SttTypeImpl(wrappedElement.getDeclaringType());
    }

    @Override
    public SttField getField(String fieldName) {
        return new SttFieldImpl(wrappedElement.getField(fieldName));
    }

    @Override
    public List<SttField> getFields() {
        try {
            return Arrays.stream(wrappedElement.getFields())
                    .map(SttFieldImpl::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getFullyQualifiedName() {
        return wrappedElement.getFullyQualifiedName();
    }

    @Override
    public String getFullyQualifiedParameterizedName() throws JavaModelException {
        return wrappedElement.getFullyQualifiedParameterizedName();
    }

    @Override
    public IInitializer getInitializer(int index) {
        return wrappedElement.getInitializer(index);
    }

    @Override
    public IInitializer[] getInitializers() throws JavaModelException {
        return wrappedElement.getInitializers();
    }

    @Override
    public SttMethod getMethod(String name, String[] signature) {
        return new SttMethodImpl(wrappedElement.getMethod(name, signature));
    }

    @Override
    public List<SttMethod> getMethods() throws JavaModelException {
        return Arrays.stream(wrappedElement.getMethods())
                .map(SttMethodImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public SttPackageFragment getPackageFragment() {
        return new SttPackageFragmentImpl(wrappedElement.getPackageFragment());
    }

    @Override
    public String getSource() throws JavaModelException {
        return wrappedElement.getSource();
    }

    @Override
    public List<String> getSuperInterfaceNames() throws JavaModelException {
        return Arrays.asList(wrappedElement.getSuperInterfaceNames());
    }

    @Override
    public List<String> getSuperInterfaceTypeSignatures() throws JavaModelException {
        return Arrays.asList(wrappedElement.getSuperInterfaceTypeSignatures());
    }

    @Override
    public String getSuperclassName() throws JavaModelException {
        return wrappedElement.getSuperclassName();
    }

    @Override
    public String getSuperclassTypeSignature() throws JavaModelException {
        return wrappedElement.getSuperclassTypeSignature();
    }

    @Override
    public SttType getType(String name) {
        return new SttTypeImpl(wrappedElement.getType(name));
    }

    @Override
    public SttTypeParameter getTypeParameter(String name) {
        return new SttTypeParameterImpl(wrappedElement.getTypeParameter(name));
    }

    @Override
    public List<String> getTypeParameterSignatures() throws JavaModelException {
        return Arrays.asList(wrappedElement.getTypeParameterSignatures());
    }

    @Override
    public List<SttTypeParameter> getTypeParameters() throws JavaModelException {
        return Arrays.stream(wrappedElement.getTypeParameters())
                .map(SttTypeParameterImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getTypeQualifiedName() {
        return wrappedElement.getTypeQualifiedName();
    }

    @Override
    public List<SttType> getTypes() throws JavaModelException {
        return Arrays.stream(wrappedElement.getTypes())
                .map(SttTypeImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAnnotation() throws JavaModelException {
        return wrappedElement.isAnnotation();
    }

    @Override
    public boolean isAnonymous() throws JavaModelException {
        return wrappedElement.isAnonymous();
    }

    @Override
    public boolean isClass() throws JavaModelException {
        return wrappedElement.isClass();
    }

    @Override
    public boolean isEnum() throws JavaModelException {
        return wrappedElement.isEnum();
    }

    @Override
    public boolean isInterface() throws JavaModelException {
        return wrappedElement.isInterface();
    }

    @Override
    public boolean isLambda() {
        return wrappedElement.isLambda();
    }

    @Override
    public boolean isLocal() throws JavaModelException {
        return wrappedElement.isLocal();
    }

    @Override
    public boolean isMember() throws JavaModelException {
        return wrappedElement.isMember();
    }

    @Override
    public ITypeHierarchy newSupertypeHierarchy() throws JavaModelException {
        return wrappedElement.newSupertypeHierarchy(progressMonitor);
    }

    @Override
    public void rename(String newName) throws JavaModelException {
        wrappedElement.rename(newName, false, null);
    }

}
package com.helospark.sparktemplatingplugin.wrapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.IInitializer;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;

public class SttType extends SttJavaElement<IType> {

    public SttType(IType type) {
        super(type);
    }

    public SttField createField(String typeName, String name) throws JavaModelException {
        return new SttField(wrappedElement.createField(typeName + " " + name + ";", null, false, progressMonitor));
    }

    public SttMethod createMethod(String content) throws JavaModelException {
        return new SttMethod(wrappedElement.createMethod(content, null, false, progressMonitor));
    }

    public SttType createType(String content) throws JavaModelException {
        return new SttType(wrappedElement.createType(content, null, false, progressMonitor));
    }

    public void delete() throws JavaModelException {
        wrappedElement.delete(false, progressMonitor);
    }

    public List<SttMethod> findMethods(IMethod method) {
        return Arrays.stream(wrappedElement.findMethods(method))
                .map(SttMethod::new)
                .collect(Collectors.toList());
    }

    public SttAnnotation getAnnotation(String annotationName) {
        return new SttAnnotation(wrappedElement.getAnnotation(annotationName));
    }

    public List<SttAnnotation> getAnnotations() throws JavaModelException {
        return Arrays.stream(wrappedElement.getAnnotations())
                .map(SttAnnotation::new)
                .collect(Collectors.toList());
    }

    public SttCompilationUnit getCompilationUnit() {
        return new SttCompilationUnit(wrappedElement.getCompilationUnit());
    }

    public SttType getDeclaringType() {
        return new SttType(wrappedElement.getDeclaringType());
    }

    public SttField getField(String fieldName) {
        return new SttField(wrappedElement.getField(fieldName));
    }

    public List<SttField> getFields() {
        try {
            return Arrays.stream(wrappedElement.getFields())
                    .map(SttField::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getFullyQualifiedName() {
        return wrappedElement.getFullyQualifiedName();
    }

    public String getFullyQualifiedParameterizedName() throws JavaModelException {
        return wrappedElement.getFullyQualifiedParameterizedName();
    }

    public IInitializer getInitializer(int index) {
        return wrappedElement.getInitializer(index);
    }

    public IInitializer[] getInitializers() throws JavaModelException {
        return wrappedElement.getInitializers();
    }

    public SttMethod getMethod(String name, String[] signature) {
        return new SttMethod(wrappedElement.getMethod(name, signature));
    }

    public List<SttMethod> getMethods() throws JavaModelException {
        return Arrays.stream(wrappedElement.getMethods())
                .map(SttMethod::new)
                .collect(Collectors.toList());
    }

    public SttPackageFragment getPackageFragment() {
        return new SttPackageFragment(wrappedElement.getPackageFragment());
    }

    public String getSource() throws JavaModelException {
        return wrappedElement.getSource();
    }

    public List<String> getSuperInterfaceNames() throws JavaModelException {
        return Arrays.asList(wrappedElement.getSuperInterfaceNames());
    }

    public List<String> getSuperInterfaceTypeSignatures() throws JavaModelException {
        return Arrays.asList(wrappedElement.getSuperInterfaceTypeSignatures());
    }

    public String getSuperclassName() throws JavaModelException {
        return wrappedElement.getSuperclassName();
    }

    public String getSuperclassTypeSignature() throws JavaModelException {
        return wrappedElement.getSuperclassTypeSignature();
    }

    public SttType getType(String name) {
        return new SttType(wrappedElement.getType(name));
    }

    public SttTypeParameter getTypeParameter(String name) {
        return new SttTypeParameter(wrappedElement.getTypeParameter(name));
    }

    public List<String> getTypeParameterSignatures() throws JavaModelException {
        return Arrays.asList(wrappedElement.getTypeParameterSignatures());
    }

    public List<SttTypeParameter> getTypeParameters() throws JavaModelException {
        return Arrays.stream(wrappedElement.getTypeParameters())
                .map(SttTypeParameter::new)
                .collect(Collectors.toList());
    }

    public String getTypeQualifiedName() {
        return wrappedElement.getTypeQualifiedName();
    }

    public List<SttType> getTypes() throws JavaModelException {
        return Arrays.stream(wrappedElement.getTypes())
                .map(SttType::new)
                .collect(Collectors.toList());
    }

    public boolean isAnnotation() throws JavaModelException {
        return wrappedElement.isAnnotation();
    }

    public boolean isAnonymous() throws JavaModelException {
        return wrappedElement.isAnonymous();
    }

    public boolean isClass() throws JavaModelException {
        return wrappedElement.isClass();
    }

    public boolean isEnum() throws JavaModelException {
        return wrappedElement.isEnum();
    }

    public boolean isInterface() throws JavaModelException {
        return wrappedElement.isInterface();
    }

    public boolean isLambda() {
        return wrappedElement.isLambda();
    }

    public boolean isLocal() throws JavaModelException {
        return wrappedElement.isLocal();
    }

    public boolean isMember() throws JavaModelException {
        return wrappedElement.isMember();
    }

    public ITypeHierarchy newSupertypeHierarchy() throws JavaModelException {
        return wrappedElement.newSupertypeHierarchy(progressMonitor);
    }

    public void rename(String newName) throws JavaModelException {
        wrappedElement.rename(newName, false, null);
    }

}
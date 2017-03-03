package com.helospark.sparktemplatingplugin.wrapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IInitializer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;

public class SttType {
    private IType type;

    private IProgressMonitor progressMonitor = new NullProgressMonitor();

    public SttType(IType type) {
        this.type = type;
    }

    public IField createField(String typeName, String name) throws JavaModelException {
        return type.createField(typeName + " " + name + ";", null, false, progressMonitor);
    }

    public IMethod createMethod(String content) throws JavaModelException {
        return type.createMethod(content, null, false, progressMonitor);
    }

    public IType createType(String content) throws JavaModelException {
        return type.createType(content, null, false, progressMonitor);
    }

    public void delete() throws JavaModelException {
        type.delete(false, progressMonitor);
    }

    public List<SttMethod> findMethods(IMethod method) {
        return Arrays.stream(type.findMethods(method))
                .map(SttMethod::new)
                .collect(Collectors.toList());
    }

    public IAnnotation getAnnotation(String annotationName) {
        return type.getAnnotation(annotationName);
    }

    public List<SttAnnotation> getAnnotations() throws JavaModelException {
        return Arrays.stream(type.getAnnotations())
                .map(SttAnnotation::new)
                .collect(Collectors.toList());
    }

    public String getAttachedJavadoc() throws JavaModelException {
        return type.getAttachedJavadoc(progressMonitor);
    }

    public SttCompilationUnit getCompilationUnit() {
        return new SttCompilationUnit(type.getCompilationUnit());
    }

    public SttType getDeclaringType() {
        return new SttType(type.getDeclaringType());
    }

    public String getElementName() {
        return type.getElementName();
    }

    public SttField getField(String fieldName) {
        return new SttField(type.getField(fieldName));
    }

    public List<SttField> getFields() throws JavaModelException {
        return Arrays.stream(type.getFields())
                .map(SttField::new)
                .collect(Collectors.toList());
    }

    public String getFullyQualifiedName() {
        return type.getFullyQualifiedName();
    }

    public String getFullyQualifiedParameterizedName() throws JavaModelException {
        return type.getFullyQualifiedParameterizedName();
    }

    public IInitializer getInitializer(int index) {
        return type.getInitializer(index);
    }

    public IInitializer[] getInitializers() throws JavaModelException {
        return type.getInitializers();
    }

    public IJavaProject getJavaProject() {
        return type.getJavaProject();
    }

    public SttMethod getMethod(String name, String[] signature) {
        return new SttMethod(type.getMethod(name, signature));
    }

    public List<SttMethod> getMethods() throws JavaModelException {
        return Arrays.stream(type.getMethods())
                .map(SttMethod::new)
                .collect(Collectors.toList());
    }

    public SttPackageFragment getPackageFragment() {
        return new SttPackageFragment(type.getPackageFragment());
    }

    //    public IJavaElement getParent() {
    //        return type.getParent();
    //    }

    public String getSource() throws JavaModelException {
        return type.getSource();
    }

    public List<String> getSuperInterfaceNames() throws JavaModelException {
        return Arrays.asList(type.getSuperInterfaceNames());
    }

    public List<String> getSuperInterfaceTypeSignatures() throws JavaModelException {
        return Arrays.asList(type.getSuperInterfaceTypeSignatures());
    }

    public String getSuperclassName() throws JavaModelException {
        return type.getSuperclassName();
    }

    public String getSuperclassTypeSignature() throws JavaModelException {
        return type.getSuperclassTypeSignature();
    }

    public SttType getType(String name) {
        return new SttType(type.getType(name));
    }

    //    public ITypeParameter getTypeParameter(String arg0) {
    //        return type.getTypeParameter(arg0);
    //    }

    public List<String> getTypeParameterSignatures() throws JavaModelException {
        return Arrays.asList(type.getTypeParameterSignatures());
    }

    //    public ITypeParameter[] getTypeParameters() throws JavaModelException {
    //        return type.getTypeParameters();
    //    }

    public String getTypeQualifiedName() {
        return type.getTypeQualifiedName();
    }

    public ITypeRoot getTypeRoot() {
        return type.getTypeRoot();
    }

    public List<SttType> getTypes() throws JavaModelException {
        return Arrays.stream(type.getTypes())
                .map(SttType::new)
                .collect(Collectors.toList());
    }

    public boolean isAnnotation() throws JavaModelException {
        return type.isAnnotation();
    }

    public boolean isAnonymous() throws JavaModelException {
        return type.isAnonymous();
    }

    public boolean isBinary() {
        return type.isBinary();
    }

    public boolean isClass() throws JavaModelException {
        return type.isClass();
    }

    public boolean isEnum() throws JavaModelException {
        return type.isEnum();
    }

    public boolean isInterface() throws JavaModelException {
        return type.isInterface();
    }

    public boolean isLambda() {
        return type.isLambda();
    }

    public boolean isLocal() throws JavaModelException {
        return type.isLocal();
    }

    public boolean isMember() throws JavaModelException {
        return type.isMember();
    }

    public boolean isReadOnly() {
        return type.isReadOnly();
    }

    public boolean isResolved() {
        return type.isResolved();
    }

    //    public void move(IJavaElement arg0, IJavaElement arg1, String arg2, boolean arg3, IProgressMonitor arg4) throws JavaModelException {
    //        type.move(arg0, arg1, arg2, arg3, arg4);
    //    }

    public ITypeHierarchy newSupertypeHierarchy() throws JavaModelException {
        return type.newSupertypeHierarchy(progressMonitor);
    }

    public void rename(String newName) throws JavaModelException {
        type.rename(newName, false, null);
    }

    public IType getRawIType() {
        return type;
    }
}

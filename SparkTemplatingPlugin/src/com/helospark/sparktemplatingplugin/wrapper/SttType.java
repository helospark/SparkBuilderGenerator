package com.helospark.sparktemplatingplugin.wrapper;

import java.util.List;

import org.eclipse.jdt.core.IInitializer;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;

public interface SttType {
    SttField createField(String typeName, String name) throws JavaModelException;

    SttMethod createMethod(String content) throws JavaModelException;

    SttType createType(String content) throws JavaModelException;

    void delete() throws JavaModelException;

    List<SttMethod> findMethods(IMethod method);

    SttAnnotation getAnnotation(String annotationName);

    List<SttAnnotation> getAnnotations() throws JavaModelException;

    SttCompilationUnit getCompilationUnit();

    SttType getDeclaringType();

    SttField getField(String fieldName);

    List<SttField> getFields();

    String getFullyQualifiedName();

    String getFullyQualifiedParameterizedName() throws JavaModelException;

    IInitializer getInitializer(int index);

    IInitializer[] getInitializers() throws JavaModelException;

    SttMethod getMethod(String name, String[] signature);

    List<SttMethod> getMethods() throws JavaModelException;

    SttPackageFragment getPackageFragment();

    String getSource() throws JavaModelException;

    List<String> getSuperInterfaceNames() throws JavaModelException;

    List<String> getSuperInterfaceTypeSignatures() throws JavaModelException;

    String getSuperclassName() throws JavaModelException;

    String getSuperclassTypeSignature() throws JavaModelException;

    SttType getType(String name);

    SttTypeParameter getTypeParameter(String name);

    List<String> getTypeParameterSignatures() throws JavaModelException;

    List<SttTypeParameter> getTypeParameters() throws JavaModelException;

    String getTypeQualifiedName();

    List<SttType> getTypes() throws JavaModelException;

    boolean isAnnotation() throws JavaModelException;

    boolean isAnonymous() throws JavaModelException;

    boolean isClass() throws JavaModelException;

    boolean isEnum() throws JavaModelException;

    boolean isInterface() throws JavaModelException;

    boolean isLambda();

    boolean isLocal() throws JavaModelException;

    boolean isMember() throws JavaModelException;

    ITypeHierarchy newSupertypeHierarchy() throws JavaModelException;

    void rename(String newName) throws JavaModelException;

    Object getElementName();

    boolean isPresent();
}
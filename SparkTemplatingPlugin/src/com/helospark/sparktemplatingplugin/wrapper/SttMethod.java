package com.helospark.sparktemplatingplugin.wrapper;

import java.util.List;

import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.JavaModelException;

public interface SttMethod {

    void delete() throws JavaModelException;

    SttAnnotation getAnnotation(String annotationName);

    List<SttAnnotation> getAnnotations() throws JavaModelException;

    SttCompilationUnit getCompilationUnit();

    SttType getDeclaringType();

    IMemberValuePair getDefaultValue() throws JavaModelException;

    List<String> getExceptionTypes() throws JavaModelException;

    int getNumberOfParameters();

    List<String> getParameterNames() throws JavaModelException;

    List<String> getParameterTypes();

    List<SttLocalVariable> getParameters() throws JavaModelException;

    List<String> getRawParameterNames() throws JavaModelException;

    String getSignature() throws JavaModelException;

    String getSource() throws JavaModelException;

    SttTypeParameter getTypeParameter(String paramString);

    List<SttTypeParameter> getTypeParameters() throws JavaModelException;

    boolean isConstructor() throws JavaModelException;

    boolean isLambdaMethod();

    boolean isMainMethod() throws JavaModelException;

    void rename(String newName) throws JavaModelException;

    String getReturnType() throws JavaModelException;

    boolean isPresent();
}
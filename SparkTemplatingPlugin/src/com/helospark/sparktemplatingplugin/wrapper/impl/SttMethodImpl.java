package com.helospark.sparktemplatingplugin.wrapper.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import com.helospark.sparktemplatingplugin.wrapper.SttAnnotation;
import com.helospark.sparktemplatingplugin.wrapper.SttCompilationUnit;
import com.helospark.sparktemplatingplugin.wrapper.SttLocalVariable;
import com.helospark.sparktemplatingplugin.wrapper.SttMethod;
import com.helospark.sparktemplatingplugin.wrapper.SttType;
import com.helospark.sparktemplatingplugin.wrapper.SttTypeParameter;

public class SttMethodImpl extends SttJavaElementImpl<IMethod> implements SttMethod {

    public SttMethodImpl(IMethod method) {
        super(method);
    }

    @Override
    public void delete() throws JavaModelException {
        wrappedElement.delete(false, progressMonitor);
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
    public IMemberValuePair getDefaultValue() throws JavaModelException {
        return wrappedElement.getDefaultValue();
    }

    @Override
    public List<String> getExceptionTypes() throws JavaModelException {
        return Arrays.asList(wrappedElement.getExceptionTypes());
    }

    @Override
    public int getNumberOfParameters() {
        return wrappedElement.getNumberOfParameters();
    }

    @Override
    public List<String> getParameterNames() throws JavaModelException {
        return Arrays.asList(wrappedElement.getParameterNames());
    }

    @Override
    public List<String> getParameterTypes() {
        return Arrays.asList(wrappedElement.getParameterTypes());
    }

    @Override
    public List<SttLocalVariable> getParameters() throws JavaModelException {
        return Arrays.stream(wrappedElement.getParameters())
                .map(SttLocalVariableImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getRawParameterNames() throws JavaModelException {
        return Arrays.asList(wrappedElement.getRawParameterNames());
    }

    @Override
    public String getReturnType() throws JavaModelException {
        return wrappedElement.getReturnType();
    }

    @Override
    public String getSignature() throws JavaModelException {
        return wrappedElement.getSignature();
    }

    @Override
    public String getSource() throws JavaModelException {
        return wrappedElement.getSource();
    }

    @Override
    public SttTypeParameter getTypeParameter(String paramString) {
        return new SttTypeParameterImpl(wrappedElement.getTypeParameter(paramString));
    }

    @Override
    public List<SttTypeParameter> getTypeParameters() throws JavaModelException {
        return Arrays.stream(wrappedElement.getTypeParameters())
                .map(SttTypeParameterImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isConstructor() throws JavaModelException {
        return wrappedElement.isConstructor();
    }

    @Override
    public boolean isLambdaMethod() {
        return wrappedElement.isLambdaMethod();
    }

    @Override
    public boolean isMainMethod() throws JavaModelException {
        return wrappedElement.isMainMethod();
    }

    @Override
    public void rename(String newName) throws JavaModelException {
        wrappedElement.rename(newName, false, progressMonitor);
    }

}

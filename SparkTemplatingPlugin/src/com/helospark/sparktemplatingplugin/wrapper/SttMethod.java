package com.helospark.sparktemplatingplugin.wrapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

public class SttMethod extends SttJavaElement<IMethod> {

    public SttMethod(IMethod method) {
        super(method);
    }

    public void delete() throws JavaModelException {
        wrappedElement.delete(false, progressMonitor);
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

    public IMemberValuePair getDefaultValue() throws JavaModelException {
        return wrappedElement.getDefaultValue();
    }

    public List<String> getExceptionTypes() throws JavaModelException {
        return Arrays.asList(wrappedElement.getExceptionTypes());
    }

    public int getNumberOfParameters() {
        return wrappedElement.getNumberOfParameters();
    }

    public List<String> getParameterNames() throws JavaModelException {
        return Arrays.asList(wrappedElement.getParameterNames());
    }

    public List<String> getParameterTypes() {
        return Arrays.asList(wrappedElement.getParameterTypes());
    }

    public List<SttLocalVariable> getParameters() throws JavaModelException {
        return Arrays.stream(wrappedElement.getParameters())
                .map(SttLocalVariable::new)
                .collect(Collectors.toList());
    }

    public List<String> getRawParameterNames() throws JavaModelException {
        return Arrays.asList(wrappedElement.getRawParameterNames());
    }

    public String getReturnType() throws JavaModelException {
        return wrappedElement.getReturnType();
    }

    public String getSignature() throws JavaModelException {
        return wrappedElement.getSignature();
    }

    public String getSource() throws JavaModelException {
        return wrappedElement.getSource();
    }

    public SttTypeParameter getTypeParameter(String paramString) {
        return new SttTypeParameter(wrappedElement.getTypeParameter(paramString));
    }

    public List<SttTypeParameter> getTypeParameters() throws JavaModelException {
        return Arrays.stream(wrappedElement.getTypeParameters())
                .map(SttTypeParameter::new)
                .collect(Collectors.toList());
    }

    public boolean isConstructor() throws JavaModelException {
        return wrappedElement.isConstructor();
    }

    public boolean isLambdaMethod() {
        return wrappedElement.isLambdaMethod();
    }

    public boolean isMainMethod() throws JavaModelException {
        return wrappedElement.isMainMethod();
    }

    public void rename(String newName) throws JavaModelException {
        wrappedElement.rename(newName, false, progressMonitor);
    }

}

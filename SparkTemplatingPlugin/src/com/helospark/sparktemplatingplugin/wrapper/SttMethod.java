package com.helospark.sparktemplatingplugin.wrapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;

public class SttMethod {
    private IMethod method;

    private IProgressMonitor progressMonitor = new NullProgressMonitor();

    public SttMethod(IMethod method) {
        this.method = method;
    }

    public void copy(IJavaElement paramIJavaElement1, IJavaElement paramIJavaElement2, String paramString, boolean paramBoolean, IProgressMonitor paramIProgressMonitor)
            throws JavaModelException {
        method.copy(paramIJavaElement1, paramIJavaElement2, paramString, paramBoolean, paramIProgressMonitor);
    }

    public void delete() throws JavaModelException {
        method.delete(false, progressMonitor);
    }

    public IJavaElement getAncestor(int paramInt) {
        return method.getAncestor(paramInt);
    }

    public SttAnnotation getAnnotation(String annotationName) {
        return new SttAnnotation(method.getAnnotation(annotationName));
    }

    public List<SttAnnotation> getAnnotations() throws JavaModelException {
        return Arrays.stream(method.getAnnotations())
                .map(SttAnnotation::new)
                .collect(Collectors.toList());
    }

    public String getAttachedJavadoc() throws JavaModelException {
        return method.getAttachedJavadoc(progressMonitor);
    }

    public SttCompilationUnit getCompilationUnit() {
        return new SttCompilationUnit(method.getCompilationUnit());
    }

    public SttType getDeclaringType() {
        return new SttType(method.getDeclaringType());
    }

    public IMemberValuePair getDefaultValue() throws JavaModelException {
        return method.getDefaultValue();
    }

    public String getElementName() {
        return method.getElementName();
    }

    public String[] getExceptionTypes() throws JavaModelException {
        return method.getExceptionTypes();
    }

    public IJavaProject getJavaProject() {
        return method.getJavaProject();
    }

    public int getNumberOfParameters() {
        return method.getNumberOfParameters();
    }

    public String[] getParameterNames() throws JavaModelException {
        return method.getParameterNames();
    }

    public String[] getParameterTypes() {
        return method.getParameterTypes();
    }

    public ILocalVariable[] getParameters() throws JavaModelException {
        return method.getParameters();
    }

    public String[] getRawParameterNames() throws JavaModelException {
        return method.getRawParameterNames();
    }

    public String getReturnType() throws JavaModelException {
        return method.getReturnType();
    }

    public String getSignature() throws JavaModelException {
        return method.getSignature();
    }

    public String getSource() throws JavaModelException {
        return method.getSource();
    }

    public ISourceRange getSourceRange() throws JavaModelException {
        return method.getSourceRange();
    }

    public SttType getType(String paramString, int paramInt) {
        return new SttType(method.getType(paramString, paramInt));
    }

    public ITypeParameter getTypeParameter(String paramString) {
        return method.getTypeParameter(paramString);
    }

    public ITypeParameter[] getTypeParameters() throws JavaModelException {
        return method.getTypeParameters();
    }

    public ITypeRoot getTypeRoot() {
        return method.getTypeRoot();
    }

    public boolean isBinary() {
        return method.isBinary();
    }

    public boolean isConstructor() throws JavaModelException {
        return method.isConstructor();
    }

    public boolean isLambdaMethod() {
        return method.isLambdaMethod();
    }

    public boolean isMainMethod() throws JavaModelException {
        return method.isMainMethod();
    }

    public boolean isReadOnly() {
        return method.isReadOnly();
    }

    public boolean isResolved() {
        return method.isResolved();
    }

    public boolean isSimilar(IMethod paramIMethod) {
        return method.isSimilar(paramIMethod);
    }

    //    public void move(IJavaElement paramIJavaElement1, IJavaElement paramIJavaElement2, String paramString, boolean paramBoolean, IProgressMonitor paramIProgressMonitor)
    //            throws JavaModelException {
    //        method.move(paramIJavaElement1, paramIJavaElement2, paramString, paramBoolean, paramIProgressMonitor);
    //    }

    public void rename(String newName) throws JavaModelException {
        method.rename(newName, false, progressMonitor);
    }

}

package com.helospark.sparktemplatingplugin.wrapper;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

public class SttField {
    private IField field;

    private IProgressMonitor progressMonitor = new NullProgressMonitor();

    public SttField(IField field) {
        this.field = field;
    }

    public IAnnotation getAnnotation(String paramString) {
        return field.getAnnotation(paramString);
    }

    public void copy(IJavaElement paramIJavaElement1, IJavaElement paramIJavaElement2, String paramString, boolean paramBoolean, IProgressMonitor paramIProgressMonitor)
            throws JavaModelException {
        field.copy(paramIJavaElement1, paramIJavaElement2, paramString, paramBoolean, paramIProgressMonitor);
    }

    public IAnnotation[] getAnnotations() throws JavaModelException {
        return field.getAnnotations();
    }

    public SttCompilationUnit getCompilationUnit() {
        return new SttCompilationUnit(field.getCompilationUnit());
    }

    public SttType getDeclaringType() {
        return new SttType(field.getDeclaringType());
    }

    public void delete() throws JavaModelException {
        field.delete(false, progressMonitor);
    }

    public IJavaElement getAncestor(int paramInt) {
        return field.getAncestor(paramInt);
    }

    public String getAttachedJavadoc(IProgressMonitor paramIProgressMonitor) throws JavaModelException {
        return field.getAttachedJavadoc(paramIProgressMonitor);
    }

    public Object getConstant() throws JavaModelException {
        return field.getConstant();
    }

    public String getElementName() {
        return field.getElementName();
    }

    public IJavaProject getJavaProject() {
        return field.getJavaProject();
    }

    public String getSource() throws JavaModelException {
        return field.getSource();
    }

    public SttType getType(String paramString, int paramInt) {
        return new SttType(field.getType(paramString, paramInt));
    }

    public String getTypeSignature() throws JavaModelException {
        return field.getTypeSignature();
    }

    public boolean isEnumConstant() throws JavaModelException {
        return field.isEnumConstant();
    }

    //    public void move(IJavaElement paramIJavaElement1, IJavaElement paramIJavaElement2, String paramString, boolean paramBoolean, IProgressMonitor paramIProgressMonitor)
    //            throws JavaModelException {
    //        field.move(paramIJavaElement1, paramIJavaElement2, paramString, paramBoolean, paramIProgressMonitor);
    //    }

    public void rename(String newName) throws JavaModelException {
        field.rename(newName, false, progressMonitor);
    }

}

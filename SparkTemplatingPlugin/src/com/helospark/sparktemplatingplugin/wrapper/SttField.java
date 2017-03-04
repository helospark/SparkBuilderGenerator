package com.helospark.sparktemplatingplugin.wrapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;

public class SttField extends SttJavaElement<IField> {
    public SttField(IField field) {
        super(field);
    }

    public SttAnnotation getAnnotation(String paramString) {
        return new SttAnnotation(wrappedElement.getAnnotation(paramString));
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

    public void delete() throws JavaModelException {
        wrappedElement.delete(false, progressMonitor);
    }

    public String getAttachedJavadoc(IProgressMonitor paramIProgressMonitor) throws JavaModelException {
        return wrappedElement.getAttachedJavadoc(paramIProgressMonitor);
    }

    public Object getConstant() throws JavaModelException {
        return wrappedElement.getConstant();
    }

    @Override
    public String getElementName() {
        return wrappedElement.getElementName();
    }

    public String getSource() throws JavaModelException {
        return wrappedElement.getSource();
    }

    public SttType getType(String paramString, int paramInt) {
        return new SttType(wrappedElement.getType(paramString, paramInt));
    }

    public String getTypeSignature() {
        try {
            return wrappedElement.getTypeSignature();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isEnumConstant() throws JavaModelException {
        return wrappedElement.isEnumConstant();
    }

    public void rename(String newName) throws JavaModelException {
        wrappedElement.rename(newName, false, progressMonitor);
    }

}

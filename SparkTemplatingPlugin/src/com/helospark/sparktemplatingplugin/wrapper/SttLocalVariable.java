package com.helospark.sparktemplatingplugin.wrapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.JavaModelException;

public class SttLocalVariable extends SttJavaElement<ILocalVariable> {
    public SttLocalVariable(ILocalVariable localVariable) {
        super(localVariable);
    }

    public SttAnnotation getAnnotation(String annotationName) {
        return new SttAnnotation(wrappedElement.getAnnotation(annotationName));
    }

    public List<SttAnnotation> getAnnotations() throws JavaModelException {
        return Arrays.stream(wrappedElement.getAnnotations())
                .map(SttAnnotation::new)
                .collect(Collectors.toList());
    }

    public String getAttachedJavadoc(IProgressMonitor paramIProgressMonitor) throws JavaModelException {
        return wrappedElement.getAttachedJavadoc(paramIProgressMonitor);
    }

    public SttMember getDeclaringMember() {
        return new SttMember(wrappedElement.getDeclaringMember());
    }

    public String getSource() throws JavaModelException {
        return wrappedElement.getSource();
    }

    public String getTypeSignature() {
        return wrappedElement.getTypeSignature();
    }

    public boolean isParameter() {
        return wrappedElement.isParameter();
    }
}

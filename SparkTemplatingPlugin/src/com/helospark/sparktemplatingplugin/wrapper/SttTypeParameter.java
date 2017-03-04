package com.helospark.sparktemplatingplugin.wrapper;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaModelException;

public class SttTypeParameter extends SttJavaElement<ITypeParameter> {

    public SttTypeParameter(ITypeParameter typeParameter) {
        super(typeParameter);
    }

    public List<String> getBounds() throws JavaModelException {
        return Arrays.asList(wrappedElement.getBounds());
    }

    public List<String> getBoundsSignatures() throws JavaModelException {
        return Arrays.asList(wrappedElement.getBoundsSignatures());
    }

    public SttMember getDeclaringMember() {
        return new SttMember(wrappedElement.getDeclaringMember());
    }

    public String getAttachedJavadoc(IProgressMonitor paramIProgressMonitor) throws JavaModelException {
        return wrappedElement.getAttachedJavadoc(paramIProgressMonitor);
    }

    public String getSource() throws JavaModelException {
        return wrappedElement.getSource();
    }
}
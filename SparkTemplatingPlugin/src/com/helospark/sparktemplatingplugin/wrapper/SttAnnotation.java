package com.helospark.sparktemplatingplugin.wrapper;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.JavaModelException;

public class SttAnnotation extends SttJavaElement<IAnnotation> {

    public SttAnnotation(IAnnotation annotation) {
        super(annotation);
    }

    @Override
    public String getAttachedJavadoc() throws JavaModelException {
        return wrappedElement.getAttachedJavadoc(progressMonitor);
    }

    @Override
    public String getElementName() {
        return wrappedElement.getElementName();
    }

    @Override
    public SttJavaProject getJavaProject() {
        return new SttJavaProject(wrappedElement.getJavaProject());
    }

    public List<IMemberValuePair> getMemberValuePairs() throws JavaModelException {
        return Arrays.asList(wrappedElement.getMemberValuePairs());
    }

    public String getSource() throws JavaModelException {
        return wrappedElement.getSource();
    }

}

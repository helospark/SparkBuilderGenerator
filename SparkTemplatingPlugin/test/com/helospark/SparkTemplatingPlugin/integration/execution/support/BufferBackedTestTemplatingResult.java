package com.helospark.SparkTemplatingPlugin.integration.execution.support;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.BadLocationException;

import com.helospark.sparktemplatingplugin.execute.templater.ITemplatingResult;

public class BufferBackedTestTemplatingResult implements ITemplatingResult {
    private StringBuffer stringBuffer = new StringBuffer();

    @Override
    public void append(String data) {
        stringBuffer.append(data);
    }

    @Override
    public void appendToCurrentPosition() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void appendToNewFile(IProject iProject, String folder) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void appendToMethod(ICompilationUnit iCompilationUnit, String program, CompilationUnit astRoot) throws JavaModelException, BadLocationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clearBuffer() {
        stringBuffer.setLength(0);
    }

    @Override
    public String getBufferContent() {
        return stringBuffer.toString();
    }

    @Override
    public String getExposedName() {
        return "result";
    }

    @Override
    public String getDocumentation() {
        throw new UnsupportedOperationException();
    }

}

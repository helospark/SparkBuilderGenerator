package com.helospark.sparktemplatingplugin.execute.templater;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.BadLocationException;

public interface ITemplatingResult {

    void append(String data);

    void appendToCurrentPosition();

    void appendToNewFile(IProject iProject, String folder);

    void appendToMethod(ICompilationUnit iCompilationUnit, String program, CompilationUnit astRoot) throws JavaModelException, BadLocationException;

    void clearBuffer();

    String getBufferContent();

    String getExposedName();

    String getDocumentation();

}
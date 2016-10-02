package com.helospark.spark.converter.handlers.service;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

public class ClassTypeAppender {

    public void addType(ICompilationUnit iCompilationUnit, CompilationUnit compilationUnit, TypeDeclaration newType)
            throws JavaModelException, BadLocationException {
        ASTRewrite rewriter = ASTRewrite.create(compilationUnit.getAST());
        ListRewrite listRewriter = rewriter.getListRewrite(compilationUnit, CompilationUnit.TYPES_PROPERTY);
        listRewriter.insertLast(newType, null);
        Document document = new Document(iCompilationUnit.getSource());
        TextEdit edits = rewriter.rewriteAST(document, null);
        edits.apply(document);
        String newSource = document.get();
        iCompilationUnit.getBuffer().setContents(newSource);
        iCompilationUnit.getBuffer().save(new NullProgressMonitor(), true);
    }
}

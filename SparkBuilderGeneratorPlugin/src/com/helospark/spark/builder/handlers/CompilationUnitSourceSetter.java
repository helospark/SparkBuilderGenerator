package com.helospark.spark.builder.handlers;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

public class CompilationUnitSourceSetter {

    public void commitCodeChange(ICompilationUnit iCompilationUnit, ASTRewrite rewriter) {
        try {
            Document document = new Document(iCompilationUnit.getSource());
            TextEdit edits = rewriter.rewriteAST(document, null);
            edits.apply(document);
            iCompilationUnit.getBuffer().setContents(document.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

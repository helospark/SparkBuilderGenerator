package com.helospark.spark.converter.handlers.service.emitter.helper;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;

public class ImportAppender {

    public void addImportsToAst(CompilationUnitModificationDomain converterCompilationUnit) {
        try {
            CompilationUnit compilationUnit = converterCompilationUnit.getCompilationUnit();
            ICompilationUnit iCompilationUnit = converterCompilationUnit.getiCompilationUnit();
            AST ast = converterCompilationUnit.getAst();

            ASTRewrite rewriter = ASTRewrite.create(ast);
            ListRewrite listRewriter = rewriter.getListRewrite(compilationUnit, CompilationUnit.IMPORTS_PROPERTY);

            for (ImportDeclaration importDeclaration : converterCompilationUnit.getImports()) {
                listRewriter.insertLast(importDeclaration, null);
            }

            Document document = new Document(iCompilationUnit.getSource());
            TextEdit edits = rewriter.rewriteAST(document, null);
            edits.apply(document);
            String newSource = document.get();
            iCompilationUnit.getBuffer().setContents(newSource);
            iCompilationUnit.getBuffer().save(new NullProgressMonitor(), true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

package com.helospark.spark.converter.handlers.service.emitter.helper;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;

public class CommentAppender {

    public void appendComments(CompilationUnitModificationDomain converterCompilationUnit, TypeDeclaration converterClassType) {
        try {
            CompilationUnit compilationUnit = converterCompilationUnit.getCompilationUnit();
            ICompilationUnit iCompilationUnit = converterCompilationUnit.getiCompilationUnit();
            AST ast = converterCompilationUnit.getAst();

            for (MethodDeclaration methodDecl : converterClassType.getMethods()) {
                ASTRewrite rewriter = ASTRewrite.create(ast);
                // ListRewrite listRewriter =
                // rewriter.getListRewrite(method.getBody(),
                // Block.STATEMENTS_PROPERTY);
                // Statement placeHolder = (Statement)
                // rewriter.createStringPlaceholder("//mycomment",
                // ASTNode.EMPTY_STATEMENT);
                // // (ASTNode) method.getBody().statements().get(0)
                // listRewriter.insertLast(placeHolder, null);
                //
                // TextEdit edits = rewriter.rewriteAST();
                // Document document = new
                // Document(iCompilationUnit.getSource());
                // edits.apply(document);
                // String newSource = document.get();
                // iCompilationUnit.getBuffer().setContents(newSource);
                // iCompilationUnit.getBuffer().save(new NullProgressMonitor(),
                // true);
                Block block = methodDecl.getBody();

                ListRewrite listRewrite = rewriter.getListRewrite(block, Block.STATEMENTS_PROPERTY);
                Statement placeHolder = (Statement) rewriter.createStringPlaceholder("//mycomment", ASTNode.EMPTY_STATEMENT);
                listRewrite.insertFirst(placeHolder, null);

                TextEdit edits = rewriter.rewriteAST();

                // apply the text edits to the compilation unit
                Document document = new Document(iCompilationUnit.getSource());

                edits.apply(document);

                // this is the code for adding statements
                iCompilationUnit.getBuffer().setContents(document.get());

                System.out.println("done");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

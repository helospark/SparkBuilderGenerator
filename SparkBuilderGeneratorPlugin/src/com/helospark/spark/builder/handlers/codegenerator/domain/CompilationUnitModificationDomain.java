package com.helospark.spark.builder.handlers.codegenerator.domain;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

public class CompilationUnitModificationDomain {
    private ListRewrite listRewrite;
    private AST ast;
    private TypeDeclaration originalType;

    public CompilationUnitModificationDomain(ListRewrite listRewrite, AST ast, TypeDeclaration originalType) {
        this.listRewrite = listRewrite;
        this.ast = ast;
        this.originalType = originalType;
    }

    public ListRewrite getListRewrite() {
        return listRewrite;
    }

    public AST getAst() {
        return ast;
    }

    public TypeDeclaration getOriginalType() {
        return originalType;
    }

}

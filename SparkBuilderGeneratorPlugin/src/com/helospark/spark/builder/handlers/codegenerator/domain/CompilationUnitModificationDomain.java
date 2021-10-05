package com.helospark.spark.builder.handlers.codegenerator.domain;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

/**
 * Contains all data necessary to modify a compilation unit.
 * TODO: Better name is needed
 * TODO: Should be replaced, where it is not yet used
 * @author helospark
 */
public class CompilationUnitModificationDomain {
    private ListRewrite listRewrite;
    private ASTRewrite astRewriter;
    private AST ast;
    private TypeDeclaration originalType;
    private CompilationUnit compilationUnit;
    private List<MethodDeclaration> savedCustomMethodDeclarations = new ArrayList<>();

    @Generated("SparkTools")
    private CompilationUnitModificationDomain(Builder builder) {
        this.listRewrite = builder.listRewrite;
        this.astRewriter = builder.astRewriter;
        this.ast = builder.ast;
        this.originalType = builder.originalType;
        this.compilationUnit = builder.compilationUnit;
    }

    public void addSavedMethodDeclaration(MethodDeclaration methodDeclaration) {
        this.savedCustomMethodDeclarations.add(methodDeclaration);
    }

    public List<MethodDeclaration> getSavedCustomMethodDeclarations() {
        return savedCustomMethodDeclarations;
    }

    public ListRewrite getListRewrite() {
        return listRewrite;
    }

    public ASTRewrite getAstRewriter() {
        return astRewriter;
    }

    public AST getAst() {
        return ast;
    }

    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    public TypeDeclaration getOriginalType() {
        return originalType;
    }

    @Generated("SparkTools")
    public static Builder builder() {
        return new Builder();
    }

    @Generated("SparkTools")
    public static final class Builder {
        private ListRewrite listRewrite;
        private ASTRewrite astRewriter;
        private AST ast;
        private TypeDeclaration originalType;
        private CompilationUnit compilationUnit;

        private Builder() {
        }

        public Builder withListRewrite(ListRewrite listRewrite) {
            this.listRewrite = listRewrite;
            return this;
        }

        public Builder withAstRewriter(ASTRewrite astRewriter) {
            this.astRewriter = astRewriter;
            return this;
        }

        public Builder withAst(AST ast) {
            this.ast = ast;
            return this;
        }

        public Builder withOriginalType(TypeDeclaration originalType) {
            this.originalType = originalType;
            return this;
        }

        public Builder withCompilationUnit(CompilationUnit compilationUnit) {
            this.compilationUnit = compilationUnit;
            return this;
        }

        public CompilationUnitModificationDomain build() {
            return new CompilationUnitModificationDomain(this);
        }
    }

}

package com.helospark.spark.converter.handlers.service.common.domain;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;

public class CompilationUnitModificationDomain {
    private AST ast;
    private ICompilationUnit iCompilationUnit;
    private CompilationUnit compilationUnit;
    private List<ImportDeclaration> imports = new ArrayList<>();

    @Generated("SparkTools")
    private CompilationUnitModificationDomain(Builder builder) {
        this.ast = builder.ast;
        this.iCompilationUnit = builder.iCompilationUnit;
        this.compilationUnit = builder.compilationUnit;
    }

    public AST getAst() {
        return ast;
    }

    public ICompilationUnit getiCompilationUnit() {
        return iCompilationUnit;
    }

    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    public void addImport(ImportDeclaration importDeclaration) {
        this.imports.add(importDeclaration);
    }

    public List<ImportDeclaration> getImports() {
        return imports;
    }

    @Generated("SparkTools")
    public static Builder builder() {
        return new Builder();
    }

    @Generated("SparkTools")
    public static final class Builder {
        private AST ast;
        private ICompilationUnit iCompilationUnit;
        private CompilationUnit compilationUnit;

        private Builder() {
        }

        public Builder withAst(AST ast) {
            this.ast = ast;
            return this;
        }

        public Builder withICompilationUnit(ICompilationUnit iCompilationUnit) {
            this.iCompilationUnit = iCompilationUnit;
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

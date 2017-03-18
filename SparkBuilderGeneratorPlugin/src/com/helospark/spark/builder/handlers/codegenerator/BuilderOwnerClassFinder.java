package com.helospark.spark.builder.handlers.codegenerator;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;
import com.helospark.spark.builder.handlers.exception.PluginException;

public class BuilderOwnerClassFinder {

    public CompilationUnitModificationDomain asd(CompilationUnit compilationUnit, AST ast, ASTRewrite rewriter) {
        List types = compilationUnit.types();
        if (types == null || types.size() == 0) {
            throw new PluginException("No types are present in the current java file");
        }
        TypeDeclaration originalType = (TypeDeclaration) types.get(0);
        ListRewrite listRewrite = rewriter.getListRewrite(originalType, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);

        return new CompilationUnitModificationDomain(listRewrite, ast, originalType);
    }
}

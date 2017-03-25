package com.helospark.spark.builder.handlers.codegenerator.component.remover;

import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

public interface BuilderRemoverChainItem {

    void remove(ASTRewrite rewriter, TypeDeclaration mainType);

}

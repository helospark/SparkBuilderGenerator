package com.helospark.spark.builder.handlers.codegenerator.component.remover;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;

/**
 * Chain item to remove parts of the previously generated builder.
 * @author helospark
 */
public interface BuilderRemoverChainItem {

    void remove(ASTRewrite rewriter, AbstractTypeDeclaration mainType, CompilationUnitModificationDomain modificationDomain);

}

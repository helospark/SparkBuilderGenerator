package com.helospark.spark.builder.handlers.codegenerator.component;

import java.util.List;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import com.helospark.spark.builder.handlers.codegenerator.component.remover.BuilderRemoverChainItem;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;

/**
 * Tries to remove the (earlier generated) builder from the given compilation unit.
 * @author helospark
 */
public class BuilderAstRemover {
    private List<BuilderRemoverChainItem> builderRemovers;

    public BuilderAstRemover(List<BuilderRemoverChainItem> builderRemovers) {
        this.builderRemovers = builderRemovers;
    }

    public void removeBuilder(ASTRewrite rewriter, CompilationUnit compilationUnit, CompilationUnitModificationDomain modificationDomain) {
        AbstractTypeDeclaration type = modificationDomain.getOriginalType();
        builderRemovers.stream().forEach(remover -> remover.remove(rewriter, type, modificationDomain));
    }
}

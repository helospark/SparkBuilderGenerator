package com.helospark.spark.builder.handlers.codegenerator.component;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import com.helospark.spark.builder.handlers.codegenerator.component.remover.BuilderRemoverChainItem;

/**
 * Tries to remove the (earlier generated) builder from the given compilation unit.
 * @author helospark
 */
public class BuilderAstRemover {
    private List<BuilderRemoverChainItem> builderRemovers;

    public BuilderAstRemover(List<BuilderRemoverChainItem> builderRemovers) {
        this.builderRemovers = builderRemovers;
    }

    public void removeBuilder(ASTRewrite rewriter, CompilationUnit compilationUnit) {
        List<TypeDeclaration> types = compilationUnit.types();
        if (types.size() == 1) {
            builderRemovers.stream()
                    .forEach(remover -> remover.remove(rewriter, types.get(0)));
        }
    }
}

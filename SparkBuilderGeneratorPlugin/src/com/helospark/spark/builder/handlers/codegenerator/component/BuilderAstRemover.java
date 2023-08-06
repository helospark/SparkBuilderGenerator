package com.helospark.spark.builder.handlers.codegenerator.component;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
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
        List<AbstractTypeDeclaration> types = ((List<AbstractTypeDeclaration>) compilationUnit.types())
                .stream()
                .filter(abstractTypeDeclaration -> abstractTypeDeclaration instanceof TypeDeclaration)
                .map(abstractTypeDeclaration -> (TypeDeclaration) abstractTypeDeclaration)
                .collect(Collectors.toList());
        if (types.size() == 1) {
            builderRemovers.stream()
                    .forEach(remover -> remover.remove(rewriter, types.get(0), modificationDomain));
        }
    }
}

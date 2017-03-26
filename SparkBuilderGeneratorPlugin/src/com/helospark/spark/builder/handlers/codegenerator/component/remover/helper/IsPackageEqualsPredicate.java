package com.helospark.spark.builder.handlers.codegenerator.component.remover.helper;

import java.util.Objects;
import java.util.function.BiPredicate;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class IsPackageEqualsPredicate implements BiPredicate<ASTNode, ASTNode> {

    @Override
    public boolean test(ASTNode first, ASTNode second) {
        return Objects.equals(((CompilationUnit) first.getRoot()).getPackage().getName().toString(),
                ((CompilationUnit) second.getRoot()).getPackage().getName().toString());
    }
}

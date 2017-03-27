package com.helospark.spark.builder.handlers.codegenerator.component.remover.helper;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class IsPackageEqualsPredicate implements BiPredicate<ASTNode, ASTNode> {

    @Override
    public boolean test(ASTNode first, ASTNode second) {
        Optional<String> firstPackageName = getPackageName(first);
        Optional<String> secondPackageName = getPackageName(second);

        return Objects.equals(firstPackageName, secondPackageName);
    }

    private Optional<String> getPackageName(ASTNode first) {
        return Optional.ofNullable(first.getRoot())
                .filter(root -> root instanceof CompilationUnit)
                .map(root -> (CompilationUnit) root)
                .map(compilationUnit -> compilationUnit.getPackage())
                .map(packageDeclaration -> packageDeclaration.getName())
                .map(packageName -> packageName.toString());
    }
}

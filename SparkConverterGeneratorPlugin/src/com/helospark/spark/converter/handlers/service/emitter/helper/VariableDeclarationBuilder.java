package com.helospark.spark.converter.handlers.service.emitter.helper;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class VariableDeclarationBuilder {

    public VariableDeclarationStatement buildVariableDeclaration(AST ast, String name, Expression initialValue) {
        VariableDeclarationFragment singleVariableDeclaration = ast.newVariableDeclarationFragment();
        singleVariableDeclaration.setName(ast.newSimpleName(name));
        singleVariableDeclaration.setInitializer(initialValue);

        VariableDeclarationStatement vds = ast.newVariableDeclarationStatement(singleVariableDeclaration);
        return vds;
    }
}

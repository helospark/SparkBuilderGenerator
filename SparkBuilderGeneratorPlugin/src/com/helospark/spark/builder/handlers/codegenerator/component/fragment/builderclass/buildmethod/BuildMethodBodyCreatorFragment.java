package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.buildmethod;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;

/**
 * Fragment to add create the build() method' body.
 * Generated code is something like:
 * <pre>
 * {
 *   return new Clazz(this);
 * }
 * </pre>
 * @author helospark
 */
public class BuildMethodBodyCreatorFragment {

    public Block createBody(AST ast, AbstractTypeDeclaration originalType) {
        ClassInstanceCreation newClassInstanceCreation = ast.newClassInstanceCreation();
        newClassInstanceCreation.setType(ast.newSimpleType(ast.newName(originalType.getName().toString())));
        newClassInstanceCreation.arguments().add(ast.newThisExpression());

        ReturnStatement statement = ast.newReturnStatement();
        statement.setExpression(newClassInstanceCreation);

        Block block = ast.newBlock();
        block.statements().add(statement);
        return block;
    }
}

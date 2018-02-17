package com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.copy;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Creates a block which creates a builder using the copy constructor, generated code is something like:
 * <pre>
 * {
 *     return new Builder(parameterName);
 * }
 * </pre>
 * @author helospark
 */
public class BlockWithNewCopyBuilderCreationFragment {

    public Block createReturnBlock(AST ast, TypeDeclaration builderType, String parameterName) {
        Block builderMethodBlock = ast.newBlock();
        ReturnStatement returnStatement = ast.newReturnStatement();
        ClassInstanceCreation newClassInstanceCreation = ast.newClassInstanceCreation();
        newClassInstanceCreation.setType(ast.newSimpleType(ast.newName(builderType.getName().toString())));
        newClassInstanceCreation.arguments().add(createArgument(ast, parameterName));
        returnStatement.setExpression(newClassInstanceCreation);
        builderMethodBlock.statements().add(returnStatement);
        return builderMethodBlock;
    }

    private Expression createArgument(AST ast, String parameterName) {
        return ast.newSimpleName(parameterName);
    }

}

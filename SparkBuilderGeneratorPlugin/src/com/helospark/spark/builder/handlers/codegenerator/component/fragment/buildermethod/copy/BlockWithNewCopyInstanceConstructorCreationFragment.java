package com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.copy;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;

/**
 * Creates a block which creates a builder using the copy constructor, generated code is something like:
 * <pre>
 * {
 *     return new Builder(parameterName);
 * }
 * </pre>
 * @author helospark
 */
public class BlockWithNewCopyInstanceConstructorCreationFragment {

    public Block createReturnBlock(AST ast, AbstractTypeDeclaration builderType, String parameterName) {
        ClassInstanceCreation builderIntantiation = createClassInstantiation(ast, builderType, parameterName);
        ReturnStatement returnStatement = createReturnStatementWithInstantiation(ast, builderIntantiation);
        return createBlockWithReturnStatement(ast, returnStatement);
    }

    private ClassInstanceCreation createClassInstantiation(AST ast, AbstractTypeDeclaration builderType, String parameterName) {
        ClassInstanceCreation newClassInstanceCreation = ast.newClassInstanceCreation();
        newClassInstanceCreation.setType(ast.newSimpleType(ast.newName(builderType.getName().toString())));
        newClassInstanceCreation.arguments().add(createArgument(ast, parameterName));
        return newClassInstanceCreation;
    }

    private Expression createArgument(AST ast, String parameterName) {
        return ast.newSimpleName(parameterName);
    }

    private ReturnStatement createReturnStatementWithInstantiation(AST ast, ClassInstanceCreation builderIntantiation) {
        ReturnStatement returnStatement = ast.newReturnStatement();
        returnStatement.setExpression(builderIntantiation);
        return returnStatement;
    }

    private Block createBlockWithReturnStatement(AST ast, ReturnStatement returnStatement) {
        Block builderMethodBlock = ast.newBlock();
        builderMethodBlock.statements().add(returnStatement);
        return builderMethodBlock;
    }

}

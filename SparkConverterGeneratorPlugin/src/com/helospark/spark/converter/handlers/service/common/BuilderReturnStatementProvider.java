package com.helospark.spark.converter.handlers.service.common;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ReturnStatement;

import com.helospark.spark.converter.handlers.service.common.domain.ConvertableDomain;

public class BuilderReturnStatementProvider {

    public ReturnStatement initializeBuilderReturn(ConvertableDomain convertableDomain, AST ast, Expression lastDeclaration) {
        ReturnStatement returnStatement = ast.newReturnStatement();
        if (convertableDomain.isUseBuilder()) {
            MethodInvocation builderMethodInvocation = ast.newMethodInvocation();
            builderMethodInvocation.setExpression(lastDeclaration);
            builderMethodInvocation.setName(ast.newSimpleName("build"));
            returnStatement.setExpression(builderMethodInvocation);
        } else {
            returnStatement.setExpression(ast.newName("destination"));
        }
        return returnStatement;
    }
}

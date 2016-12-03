package com.helospark.spark.converter.handlers.service.common;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertableDomain;

public class BuilderInitializationValueProvider {

    public Expression addLastDeclarationInitialization(CompilationUnitModificationDomain compilationUnitModificationDomain, ConvertableDomain convertableDomain,
            String destinationTypeName, Block body) {
        AST ast = compilationUnitModificationDomain.getAst();
        Expression lastDeclaration = null;
        if (convertableDomain.isUseBuilder()) {
            MethodInvocation methodInvocation = ast.newMethodInvocation();
            methodInvocation.setName(ast.newSimpleName("builder"));
            methodInvocation.setExpression(ast.newName(destinationTypeName));

            lastDeclaration = methodInvocation;
        } else {
            ClassInstanceCreation newClassInstanceCreation = ast.newClassInstanceCreation();
            SimpleType destinationType = ast.newSimpleType(ast.newName(destinationTypeName));
            newClassInstanceCreation.setType((SimpleType) ASTNode.copySubtree(ast, destinationType));

            VariableDeclarationFragment singleVariableDeclaration = ast.newVariableDeclarationFragment();
            singleVariableDeclaration.setName(ast.newSimpleName("destination"));
            singleVariableDeclaration.setInitializer(newClassInstanceCreation);

            VariableDeclarationStatement vds = ast.newVariableDeclarationStatement(singleVariableDeclaration);
            vds.setType((SimpleType) ASTNode.copySubtree(ast, destinationType));
            body.statements().add(vds);
        }
        return lastDeclaration;
    }
}

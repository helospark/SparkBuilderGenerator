package com.helospark.spark.converter.handlers.service.codegenerator.impl;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;

import com.helospark.spark.converter.handlers.service.codegenerator.ConverterTypeCodeGenerator;
import com.helospark.spark.converter.handlers.service.domain.CompilationUnitModificationDomain;
import com.helospark.spark.converter.handlers.service.domain.ConvertType;
import com.helospark.spark.converter.handlers.service.domain.ConvertableDomainParameter;

public class ReferenceCopyCodeGenerator implements ConverterTypeCodeGenerator {

    @Override
    public Expression generate(CompilationUnitModificationDomain compilationUnitModificationDomain, Block body, Expression expression, Expression sourceObject,
            ConvertableDomainParameter parameter) {
        AST ast = compilationUnitModificationDomain.getAst();
        MethodInvocation newMethodInvocation = ast.newMethodInvocation();
        newMethodInvocation.setExpression(expression);
        newMethodInvocation.setName(ast.newSimpleName(parameter.getDestinationMethodName()));

        MethodInvocation getMethodInvocation = ast.newMethodInvocation();
        getMethodInvocation.setExpression(sourceObject);
        getMethodInvocation.setName(ast.newSimpleName(parameter.getSourceMethodName()));

        newMethodInvocation.arguments().add(getMethodInvocation);

        return newMethodInvocation;
    }

    @Override
    public boolean canHandle(ConvertType type) {
        return type == ConvertType.REFERENCE_COPY;
    }

}

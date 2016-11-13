package com.helospark.spark.converter.handlers.service.emitter.parametercodegenerator.impl;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;

import com.helospark.spark.converter.handlers.domain.ConverterMethodCodeGenerationRequest;
import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertType;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertableDomainParameter;
import com.helospark.spark.converter.handlers.service.emitter.parametercodegenerator.ParameterConvertCodeGenerator;

public class ReferenceCopyCodeGenerator implements ParameterConvertCodeGenerator {

    @Override
    public Expression generate(CompilationUnitModificationDomain compilationUnitModificationDomain, Block body, Expression expression, Expression sourceObject,
            ConvertableDomainParameter parameter, ConverterTypeCodeGenerationRequest generationRequest, ConverterMethodCodeGenerationRequest method) {
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

package com.helospark.spark.converter.handlers.service.emitter.parametercodegenerator.impl;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;

import com.helospark.spark.converter.handlers.domain.ConverterMethodCodeGenerationRequest;
import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertType;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertableDomainParameter;
import com.helospark.spark.converter.handlers.service.emitter.parametercodegenerator.ParameterConvertCodeGenerator;

public class NoSourceCodeGenerator implements ParameterConvertCodeGenerator {

    @Override
    public Expression generate(CompilationUnitModificationDomain compilationUnitModificationDomain, Block body, Expression expression, Expression sourceObject,
            ConvertableDomainParameter parameter, ConverterTypeCodeGenerationRequest generationRequest, ConverterMethodCodeGenerationRequest method) {
        AST ast = compilationUnitModificationDomain.getAst();
        return ast.newNullLiteral();
    }

    @Override
    public boolean canHandle(ConvertType type) {
        return ConvertType.NO_DESTINATION.equals(type);
    }

}

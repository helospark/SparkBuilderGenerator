package com.helospark.spark.converter.handlers.service.emitter.parametercodegenerator;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;

import com.helospark.spark.converter.handlers.domain.ConverterMethodCodeGenerationRequest;
import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertType;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertableDomainParameter;

public interface ParameterConvertCodeGenerator {

    public Expression generate(CompilationUnitModificationDomain compilationUnitModificationDomain, Block body, Expression expression, Expression sourceObject,
            ConvertableDomainParameter parameter, ConverterTypeCodeGenerationRequest generationRequest, ConverterMethodCodeGenerationRequest method);

    public boolean canHandle(ConvertType type);

}

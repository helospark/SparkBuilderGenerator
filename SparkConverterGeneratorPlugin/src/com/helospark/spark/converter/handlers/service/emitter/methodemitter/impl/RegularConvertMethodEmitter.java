package com.helospark.spark.converter.handlers.service.emitter.methodemitter.impl;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import com.helospark.spark.converter.handlers.domain.ConverterMethodCodeGenerationRequest;
import com.helospark.spark.converter.handlers.domain.ConverterMethodType;
import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.common.ConvertableParametersGenerator;
import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertableDomain;
import com.helospark.spark.converter.handlers.service.common.domain.SourceDestinationType;
import com.helospark.spark.converter.handlers.service.emitter.methodemitter.MethodEmitter;
import com.helospark.spark.converter.handlers.service.emitter.methodemitter.impl.helper.ConverterConvertMethodGenerator;

public class RegularConvertMethodEmitter implements MethodEmitter {
    private ConvertableParametersGenerator convertableParametersGenerator;
    private ConverterConvertMethodGenerator converterConvertMethodGenerator;

    public RegularConvertMethodEmitter(ConvertableParametersGenerator convertableParametersGenerator, ConverterConvertMethodGenerator converterConvertMethodGenerator) {
        this.convertableParametersGenerator = convertableParametersGenerator;
        this.converterConvertMethodGenerator = converterConvertMethodGenerator;
    }

    @Override
    public MethodDeclaration generateMethod(CompilationUnitModificationDomain compilationUnitModificationDomain, ConverterTypeCodeGenerationRequest generationRequest,
            ConverterMethodCodeGenerationRequest method) {
        ConvertableDomain parameters = convertableParametersGenerator.extract(new SourceDestinationType(method.getSourceType(), method.getDestinationType()));
        return converterConvertMethodGenerator.generate(compilationUnitModificationDomain, parameters, generationRequest, method);
    }

    @Override
    public boolean canHandle(ConverterMethodType converterMethodType) {
        return converterMethodType.equals(ConverterMethodType.REGULAR_CONVERTER);
    }

}

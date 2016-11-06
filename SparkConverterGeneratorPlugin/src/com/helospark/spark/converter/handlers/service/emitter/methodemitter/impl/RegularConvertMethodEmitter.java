package com.helospark.spark.converter.handlers.service.emitter.methodemitter.impl;

import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.converter.handlers.domain.ConverterMethodType;
import com.helospark.spark.converter.handlers.service.common.ConvertableParametersGenerator;
import com.helospark.spark.converter.handlers.service.domain.CompilationUnitModificationDomain;
import com.helospark.spark.converter.handlers.service.domain.ConvertableDomainParameter;
import com.helospark.spark.converter.handlers.service.emitter.methodemitter.MethodEmitter;
import com.helospark.spark.converter.handlers.service.emitter.methodemitter.impl.helper.ConverterConvertMethodGenerator;

public class RegularConvertMethodEmitter implements MethodEmitter {
    private ConvertableParametersGenerator convertableParametersGenerator;
    private ConverterConvertMethodGenerator converterConvertMethodGenerator;

    @Override
    public void addMethod( newType, ConverterTypeCodeGenerationRequest generationRequest, ConverterMethodCodeGenerationRequest method) {
        ConvertableDomain parameters = convertableParametersGenerator.extract(new SourceDestinationType(method.getSourceType(), method.getDestinationType()));
        parameters.getConvertableDomainParameters()
                .stream()
                .forEach(parameter -> addParameter(newType, parameter));
    }

    private void addParameter(TypeDeclaration newType, CompilationUnitModificationDomain compilationUnitModificationDomain, ConvertableDomainParameter parameter) {
        converterConvertMethodGenerator.generate(compilationUnitModificationDomain, parameter);
        return null;
    }

    @Override
    public boolean canHandle(ConverterMethodType converterMethodType) {
        return converterMethodType.equals(ConverterMethodType.REGULAR_CONVERTER);
    }

}

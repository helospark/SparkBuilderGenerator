package com.helospark.spark.converter.handlers.service.emitter.unittestemitter.impl;

import com.helospark.spark.converter.handlers.domain.ConverterMethodCodeGenerationRequest;
import com.helospark.spark.converter.handlers.domain.ConverterMethodType;
import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;
import com.helospark.spark.converter.handlers.service.emitter.unittestemitter.UnitTestMethodEmitter;

public class ConverterUnitTestMethodEmitter implements UnitTestMethodEmitter {

    @Override
    public void generateMethod(CompilationUnitModificationDomain compilationUnitModificationDomain, ConverterTypeCodeGenerationRequest generationRequest,
            ConverterMethodCodeGenerationRequest method) {

    }

    @Override
    public boolean canHandle(ConverterMethodType converterMethodType) {
        return ConverterMethodType.REGULAR_CONVERTER.equals(converterMethodType);
    }

}

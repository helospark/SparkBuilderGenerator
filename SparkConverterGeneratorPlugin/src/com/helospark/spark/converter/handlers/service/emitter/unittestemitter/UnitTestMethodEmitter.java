package com.helospark.spark.converter.handlers.service.emitter.unittestemitter;

import com.helospark.spark.converter.handlers.domain.ConverterMethodCodeGenerationRequest;
import com.helospark.spark.converter.handlers.domain.ConverterMethodType;
import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;

public interface UnitTestMethodEmitter {
    public void generateMethod(CompilationUnitModificationDomain compilationUnitModificationDomain, ConverterTypeCodeGenerationRequest generationRequest,
            ConverterMethodCodeGenerationRequest method);

    public boolean canHandle(ConverterMethodType converterMethodType);
}

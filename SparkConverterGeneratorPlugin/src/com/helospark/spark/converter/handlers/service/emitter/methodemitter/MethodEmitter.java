package com.helospark.spark.converter.handlers.service.emitter.methodemitter;

import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.converter.handlers.domain.ConverterMethodCodeGenerationRequest;
import com.helospark.spark.converter.handlers.domain.ConverterMethodType;
import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;

public interface MethodEmitter {
    public void addMethod(TypeDeclaration newType, ConverterTypeCodeGenerationRequest generationRequest, ConverterMethodCodeGenerationRequest method);

    public boolean canHandle(ConverterMethodType converterMethodType);
}

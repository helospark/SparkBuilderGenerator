package com.helospark.spark.converter.handlers.service.emitter.methodemitter;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import com.helospark.spark.converter.handlers.domain.ConverterMethodCodeGenerationRequest;
import com.helospark.spark.converter.handlers.domain.ConverterMethodType;
import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.domain.CompilationUnitModificationDomain;

public interface MethodEmitter {
    public MethodDeclaration generateMethod(CompilationUnitModificationDomain compilationUnitModificationDomain, ConverterTypeCodeGenerationRequest generationRequest,
            ConverterMethodCodeGenerationRequest method);

    public boolean canHandle(ConverterMethodType converterMethodType);
}

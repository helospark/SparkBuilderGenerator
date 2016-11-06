package com.helospark.spark.converter.handlers.service.emitter;

import java.util.List;

import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.converter.handlers.domain.ConverterMethodCodeGenerationRequest;
import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.emitter.methodemitter.MethodEmitter;

public class MethodsEmitter {
    private List<MethodEmitter> methodEmitters;

    public void addMethod(TypeDeclaration newType, ConverterTypeCodeGenerationRequest generationRequest, ConverterMethodCodeGenerationRequest method) {
        methodEmitters.stream()
                .filter(emitter -> emitter.canHandle(method.getConverterMethodType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No emitter for type " + method.getConverterMethodType()))
                .addMethod(newType, generationRequest, method);
    }

}

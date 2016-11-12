package com.helospark.spark.converter.handlers.service.emitter;

import java.util.List;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.converter.handlers.domain.ConverterMethodCodeGenerationRequest;
import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.domain.CompilationUnitModificationDomain;
import com.helospark.spark.converter.handlers.service.emitter.methodemitter.MethodEmitter;

public class MethodsEmitter {
    private List<MethodEmitter> methodEmitters;

    public MethodsEmitter(List<MethodEmitter> methodEmitters) {
        this.methodEmitters = methodEmitters;
    }

    public void addMethod(TypeDeclaration type, CompilationUnitModificationDomain compilationUnitModificationDomain, ConverterTypeCodeGenerationRequest generationRequest,
            ConverterMethodCodeGenerationRequest method) {
        MethodDeclaration generatedMethod = methodEmitters.stream()
                .filter(emitter -> emitter.canHandle(method.getConverterMethodType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No emitter for type " + method.getConverterMethodType()))
                .generateMethod(compilationUnitModificationDomain, generationRequest, method);
        type.bodyDeclarations().add(generatedMethod);
    }

}

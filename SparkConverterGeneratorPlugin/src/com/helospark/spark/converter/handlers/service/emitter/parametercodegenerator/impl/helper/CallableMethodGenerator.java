package com.helospark.spark.converter.handlers.service.emitter.parametercodegenerator.impl.helper;

import java.util.Optional;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodInvocation;

import com.helospark.spark.converter.handlers.domain.ConverterMethodCodeGenerationRequest;
import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.collector.collectors.helper.ConverterMethodLocator;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertableDomainParameter;
import com.helospark.spark.converter.handlers.service.common.domain.SourceDestinationType;

public class CallableMethodGenerator {
    private ConverterMethodLocator converterMethodLocator;

    public CallableMethodGenerator(ConverterMethodLocator converterMethodLocator) {
        this.converterMethodLocator = converterMethodLocator;
    }

    public MethodInvocation generateMethodCall(AST ast, ConverterTypeCodeGenerationRequest generationRequest, ConverterMethodCodeGenerationRequest method,
            SourceDestinationType sourceDestionation) {
        Optional<ConverterMethodCodeGenerationRequest> convertMethodApplicable = converterMethodLocator.getConverterMethod(generationRequest.getDependencies(),
                sourceDestionation);

        if (convertMethodApplicable.isPresent()) {
            MethodInvocation methodInvocation = ast.newMethodInvocation();
            methodInvocation.setName(ast.newSimpleName(convertMethodApplicable.get().getName()));
            methodInvocation.setExpression(ast.newSimpleName(convertMethodApplicable.get().getContainingType().getFieldName()));
            return methodInvocation;
        } else {
            ConverterMethodCodeGenerationRequest convertMethod = converterMethodLocator.findConvertMethod(generationRequest.getMethods(), sourceDestionation)
                    .orElseThrow(() -> new IllegalStateException("We cannot find a converter for type " + sourceDestionation));
            MethodInvocation methodInvocation = ast.newMethodInvocation();
            methodInvocation.setName(ast.newSimpleName(convertMethod.getName()));
            return methodInvocation;
        }
    }

    private String getDependencyName(String className) {
        return className;
    }

}

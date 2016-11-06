package com.helospark.spark.converter.handlers.service.collector.collectors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.helospark.spark.converter.handlers.domain.ConverterInputParameters;
import com.helospark.spark.converter.handlers.domain.ConverterMethodCodeGenerationRequest;
import com.helospark.spark.converter.handlers.domain.ConverterMethodType;
import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.collector.collectors.helper.ConverterMethodLocator;
import com.helospark.spark.converter.handlers.service.domain.ConvertType;
import com.helospark.spark.converter.handlers.service.domain.SourceDestinationType;

public class ConvertMethodCollector implements MethodCollectorChain {
    private ConverterMethodLocator converterMethodLocator;

    public ConvertMethodCollector(ConverterMethodLocator converterMethodLocator) {
        this.converterMethodLocator = converterMethodLocator;
    }

    @Override
    public void handle(ConverterInputParameters converterInputParameters, SourceDestinationType sourceDestination, List<ConverterTypeCodeGenerationRequest> result) {
        Optional<ConverterTypeCodeGenerationRequest> converterMethod = converterMethodLocator.getConvertMethodApplicable(result, sourceDestination);
        if (!converterMethod.isPresent()) {
            createConverterMethod(converterInputParameters, sourceDestination, result);
        }
    }

    private void createConverterMethod(ConverterInputParameters converterInputParameters, SourceDestinationType sourceDestination,
            List<ConverterTypeCodeGenerationRequest> converters) {
        ConverterTypeCodeGenerationRequest converterType = getOrCreateConverterType(converterInputParameters, sourceDestination, converters);
        ConverterMethodCodeGenerationRequest method = createConverterMethod(sourceDestination, converterType);
        converterType.addMethod(method);
    }

    private ConverterMethodCodeGenerationRequest createConverterMethod(SourceDestinationType sourceDestination, ConverterTypeCodeGenerationRequest converterType) {
        return ConverterMethodCodeGenerationRequest.builder()
                .withContainingType(converterType)
                .withConverterMethodType(ConverterMethodType.REGULAR_CONVERTER)
                .withDestinationType(sourceDestination.getDestinationType())
                .withSourceType(sourceDestination.getSourceType())
                .withName("convert")
                .withParameterName("param")
                .build();
    }

    private ConverterTypeCodeGenerationRequest getOrCreateConverterType(ConverterInputParameters converterInputParameters, SourceDestinationType sourceDestination,
            List<ConverterTypeCodeGenerationRequest> converters) {
        String className = sourceDestination.getDestinationType().getType().getElementName() + "Converter";
        Optional<ConverterTypeCodeGenerationRequest> converterType = converterMethodLocator.findByName(converters, className);
        if (converterType.isPresent()) {
            return converterType.get();
        } else {
            return ConverterTypeCodeGenerationRequest.builder()
                    .withClassName(className)
                    .withDependencies(new ArrayList<>())
                    .withMethods(new ArrayList<>())
                    .withPackageName(converterInputParameters.getDestinationPackageFragment())
                    .build();
        }
    }

    @Override
    public boolean canHandle(ConvertType type) {
        return ConvertType.CONVERT.equals(type);
    }
}

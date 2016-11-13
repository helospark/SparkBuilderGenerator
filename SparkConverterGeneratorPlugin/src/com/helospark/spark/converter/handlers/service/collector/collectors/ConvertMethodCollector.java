package com.helospark.spark.converter.handlers.service.collector.collectors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.helospark.spark.converter.handlers.domain.ConverterInputParameters;
import com.helospark.spark.converter.handlers.domain.ConverterMethodCodeGenerationRequest;
import com.helospark.spark.converter.handlers.domain.ConverterMethodType;
import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.collector.collectors.helper.ConverterMethodLocator;
import com.helospark.spark.converter.handlers.service.common.ClassNameToVariableNameConverter;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertType;
import com.helospark.spark.converter.handlers.service.common.domain.SourceDestinationType;

public class ConvertMethodCollector implements MethodCollectorChain {
    private ConverterMethodLocator converterMethodLocator;
    private ClassNameToVariableNameConverter classNameToVariableNameConverter;

    public ConvertMethodCollector(ConverterMethodLocator converterMethodLocator, ClassNameToVariableNameConverter classNameToVariableNameConverter) {
        this.converterMethodLocator = converterMethodLocator;
        this.classNameToVariableNameConverter = classNameToVariableNameConverter;
    }

    @Override
    public ConverterTypeCodeGenerationRequest handle(ConverterInputParameters converterInputParameters, SourceDestinationType sourceDestination,
            List<ConverterTypeCodeGenerationRequest> result) {
        Optional<ConverterTypeCodeGenerationRequest> converterMethod = converterMethodLocator.getConverterClass(result, sourceDestination);
        if (!converterMethod.isPresent()) {
            ConverterTypeCodeGenerationRequest createdConverter = createConverterMethod(converterInputParameters, sourceDestination, result);
            result.add(createdConverter);
            return createdConverter;
        } else {
            return converterMethod.get();
        }
    }

    private ConverterTypeCodeGenerationRequest createConverterMethod(ConverterInputParameters converterInputParameters, SourceDestinationType sourceDestination,
            List<ConverterTypeCodeGenerationRequest> converters) {
        ConverterTypeCodeGenerationRequest converterType = getOrCreateConverterType(converterInputParameters, sourceDestination, converters);
        ConverterMethodCodeGenerationRequest method = createConverterMethod(sourceDestination, converterType);
        converterType.addMethod(method);
        return converterType;
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
                    .withFieldName(classNameToVariableNameConverter.convert(className))
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

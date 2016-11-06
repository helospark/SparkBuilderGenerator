package com.helospark.spark.converter.handlers.service.collector.collectors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.helospark.spark.converter.handlers.domain.ConverterInputParameters;
import com.helospark.spark.converter.handlers.domain.ConverterMethodCodeGenerationRequest;
import com.helospark.spark.converter.handlers.domain.ConverterMethodType;
import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.collector.MethodCollector;
import com.helospark.spark.converter.handlers.service.collector.collectors.helper.ConverterMethodLocator;
import com.helospark.spark.converter.handlers.service.domain.ConvertType;
import com.helospark.spark.converter.handlers.service.domain.SourceDestinationType;

public class OptionalMethodCollector implements MethodCollectorChain {
    private ConverterMethodLocator converterMethodLocator;
    private MethodCollector methodCollector;

    public OptionalMethodCollector(ConverterMethodLocator converterMethodLocator, MethodCollector methodCollector) {
        this.converterMethodLocator = converterMethodLocator;
        this.methodCollector = methodCollector;
    }

    @Override
    public void handle(ConverterInputParameters converterInputParameters, SourceDestinationType sourceDestination, List<ConverterTypeCodeGenerationRequest> converters) {
        Optional<ConverterTypeCodeGenerationRequest> converterMethod = converterMethodLocator.getConvertMethodApplicable(converters, sourceDestination);
        if (!converterMethod.isPresent()) {
            createConverterMethod(converterInputParameters, sourceDestination, converters);
        }
    }

    private void createConverterMethod(ConverterInputParameters converterInputParameters, SourceDestinationType sourceDestination,
            List<ConverterTypeCodeGenerationRequest> converters) {
        ConverterTypeCodeGenerationRequest converterType = getOrCreateConverterType(converterInputParameters, sourceDestination, converters);
        ConverterMethodCodeGenerationRequest method = createConverterMethod(sourceDestination, converterType);
        converterType.addMethod(method);

        ConverterTypeCodeGenerationRequest templateConvertType = addRecursiveConverterForTemplateParameter(converterInputParameters, sourceDestination, converters);
        converterType.addDependency(templateConvertType);
    }

    private ConverterTypeCodeGenerationRequest addRecursiveConverterForTemplateParameter(ConverterInputParameters converterInputParameters, SourceDestinationType sourceDestination,
            List<ConverterTypeCodeGenerationRequest> converters) {
        SourceDestinationType templateParameters = new SourceDestinationType(sourceDestination.getSourceType().getTemplates().get(0),
                sourceDestination.getDestinationType().getTemplates().get(0));
        methodCollector.recursivelyCollectConverters(converterInputParameters, templateParameters, converters);
        ConverterTypeCodeGenerationRequest templateConvertType = converterMethodLocator.getConvertMethodApplicable(converters, templateParameters)
                .orElseThrow(() -> new RuntimeException("We generated a new converter, but cannot be located subsequently"));
        return templateConvertType;
    }

    private ConverterMethodCodeGenerationRequest createConverterMethod(SourceDestinationType sourceDestination, ConverterTypeCodeGenerationRequest converterType) {
        return ConverterMethodCodeGenerationRequest.builder()
                .withContainingType(converterType)
                .withConverterMethodType(ConverterMethodType.OPTIONAL_CONVERTER)
                .withDestinationType(sourceDestination.getDestinationType())
                .withSourceType(sourceDestination.getSourceType())
                .withName("convert")
                .withParameterName("param")
                .build();
    }

    private ConverterTypeCodeGenerationRequest getOrCreateConverterType(ConverterInputParameters converterInputParameters, SourceDestinationType sourceDestination,
            List<ConverterTypeCodeGenerationRequest> converters) {
        String className = sourceDestination.getDestinationType().getTemplates().get(0).getType().getElementName() + "Converter";
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
        return ConvertType.OPTIONAL_CONVERT.equals(type);
    }
}

package com.helospark.spark.converter.handlers.service.collector.collectors.helper;

import java.util.List;
import java.util.Optional;

import com.helospark.spark.converter.handlers.domain.ConverterMethodCodeGenerationRequest;
import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.domain.SourceDestinationType;

public class ConverterMethodLocator {

    public Optional<ConverterTypeCodeGenerationRequest> getConverterClass(List<ConverterTypeCodeGenerationRequest> converters,
            SourceDestinationType sourceDestination) {
        return converters.stream()
                .filter(converter -> containsApplicableConvertMethod(converter, sourceDestination))
                .findFirst();
    }

    public Optional<ConverterMethodCodeGenerationRequest> getConverterMethod(List<ConverterTypeCodeGenerationRequest> converters,
            SourceDestinationType sourceDestination) {
        return converters.stream()
                .map(converter -> findConvertMethod(converter.getMethods(), sourceDestination))
                .filter(convertMethod -> convertMethod.isPresent())
                .map(convertMethod -> convertMethod.get())
                .findFirst();
    }

    public Optional<ConverterMethodCodeGenerationRequest> findConvertMethod(List<ConverterMethodCodeGenerationRequest> methods,
            SourceDestinationType sourceDestination) {
        return methods.stream()
                .filter(method -> isMethodApplicable(method, sourceDestination))
                .findFirst();
    }

    public Optional<ConverterTypeCodeGenerationRequest> findByName(List<ConverterTypeCodeGenerationRequest> converters, String name) {
        return converters.stream()
                .filter(converter -> converter.getClassName().equals(name))
                .findFirst();
    }

    private boolean containsApplicableConvertMethod(ConverterTypeCodeGenerationRequest converter, SourceDestinationType sourceDestination) {
        return converter.getMethods()
                .stream()
                .filter(method -> isMethodApplicable(method, sourceDestination))
                .findFirst()
                .map(data -> true)
                .orElse(false);
    }

    private boolean isMethodApplicable(ConverterMethodCodeGenerationRequest method, SourceDestinationType sourceDestination) {
        return method.getSourceType().equals(sourceDestination.getSourceType()) &&
                method.getDestinationType().equals(sourceDestination.getDestinationType());
    }
}

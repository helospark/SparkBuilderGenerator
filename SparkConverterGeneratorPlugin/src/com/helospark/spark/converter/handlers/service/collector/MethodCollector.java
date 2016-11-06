package com.helospark.spark.converter.handlers.service.collector;

import static java.util.Optional.of;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;

import com.helospark.spark.converter.handlers.domain.ConverterInputParameters;
import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.collector.collectors.MethodCollectorChain;
import com.helospark.spark.converter.handlers.service.common.ConvertableParametersGenerator;
import com.helospark.spark.converter.handlers.service.domain.ConvertType;
import com.helospark.spark.converter.handlers.service.domain.ConvertableDomain;
import com.helospark.spark.converter.handlers.service.domain.ConvertableDomainParameter;
import com.helospark.spark.converter.handlers.service.domain.SourceDestinationType;

/**
 * Generates the converter.
 * 
 * @author helospark
 */
public class MethodCollector {
    private ConvertableParametersGenerator convertableParametersGenerator;
    private List<MethodCollectorChain> methodCollectorChain;
    private ConvertTypeFinder convertTypeFinder;

    public MethodCollector(ConvertableParametersGenerator convertableParametersGenerator, List<MethodCollectorChain> methodCollectorChain) {
        this.convertableParametersGenerator = convertableParametersGenerator;
        this.methodCollectorChain = methodCollectorChain;
    }

    public List<ConverterTypeCodeGenerationRequest> collectConverters(ConverterInputParameters converterInputParameters) throws JavaModelException, BadLocationException {
        List<ConverterTypeCodeGenerationRequest> result = new ArrayList<>();
        SourceDestinationType sourceDestionation = new SourceDestinationType(converterInputParameters.getSourceType(), converterInputParameters.getDestinationType());
        recursivelyCollectConverters(converterInputParameters, sourceDestionation, result);
        return result;
    }

    public void recursivelyCollectConverters(ConverterInputParameters converterInputParameters, SourceDestinationType sourceDestionation,
            List<ConverterTypeCodeGenerationRequest> createdConverters) {
        ConvertType convertType = convertTypeFinder.findConvertType(of(sourceDestionation.getSourceType()), of(sourceDestionation.getDestinationType()));
        Optional<MethodCollectorChain> methodCollectorResult = findMethodCollectorImplementation(convertType);
        methodCollectorResult.ifPresent(methodCollector -> methodCollector.handle(converterInputParameters, sourceDestionation, createdConverters));

        if (converterInputParameters.getRecursiveGeneration()) {
            ConvertableDomain convertableDomainParameters = convertableParametersGenerator.extract(sourceDestionation);
            for (ConvertableDomainParameter domainParameter : convertableDomainParameters.getConvertableDomainParameters()) {
                SourceDestinationType parameterSourceType = new SourceDestinationType(domainParameter.getSourceType(), domainParameter.getDestinationType());
                recursivelyCollectConverters(converterInputParameters, parameterSourceType, createdConverters);
            }
        }
    }

    private Optional<MethodCollectorChain> findMethodCollectorImplementation(ConvertType convertType) {
        return methodCollectorChain.stream()
                .filter(methodCollector -> methodCollector.canHandle(convertType))
                .findFirst();
    }

}

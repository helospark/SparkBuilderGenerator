package com.helospark.spark.converter.handlers.service.collector;

import static java.util.Optional.ofNullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;

import com.helospark.spark.converter.handlers.domain.ConverterInputParameters;
import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.collector.collectors.MethodCollectorChain;
import com.helospark.spark.converter.handlers.service.collector.converttype.ConvertTypeFinder;
import com.helospark.spark.converter.handlers.service.common.ConvertableParametersGenerator;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertType;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertableDomain;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertableDomainParameter;
import com.helospark.spark.converter.handlers.service.common.domain.SourceDestinationType;

/**
 * Generates the converter.
 * 
 * @author helospark
 */
public class MethodCollector {
    private ConvertableParametersGenerator convertableParametersGenerator;
    private List<MethodCollectorChain> methodCollectorChain;
    private ConvertTypeFinder convertTypeFinder;

    public MethodCollector(ConvertableParametersGenerator convertableParametersGenerator, ConvertTypeFinder convertTypeFinder) {
        this.convertableParametersGenerator = convertableParametersGenerator;
        this.convertTypeFinder = convertTypeFinder;
    }

    public List<ConverterTypeCodeGenerationRequest> collectConverters(ConverterInputParameters converterInputParameters) throws JavaModelException, BadLocationException {
        List<ConverterTypeCodeGenerationRequest> result = new ArrayList<>();
        SourceDestinationType sourceDestionation = new SourceDestinationType(converterInputParameters.getSourceType(), converterInputParameters.getDestinationType());
        recursivelyCollectConverters(converterInputParameters, sourceDestionation, result);
        return result;
    }

    public Optional<ConverterTypeCodeGenerationRequest> recursivelyCollectConverters(ConverterInputParameters converterInputParameters, SourceDestinationType sourceDestionation,
            List<ConverterTypeCodeGenerationRequest> createdConverters) {
        ConvertType convertType = convertTypeFinder.findConvertType(ofNullable(sourceDestionation.getSourceType()), ofNullable(sourceDestionation.getDestinationType()));
        Optional<MethodCollectorChain> methodCollectorResult = findMethodCollectorImplementation(convertType);
        if (methodCollectorResult.isPresent()) {
            ConverterTypeCodeGenerationRequest generatedConverter = methodCollectorResult.get().handle(converterInputParameters, sourceDestionation, createdConverters);

            if (converterInputParameters.getRecursiveGeneration()) {
                ConvertableDomain convertableDomainParameters = convertableParametersGenerator.extract(sourceDestionation);
                for (ConvertableDomainParameter domainParameter : convertableDomainParameters.getConvertableDomainParameters()) {
                    SourceDestinationType parameterSourceType = new SourceDestinationType(domainParameter.getSourceType(), domainParameter.getDestinationType());
                    Optional<ConverterTypeCodeGenerationRequest> recursiveConverter = recursivelyCollectConverters(converterInputParameters, parameterSourceType,
                            createdConverters);
                    recursiveConverter.ifPresent(dependency -> generatedConverter.safeAddDependency(dependency));
                }
            }
            return Optional.of(generatedConverter);
        }
        return Optional.empty();
    }

    private Optional<MethodCollectorChain> findMethodCollectorImplementation(ConvertType convertType) {
        return methodCollectorChain.stream()
                .filter(methodCollector -> methodCollector.canHandle(convertType))
                .findFirst();
    }

    /**
     * Method collectors are set here instead of constructor because of the
     * cyclic dependency.
     * 
     * @param methodCollectorChain
     *            to set
     */
    // TODO! Cyclic dependency should be avoided
    public void setMethodCollectorChain(List<MethodCollectorChain> methodCollectorChain) {
        this.methodCollectorChain = methodCollectorChain;
    }

}

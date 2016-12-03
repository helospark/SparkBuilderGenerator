package com.helospark.spark.converter.handlers.service.emitter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.converter.handlers.domain.ConverterMethodCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.common.ConvertableParametersGenerator;
import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertableDomain;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertableDomainParameter;
import com.helospark.spark.converter.handlers.service.common.domain.SourceDestinationType;
import com.helospark.spark.converter.handlers.service.emitter.unittestemitter.impl.mockvalueprovider.MockValueProvider;
import com.helospark.spark.converter.handlers.service.emitter.unittestemitter.impl.mockvalueprovider.domain.MockValuePair;

public class MockValueListProvider {
    private List<MockValueProvider> mockValueProviders;
    private ConvertableParametersGenerator convertableParametersGenerator;

    public MockValueListProvider(List<MockValueProvider> mockValueProviders, ConvertableParametersGenerator convertableParametersGenerator) {
        this.mockValueProviders = mockValueProviders;
        this.convertableParametersGenerator = convertableParametersGenerator;
    }

    public List<MockValuePair> addMockValuesAndGet(CompilationUnitModificationDomain compilationUnit, TypeDeclaration unitTest,
            ConverterMethodCodeGenerationRequest generationRequest) {
        List<MockValuePair> mockValues = new ArrayList<>();
        ConvertableDomain parameters = convertableParametersGenerator.extract(new SourceDestinationType(generationRequest.getSourceType(), generationRequest.getDestinationType()));
        for (ConvertableDomainParameter parameter : parameters.getConvertableDomainParameters()) {
            mockValues.add(mockValueProviders.stream()
                    .filter(p -> p.canHandle(parameter.getSourceType()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Cannot find mock value provider"))
                    .provideMockValue(unitTest, compilationUnit, parameter));
        }
        return mockValues;
    }
}

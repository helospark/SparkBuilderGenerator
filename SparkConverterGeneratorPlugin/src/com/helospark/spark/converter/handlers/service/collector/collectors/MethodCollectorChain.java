package com.helospark.spark.converter.handlers.service.collector.collectors;

import java.util.List;

import com.helospark.spark.converter.handlers.domain.ConverterInputParameters;
import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertType;
import com.helospark.spark.converter.handlers.service.common.domain.SourceDestinationType;

public interface MethodCollectorChain {

    public ConverterTypeCodeGenerationRequest handle(ConverterInputParameters converterInputParameters, SourceDestinationType sourceDestination,
            List<ConverterTypeCodeGenerationRequest> result);

    public boolean canHandle(ConvertType type);
}

package com.helospark.spark.converter.handlers.domain;

import java.util.List;

public class ConverterTypeCodeGenerationRequest {
    private String className;
    private String packageName;
    private List<ConverterTypeCodeGenerationRequest> dependencies;
    private List<ConverterMethodCodeGenerationRequest> methods;
}

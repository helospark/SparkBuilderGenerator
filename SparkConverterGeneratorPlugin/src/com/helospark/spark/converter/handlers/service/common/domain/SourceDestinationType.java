package com.helospark.spark.converter.handlers.service.common.domain;

import com.helospark.spark.converter.handlers.domain.TemplatedIType;

public class SourceDestinationType {
    private TemplatedIType sourceType;
    private TemplatedIType destinationType;

    public SourceDestinationType(TemplatedIType sourceType, TemplatedIType destinationType) {
        this.sourceType = sourceType;
        this.destinationType = destinationType;
    }

    public TemplatedIType getSourceType() {
        return sourceType;
    }

    public TemplatedIType getDestinationType() {
        return destinationType;
    }

}

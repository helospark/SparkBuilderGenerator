package com.helospark.spark.converter.handlers.service.collector.converttype.impl;

import com.helospark.spark.converter.handlers.domain.TemplatedIType;
import com.helospark.spark.converter.handlers.service.collector.converttype.ConvertableDomainBuilderChainItem;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertType;

public class OptionalUnwrapConvertTypeBuilder implements ConvertableDomainBuilderChainItem {
    private static final String OPTIONAL_FULLY_QUALIFIED_NAME = "java.util.Optional";

    @Override
    public boolean isApplicable(TemplatedIType sourceType, TemplatedIType destinationType) {
        return sourceType.getType().getFullyQualifiedName().equals(OPTIONAL_FULLY_QUALIFIED_NAME)
                && sourceType.getTemplates().get(0).equals(destinationType);
    }

    @Override
    public ConvertType getValue() {
        return ConvertType.OPTIONAL_UNWRAP;
    }

}

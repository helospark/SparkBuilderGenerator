package com.helospark.spark.converter.handlers.service.collector.converttype.impl;

import com.helospark.spark.converter.handlers.domain.TemplatedIType;
import com.helospark.spark.converter.handlers.service.collector.converttype.ConvertableDomainBuilderChainItem;
import com.helospark.spark.converter.handlers.service.domain.ConvertType;

public class CollectionConvertTypeBuilder implements ConvertableDomainBuilderChainItem {
    private static final String LIST_FULLY_QUALIFIED_NAME = "java.util.List";

    @Override
    public boolean isApplicable(TemplatedIType sourceType, TemplatedIType destinationType) {
        return sourceType.getType().getFullyQualifiedName().equals(LIST_FULLY_QUALIFIED_NAME)
                && destinationType.getType().getFullyQualifiedName().equals(LIST_FULLY_QUALIFIED_NAME)
                && !sourceType.getTemplates().get(0).equals(destinationType.getTemplates().get(0));
    }

    @Override
    public ConvertType getValue() {
        return ConvertType.COLLECTION_CONVERT;
    }

}

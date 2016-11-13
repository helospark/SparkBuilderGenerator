package com.helospark.spark.converter.handlers.service.collector.converttype.impl;

import com.helospark.spark.converter.handlers.domain.TemplatedIType;
import com.helospark.spark.converter.handlers.service.collector.converttype.ConvertableDomainBuilderChainItem;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertType;

public class ConverterRequiredConvertTypeBuilder implements ConvertableDomainBuilderChainItem {

    @Override
    public boolean isApplicable(TemplatedIType sourceType, TemplatedIType destinationType) {
        return !sourceType.equals(destinationType);
    }

    @Override
    public ConvertType getValue() {
        return ConvertType.CONVERT;
    }

}

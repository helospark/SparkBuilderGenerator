package com.helospark.spark.converter.handlers.service.collector.converttype.impl;

import com.helospark.spark.converter.handlers.domain.TemplatedIType;
import com.helospark.spark.converter.handlers.service.collector.converttype.ConvertableDomainBuilderChainItem;
import com.helospark.spark.converter.handlers.service.domain.ConvertType;

public class ReferenceCopyConvertTypeBuilder implements ConvertableDomainBuilderChainItem {

    @Override
    public boolean isApplicable(TemplatedIType sourceType, TemplatedIType destinationType) {
        return sourceType.equals(destinationType);
    }

    @Override
    public ConvertType getValue() {
        return ConvertType.REFERENCE_COPY;
    }

}

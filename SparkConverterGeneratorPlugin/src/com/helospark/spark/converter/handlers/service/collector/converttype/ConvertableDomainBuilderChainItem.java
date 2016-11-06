package com.helospark.spark.converter.handlers.service.collector.converttype;

import com.helospark.spark.converter.handlers.domain.TemplatedIType;
import com.helospark.spark.converter.handlers.service.domain.ConvertType;

public interface ConvertableDomainBuilderChainItem {

    public boolean isApplicable(TemplatedIType sourceType, TemplatedIType destinationType);

    public ConvertType getValue();

}

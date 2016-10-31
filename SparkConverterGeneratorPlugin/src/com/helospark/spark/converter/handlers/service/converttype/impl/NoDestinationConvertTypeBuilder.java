package com.helospark.spark.converter.handlers.service.converttype.impl;

import java.util.Optional;

import org.eclipse.jdt.core.IMethod;

import com.helospark.spark.converter.handlers.service.converttype.ConvertableDomainBuilderChainItem;
import com.helospark.spark.converter.handlers.service.domain.ConvertType;

public class NoDestinationConvertTypeBuilder implements ConvertableDomainBuilderChainItem {

    @Override
    public boolean isApplicable(IMethod sourceMethod, Optional<IMethod> destinationMethod) {
        return !destinationMethod.isPresent();
    }

    @Override
    public ConvertType getValue() {
        return ConvertType.NO_DESTINATION;
    }

}

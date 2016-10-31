package com.helospark.spark.converter.handlers.service.converttype.impl;

import java.util.Optional;

import org.eclipse.jdt.core.IMethod;

import com.helospark.spark.converter.handlers.service.converttype.ConvertableDomainBuilderChainItem;
import com.helospark.spark.converter.handlers.service.domain.ConvertType;

public class ConverterRequiredConvertTypeBuilder implements ConvertableDomainBuilderChainItem {

    @Override
    public boolean isApplicable(IMethod sourceMethod, Optional<IMethod> destinationMethod) {
        try {
            return destinationMethod.isPresent() &&
                    !sourceMethod.getReturnType().equals(destinationMethod.get().getParameters()[0].getTypeSignature());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ConvertType getValue() {
        return ConvertType.CONVERT;
    }

}

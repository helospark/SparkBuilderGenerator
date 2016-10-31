package com.helospark.spark.converter.handlers.service.converttype.impl;

import java.util.Optional;

import org.eclipse.jdt.core.IMethod;

import com.helospark.spark.converter.handlers.service.converttype.ConvertableDomainBuilderChainItem;
import com.helospark.spark.converter.handlers.service.domain.ConvertType;

public class ReferenceCopyConvertTypeBuilder implements ConvertableDomainBuilderChainItem {

    @Override
    public boolean isApplicable(IMethod sourceMethod, Optional<IMethod> destinationMethod) {
        try {
            if (destinationMethod.isPresent()) {
                String destinationParameterSignature = destinationMethod.get().getParameters()[0].getTypeSignature();
                return sourceMethod.getReturnType().equals(destinationParameterSignature);
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ConvertType getValue() {
        return ConvertType.REFERENCE_COPY;
    }

}

package com.helospark.spark.converter.handlers.service.converttype;

import java.util.Optional;

import org.eclipse.jdt.core.IMethod;

import com.helospark.spark.converter.handlers.service.domain.ConvertType;

public interface ConvertableDomainBuilderChainItem {

    public boolean isApplicable(IMethod sourceMethod, Optional<IMethod> destinationMethod);

    public ConvertType getValue();

}

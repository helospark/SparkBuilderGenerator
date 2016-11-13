package com.helospark.spark.converter.handlers.service.collector.converttype;

import java.util.List;
import java.util.Optional;

import com.helospark.spark.converter.handlers.domain.TemplatedIType;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertType;

public class ConvertTypeFinder {
    private List<ConvertableDomainBuilderChainItem> convertableDomainBuilderChainItems;

    public ConvertTypeFinder(List<ConvertableDomainBuilderChainItem> convertableDomainBuilderChainItems) {
        super();
        this.convertableDomainBuilderChainItems = convertableDomainBuilderChainItems;
    }

    public ConvertType findConvertType(Optional<TemplatedIType> sourceType, Optional<TemplatedIType> destinationType) {
        ConvertType type = ConvertType.UNKNOWN;
        if (!destinationType.isPresent()) {
            type = ConvertType.NO_DESTINATION;
        } else if (!sourceType.isPresent()) {
            type = ConvertType.NO_SOURCE;
        } else {
            type = convertableDomainBuilderChainItems.stream()
                    .filter(item -> item.isApplicable(sourceType.get(), destinationType.get()))
                    .map(item -> item.getValue())
                    .findFirst()
                    .orElse(ConvertType.UNKNOWN);
        }
        return type;
    }
}

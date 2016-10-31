package com.helospark.spark.converter.handlers.service.fieldcollector.impl;

import com.helospark.spark.converter.handlers.domain.AccessFieldDeclaration;
import com.helospark.spark.converter.handlers.domain.FieldCollectorResultKind;
import com.helospark.spark.converter.handlers.service.fieldcollector.FieldCollectorResultKindFinder;

public class ReferenceCopyFinder implements FieldCollectorResultKindFinder {

    @Override
    public FieldCollectorResultKind getValue() {
        return FieldCollectorResultKind.REFERENCE_COPY;
    }

    @Override
    public boolean isApplicable(AccessFieldDeclaration source, AccessFieldDeclaration destination) {
        return source.getType().equals(destination.getType());
    }

}

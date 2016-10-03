package com.helospark.spark.converter.handlers.service.fieldcollector.impl;

import com.helospark.spark.converter.handlers.domain.AccessFieldDeclaration;
import com.helospark.spark.converter.handlers.domain.FieldCollectorResultKind;
import com.helospark.spark.converter.handlers.service.fieldcollector.FieldCollectorResultKindFinder;

public class ConverterRequiredResultKindFinder implements FieldCollectorResultKindFinder {

    @Override
    public FieldCollectorResultKind getValue() {
        return FieldCollectorResultKind.CONVERTER_REQUIRED;
    }

    @Override
    public boolean isApplicable(AccessFieldDeclaration source, AccessFieldDeclaration destination) {
        return !source.getType().equals(destination.getType());
    }

}

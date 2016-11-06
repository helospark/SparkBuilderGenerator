package com.helospark.spark.converter.handlers.service.common.fieldcollector;

import com.helospark.spark.converter.handlers.domain.AccessFieldDeclaration;
import com.helospark.spark.converter.handlers.domain.FieldCollectorResultKind;

public interface FieldCollectorResultKindFinder {

    public FieldCollectorResultKind getValue();

    public boolean isApplicable(AccessFieldDeclaration source, AccessFieldDeclaration destination);
}

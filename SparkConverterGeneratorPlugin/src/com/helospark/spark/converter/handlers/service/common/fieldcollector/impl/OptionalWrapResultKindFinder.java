package com.helospark.spark.converter.handlers.service.common.fieldcollector.impl;

import org.eclipse.jdt.core.IType;

import com.helospark.spark.converter.handlers.domain.AccessFieldDeclaration;
import com.helospark.spark.converter.handlers.domain.FieldCollectorResultKind;
import com.helospark.spark.converter.handlers.service.common.ITypeInstanceOf;
import com.helospark.spark.converter.handlers.service.common.fieldcollector.FieldCollectorResultKindFinder;

public class OptionalWrapResultKindFinder implements FieldCollectorResultKindFinder {
    private ITypeInstanceOf iTypeInstanceOf;
    private GenericTypeExtractor genericTypeExtractor;

    @Override
    public FieldCollectorResultKind getValue() {
        return FieldCollectorResultKind.OPTIONAL_WRAP;
    }

    @Override
    public boolean isApplicable(AccessFieldDeclaration source, AccessFieldDeclaration destination) {
        boolean isInstanceOfOptional = iTypeInstanceOf.isInstanceOf(destination.getType(), "java.util.Optional");
        IType destinationGenericType = genericTypeExtractor.extract(destination.getType());
        return isInstanceOfOptional && destinationGenericType.equals(source.getType());
    }

}

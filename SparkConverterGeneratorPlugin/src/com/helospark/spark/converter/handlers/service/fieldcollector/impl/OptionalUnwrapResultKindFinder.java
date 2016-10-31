package com.helospark.spark.converter.handlers.service.fieldcollector.impl;

import org.eclipse.jdt.core.IType;

import com.helospark.spark.converter.handlers.domain.AccessFieldDeclaration;
import com.helospark.spark.converter.handlers.domain.FieldCollectorResultKind;
import com.helospark.spark.converter.handlers.service.GenericTypeExtractor;
import com.helospark.spark.converter.handlers.service.ITypeInstanceOf;
import com.helospark.spark.converter.handlers.service.fieldcollector.FieldCollectorResultKindFinder;

public class OptionalUnwrapResultKindFinder implements FieldCollectorResultKindFinder {
    private ITypeInstanceOf iTypeInstanceOf;
    private GenericTypeExtractor genericTypeExtractor;

    @Override
    public FieldCollectorResultKind getValue() {
        return FieldCollectorResultKind.OPTIONAL_UNWRAP;
    }

    @Override
    public boolean isApplicable(AccessFieldDeclaration source, AccessFieldDeclaration destination) {
        boolean isInstanceOfOptional = iTypeInstanceOf.isInstanceOf(source.getType(), "java.util.Optional");
        IType sourceGenericType = genericTypeExtractor.extract(source.getType());
        return isInstanceOfOptional && sourceGenericType.equals(destination.getType());
    }

}

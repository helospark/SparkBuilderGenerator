package com.helospark.spark.converter.handlers.service.emitter.unittestemitter.impl.mockvalueprovider.impl;

import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.converter.handlers.domain.TemplatedIType;
import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertableDomainParameter;
import com.helospark.spark.converter.handlers.service.emitter.unittestemitter.impl.mockvalueprovider.MockValueProvider;
import com.helospark.spark.converter.handlers.service.emitter.unittestemitter.impl.mockvalueprovider.domain.MockValuePair;

public class LongMockValueProvider implements MockValueProvider {

    @Override
    public MockValuePair provideMockValue(TypeDeclaration unitTest, CompilationUnitModificationDomain compilationUnit, ConvertableDomainParameter parameter) {
        NumberLiteral literal = compilationUnit.getAst().newNumberLiteral("1L");
        return new MockValuePair(literal, literal, parameter);
    }

    @Override
    public boolean canHandle(TemplatedIType type) {
        return type.getType().getFullyQualifiedName().equals("java.util.Long") ||
                type.getType().getFullyQualifiedName().equals("long");
    }

}

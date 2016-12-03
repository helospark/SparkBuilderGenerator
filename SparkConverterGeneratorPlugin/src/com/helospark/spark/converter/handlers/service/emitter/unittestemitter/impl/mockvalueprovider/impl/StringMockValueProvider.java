package com.helospark.spark.converter.handlers.service.emitter.unittestemitter.impl.mockvalueprovider.impl;

import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.converter.handlers.domain.TemplatedIType;
import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertableDomainParameter;
import com.helospark.spark.converter.handlers.service.emitter.unittestemitter.impl.mockvalueprovider.MockValueProvider;
import com.helospark.spark.converter.handlers.service.emitter.unittestemitter.impl.mockvalueprovider.domain.MockValuePair;

public class StringMockValueProvider implements MockValueProvider {

    @Override
    public MockValuePair provideMockValue(TypeDeclaration unitTest, CompilationUnitModificationDomain compilationUnit, ConvertableDomainParameter parameter) {
        StringLiteral result = compilationUnit.getAst().newStringLiteral();
        result.setLiteralValue(parameter.getSourceParameterName());
        return new MockValuePair(result, result, parameter);
    }

    @Override
    public boolean canHandle(TemplatedIType type) {
        return type.getType().getFullyQualifiedName().equals("java.util.String");
    }

}

package com.helospark.spark.builder.handlers.it.dummyService;

import static java.util.Optional.ofNullable;

import java.util.Map;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.mockito.invocation.InvocationOnMock;

/**
 * Provide IType based on TypeDeclaration.
 * @author helospark
 */
public class TypeExtractorAnswerProvider {
    private Map<String, IType> typeNameToITypeMapping;

    public TypeExtractorAnswerProvider(Map<String, IType> types) {
        this.typeNameToITypeMapping = types;
    }

    public Object provideAnswer(InvocationOnMock inv) {
        TypeDeclaration result = (TypeDeclaration) inv.getArguments()[0];
        String typeName = result.getName().toString();
        return ofNullable(typeNameToITypeMapping.get(typeName));
    }
}

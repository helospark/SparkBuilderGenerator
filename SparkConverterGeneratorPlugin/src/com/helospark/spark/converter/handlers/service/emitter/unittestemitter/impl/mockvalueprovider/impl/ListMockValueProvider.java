package com.helospark.spark.converter.handlers.service.emitter.unittestemitter.impl.mockvalueprovider.impl;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.converter.handlers.domain.TemplatedIType;
import com.helospark.spark.converter.handlers.service.common.ImportPopulator;
import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertableDomainParameter;
import com.helospark.spark.converter.handlers.service.emitter.unittestemitter.impl.mockvalueprovider.MockValueProvider;
import com.helospark.spark.converter.handlers.service.emitter.unittestemitter.impl.mockvalueprovider.domain.MockValuePair;

public class ListMockValueProvider implements MockValueProvider {
    private ImportPopulator importPopulator;

    public ListMockValueProvider(ImportPopulator importPopulator) {
        this.importPopulator = importPopulator;
    }

    @Override
    public MockValuePair provideMockValue(TypeDeclaration unitTest, CompilationUnitModificationDomain compilationUnit, ConvertableDomainParameter parameter) {
        String collections = importPopulator.addImport(compilationUnit, "java.util.Collections");
        AST ast = compilationUnit.getAst();
        MethodInvocation methodInvocation = ast.newMethodInvocation();
        methodInvocation.setName(ast.newSimpleName("emptyList"));
        methodInvocation.setExpression(ast.newSimpleName(collections));
        return new MockValuePair(methodInvocation, methodInvocation, parameter);
    }

    @Override
    public boolean canHandle(TemplatedIType type) {
        return type.getType().getFullyQualifiedName().equals("java.util.List");
    }
}

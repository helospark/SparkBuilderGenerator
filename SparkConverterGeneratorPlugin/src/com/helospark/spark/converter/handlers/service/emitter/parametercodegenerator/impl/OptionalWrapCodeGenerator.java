package com.helospark.spark.converter.handlers.service.emitter.parametercodegenerator.impl;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;

import com.helospark.spark.converter.handlers.domain.ConverterMethodCodeGenerationRequest;
import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.common.ImportPopulator;
import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertType;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertableDomainParameter;
import com.helospark.spark.converter.handlers.service.emitter.parametercodegenerator.ParameterConvertCodeGenerator;

public class OptionalWrapCodeGenerator implements ParameterConvertCodeGenerator {
    private ImportPopulator importPopulator;

    public OptionalWrapCodeGenerator(ImportPopulator importPopulator) {
        this.importPopulator = importPopulator;
    }

    @Override
    public Expression generate(CompilationUnitModificationDomain compilationUnitModificationDomain, Block body, Expression expression, Expression sourceObject,
            ConvertableDomainParameter parameter, ConverterTypeCodeGenerationRequest generationRequest, ConverterMethodCodeGenerationRequest method) {
        AST ast = compilationUnitModificationDomain.getAst();

        importPopulator.addImport(compilationUnitModificationDomain, "java.util.Optional");

        MethodInvocation setMethodInvocation = ast.newMethodInvocation();
        setMethodInvocation.setExpression(expression);
        setMethodInvocation.setName(ast.newSimpleName(parameter.getDestinationMethodName()));

        MethodInvocation getMethodInvocation = ast.newMethodInvocation();
        getMethodInvocation.setExpression(sourceObject);
        getMethodInvocation.setName(ast.newSimpleName(parameter.getSourceMethodName()));

        MethodInvocation optionalOfMethod = ast.newMethodInvocation();
        optionalOfMethod.setExpression(ast.newSimpleName("Optional"));
        optionalOfMethod.setName(ast.newSimpleName("of"));
        optionalOfMethod.arguments().add(getMethodInvocation);

        setMethodInvocation.arguments().add(optionalOfMethod);

        return setMethodInvocation;
    }

    @Override
    public boolean canHandle(ConvertType type) {
        return type.equals(ConvertType.OPTIONAL_WRAP);
    }

}

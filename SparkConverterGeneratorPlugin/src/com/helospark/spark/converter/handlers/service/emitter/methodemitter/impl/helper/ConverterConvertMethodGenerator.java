package com.helospark.spark.converter.handlers.service.emitter.methodemitter.impl.helper;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import com.helospark.spark.converter.handlers.domain.ConverterMethodCodeGenerationRequest;
import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.common.BuilderInitializationValueProvider;
import com.helospark.spark.converter.handlers.service.common.BuilderReturnStatementProvider;
import com.helospark.spark.converter.handlers.service.common.ImportPopulator;
import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertableDomain;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertableDomainParameter;
import com.helospark.spark.converter.handlers.service.emitter.parametercodegenerator.ParameterConvertCodeGenerator;

public class ConverterConvertMethodGenerator {
    private List<ParameterConvertCodeGenerator> codeGenerators;
    private ImportPopulator importPopulator;
    private BuilderInitializationValueProvider builderInitializationValueProvider;
    private BuilderReturnStatementProvider builderReturnStatementProvider;

    public ConverterConvertMethodGenerator(List<ParameterConvertCodeGenerator> codeGenerators, ImportPopulator importPopulator,
            BuilderInitializationValueProvider builderInitializationValueProvider, BuilderReturnStatementProvider builderReturnStatementProvider) {
        this.codeGenerators = codeGenerators;
        this.importPopulator = importPopulator;
        this.builderInitializationValueProvider = builderInitializationValueProvider;
        this.builderReturnStatementProvider = builderReturnStatementProvider;
    }

    public MethodDeclaration generate(CompilationUnitModificationDomain compilationUnitModificationDomain, ConvertableDomain convertableDomain,
            ConverterTypeCodeGenerationRequest generationRequest, ConverterMethodCodeGenerationRequest method) {
        AST ast = compilationUnitModificationDomain.getAst();

        String sourceTypeName = importPopulator.addImport(compilationUnitModificationDomain, method.getSourceType().getType().getFullyQualifiedName());
        String destinationTypeName = importPopulator.addImport(compilationUnitModificationDomain, method.getDestinationType().getType().getFullyQualifiedName());

        SingleVariableDeclaration methodParameterDeclaration = ast.newSingleVariableDeclaration();
        SimpleType sourceType = ast.newSimpleType(ast.newName(sourceTypeName));
        methodParameterDeclaration.setType(sourceType);
        methodParameterDeclaration.setName(ast.newSimpleName(method.getParameterName()));

        MethodDeclaration newMethod = ast.newMethodDeclaration();
        newMethod.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
        newMethod.setName(ast.newSimpleName(method.getName()));
        newMethod.setReturnType2(ast.newSimpleType(ast.newName(destinationTypeName)));
        newMethod.parameters().add(methodParameterDeclaration);
        Block body = ast.newBlock();

        Expression lastDeclaration = builderInitializationValueProvider.addLastDeclarationInitialization(compilationUnitModificationDomain, convertableDomain,
                destinationTypeName,
                body);

        for (ConvertableDomainParameter parameter : convertableDomain.getConvertableDomainParameters()) {
            if (!convertableDomain.isUseBuilder()) {
                lastDeclaration = ast.newName("destination");
            }
            Expression generatedCopy = copyParameter(compilationUnitModificationDomain, body, lastDeclaration, ast.newName(method.getParameterName()), parameter,
                    generationRequest, method);
            if (!convertableDomain.isUseBuilder()) {
                body.statements().add(ast.newExpressionStatement(generatedCopy));
            } else {
                lastDeclaration = generatedCopy;
            }
        }
        ReturnStatement returnStatement = builderReturnStatementProvider.initializeBuilderReturn(convertableDomain, ast, lastDeclaration);
        body.statements().add(returnStatement);
        newMethod.setBody(body);
        return newMethod;
    }

    private Expression copyParameter(CompilationUnitModificationDomain compilationUnitModificationDomain, Block body, Expression lastDeclaration, Expression source,
            ConvertableDomainParameter parameter, ConverterTypeCodeGenerationRequest generationRequest, ConverterMethodCodeGenerationRequest method) {
        ParameterConvertCodeGenerator codeGenerator = findCodeGenerator(parameter);
        return codeGenerator.generate(compilationUnitModificationDomain, body, lastDeclaration, source, parameter, generationRequest, method);
    }

    private ParameterConvertCodeGenerator findCodeGenerator(ConvertableDomainParameter parameter) {
        return codeGenerators.stream()
                .filter(generator -> generator.canHandle(parameter.getType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Generation type not supported: " + parameter.getType()));
    }

}

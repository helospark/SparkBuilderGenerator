package com.helospark.spark.converter.handlers.service.emitter.unittestemitter.impl;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import com.helospark.spark.converter.handlers.domain.ConverterMethodCodeGenerationRequest;
import com.helospark.spark.converter.handlers.domain.ConverterMethodType;
import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.domain.TemplatedIType;
import com.helospark.spark.converter.handlers.service.common.BuilderInitializationValueProvider;
import com.helospark.spark.converter.handlers.service.common.BuilderReturnStatementProvider;
import com.helospark.spark.converter.handlers.service.common.ConvertableParametersGenerator;
import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;
import com.helospark.spark.converter.handlers.service.emitter.MockValueListProvider;
import com.helospark.spark.converter.handlers.service.emitter.helper.FieldDeclarationBuilder;
import com.helospark.spark.converter.handlers.service.emitter.helper.MarkerAnnotationBuilder;
import com.helospark.spark.converter.handlers.service.emitter.helper.VariableDeclarationBuilder;
import com.helospark.spark.converter.handlers.service.emitter.unittestemitter.UnitTestMethodEmitter;
import com.helospark.spark.converter.handlers.service.emitter.unittestemitter.impl.mockvalueprovider.MockValueProvider;
import com.helospark.spark.converter.handlers.service.emitter.unittestemitter.impl.mockvalueprovider.domain.MockValuePair;

public class ConverterUnitTestMethodEmitter implements UnitTestMethodEmitter {
    private ConvertableParametersGenerator convertableParametersGenerator;
    private FieldDeclarationBuilder fieldDeclarationBuilder;
    private MarkerAnnotationBuilder markerAnnotationBuilder;
    private List<MockValueProvider> mockValueProviders;
    private MockValueListProvider mockValueListProvider;
    private BuilderInitializationValueProvider builderInitializationValueProvider;
    private BuilderReturnStatementProvider builderReturnStatementProvider;
    private VariableDeclarationBuilder variableDeclarationBuilder;

    @Override
    public void generateMethod(CompilationUnitModificationDomain compilationUnit, ConverterTypeCodeGenerationRequest generationRequest, TypeDeclaration newType,
            ConverterMethodCodeGenerationRequest method) {
        List<MockValuePair> mockValuePairs = mockValueListProvider.addMockValuesAndGet(compilationUnit, newType, method);
        addCreateDummyObjectMethod(compilationUnit, generationRequest, method, mockValuePairs);
        addCreateExpectedResultMethod(compilationUnit, generationRequest, method, mockValuePairs);
        addVerifyMethods(compilationUnit, generationRequest, method, mockValuePairs);
        createAssertEqualsMethod(compilationUnit, generationRequest, method, mockValuePairs);
    }

    private MethodDeclaration addCreateDummyObjectMethod(CompilationUnitModificationDomain compilationUnit, ConverterTypeCodeGenerationRequest generationRequest,
            ConverterMethodCodeGenerationRequest methodRequest, List<MockValuePair> mockValuePairs) {
        AST ast = compilationUnit.getAst();
        MethodDeclaration createDummyMethod = ast.newMethodDeclaration();
        createDummyMethod.modifiers().add(ast.newModifier(ModifierKeyword.PRIVATE_KEYWORD));
        createDummyMethod.setName(ast.newSimpleName("createDummy" + generationRequest.getClassName()));
        createDummyMethod.setReturnType2(ast.newSimpleType(ast.newName(generationRequest.getClassName())));
        createDummyMethod.setBody(createBodyWithDummyParameters(compilationUnit, methodRequest, mockValuePairs, methodRequest.getSourceType()));
        return createDummyMethod;
    }

    private MethodDeclaration addCreateExpectedResultMethod(CompilationUnitModificationDomain compilationUnit, ConverterTypeCodeGenerationRequest generationRequest,
            ConverterMethodCodeGenerationRequest methodRequest, List<MockValuePair> mockValuePairs) {
        AST ast = compilationUnit.getAst();
        MethodDeclaration createDummyMethod = ast.newMethodDeclaration();
        createDummyMethod.modifiers().add(ast.newModifier(ModifierKeyword.PRIVATE_KEYWORD));
        createDummyMethod.setName(ast.newSimpleName("createDummy" + generationRequest.getClassName()));
        createDummyMethod.setReturnType2(ast.newSimpleType(ast.newName(generationRequest.getClassName())));
        createDummyMethod.setBody(createBodyWithDummyParameters(compilationUnit, methodRequest, mockValuePairs, methodRequest.getDestinationType()));
        return createDummyMethod;
    }

    private Block createBodyWithDummyParameters(CompilationUnitModificationDomain compilationUnit, ConverterMethodCodeGenerationRequest generationRequest,
            List<MockValuePair> mockValuePairs, TemplatedIType destination) {
        AST ast = compilationUnit.getAst();
        Block result = ast.newBlock();
        Expression lastExpression = builderInitializationValueProvider.addLastDeclarationInitialization(compilationUnit, convertableDomain,
                destination.getAsString(), result);
        for (MockValuePair mockValuePair : mockValuePairs) {
            if (useBuilder) {
                lastExpression = ast.newName("destination");
            }

            MethodInvocation setter = ast.newMethodInvocation();
            setter.setExpression(lastExpression);
            setter.setName(ast.newSimpleName(mockValuePair.getParameterProperties().getDestinationMethodName()));
            setter.arguments().add(mockValuePair.getDestinationExpression());

            if (!useBuilder) {
                result.statements().add(ast.newExpressionStatement(setter));
            } else {
                lastExpression = setter;
            }
        }
        ReturnStatement returnStatement = builderReturnStatementProvider.initializeBuilderReturn(convertableDomain, ast, lastDeclaration);
        result.statements().add(returnStatement);
        return result;
    }

    private void addVerifyMethods(CompilationUnitModificationDomain compilationUnit, ConverterTypeCodeGenerationRequest generationRequest,
            ConverterMethodCodeGenerationRequest method, List<MockValuePair> mockValuePairs) {
        mockValuePairs.stream()
                .filter(pair -> !pair.isSourceSameAsDestination())
                .forEach(pair -> addVerifyMethod(compilationUnit, generationRequest, method, pair));

    }

    private void addVerifyMethod(CompilationUnitModificationDomain compilationUnit, ConverterTypeCodeGenerationRequest generationRequest,
            ConverterMethodCodeGenerationRequest method, MockValuePair pair) {
        AST ast = compilationUnit.getAst();
        MethodDeclaration methodDeclaration = ast.newMethodDeclaration();
        methodDeclaration.setName(ast.newSimpleName("test" + method.getName() + "ShouldCall" + pair.getConvertingDependency().getClassName()));
        methodDeclaration.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
        methodDeclaration.modifiers().add(markerAnnotationBuilder.buildAnnotation(compilationUnit, "org.testng.Test"));
        methodDeclaration.setBody(createVerifyBody(compilationUnit, generationRequest, method, pair));
    }

    private Block createVerifyBody(CompilationUnitModificationDomain compilationUnit, ConverterTypeCodeGenerationRequest generationRequest,
            ConverterMethodCodeGenerationRequest method, MockValuePair pair) {
        AST ast = compilationUnit.getAst();
        Block result = ast.newBlock();
        final String toConvertName = "qwe";
        final String createDummyMethodName = "createDummy";
        // given
        createGiven(ast, toConvertName, createDummyMethodName);

        // when
        createWhen(method, ast, result, toConvertName);

        // then
        MethodInvocation assertThatInvocation = ast.newMethodInvocation();
        assertThatInvocation.setName(ast.newSimpleName("verify"));
        assertThatInvocation.setExpression(ast.newName("Hamcrest"));
        assertThatInvocation.arguments().add(ast.newSimpleName("underTest"));

        MethodInvocation verifyMethod = (MethodInvocation) underTestInvocation.copySubtree(ast, underTestInvocation);
        verifyMethod.setExpression(assertThatInvocation);
        result.statements().add(verifyMethod);

        return result;
    }

    private void createAssertEqualsMethod(CompilationUnitModificationDomain compilationUnit, ConverterTypeCodeGenerationRequest generationRequest,
            ConverterMethodCodeGenerationRequest method, List<MockValuePair> mockValuePairs) {
        AST ast = compilationUnit.getAst();
        Block result = ast.newBlock();
        final String toConvertName = "qwe";
        final String createDummyMethodName = "createDummy";
        // given
        MethodInvocation createDummyInvocation = ast.newMethodInvocation();
        createDummyInvocation.setName(ast.newSimpleName(createDummyMethodName));
        variableDeclarationBuilder.buildVariableDeclaration(ast, "expectedResult", createDummyInvocation);

        createGiven(ast, toConvertName, createDummyMethodName);

        // when
        createWhen(method, ast, result, toConvertName);

        // then
        MethodInvocation assertThatInvocation = ast.newMethodInvocation();
        assertThatInvocation.setName(ast.newSimpleName("assertEquals"));
        assertThatInvocation.setExpression(ast.newName("Assert"));
        assertThatInvocation.arguments().add(ast.newName("result"));
        assertThatInvocation.arguments().add(ast.newName("expectedResult"));
        result.statements().add(assertThatInvocation);
    }

    private void createWhen(ConverterMethodCodeGenerationRequest method, AST ast, Block result, final String toConvertName) {
        MethodInvocation underTestInvocation = ast.newMethodInvocation();
        underTestInvocation.setName(ast.newSimpleName(method.getName()));
        underTestInvocation.setExpression(ast.newSimpleName("underTest"));
        underTestInvocation.arguments().add(ast.newSimpleName(toConvertName));
        VariableDeclarationStatement resultStatement = variableDeclarationBuilder.buildVariableDeclaration(ast, "result", underTestInvocation);
        result.statements().add(resultStatement);
    }

    private void createGiven(AST ast, final String toConvertName, final String createDummyMethodName) {
        MethodInvocation createConvertableInvocation = ast.newMethodInvocation();
        createConvertableInvocation.setName(ast.newSimpleName(createDummyMethodName));
        variableDeclarationBuilder.buildVariableDeclaration(ast, toConvertName, createConvertableInvocation);
    }

    @Override
    public boolean canHandle(ConverterMethodType converterMethodType) {
        return ConverterMethodType.REGULAR_CONVERTER.equals(converterMethodType);
    }

}

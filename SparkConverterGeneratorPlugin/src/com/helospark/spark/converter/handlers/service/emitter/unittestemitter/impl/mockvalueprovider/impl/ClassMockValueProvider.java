package com.helospark.spark.converter.handlers.service.emitter.unittestemitter.impl.mockvalueprovider.impl;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.converter.handlers.domain.TemplatedIType;
import com.helospark.spark.converter.handlers.service.common.ImportPopulator;
import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;
import com.helospark.spark.converter.handlers.service.common.domain.ConvertableDomainParameter;
import com.helospark.spark.converter.handlers.service.emitter.helper.FieldDeclarationBuilder;
import com.helospark.spark.converter.handlers.service.emitter.helper.MarkerAnnotationBuilder;
import com.helospark.spark.converter.handlers.service.emitter.unittestemitter.impl.mockvalueprovider.MockValueProvider;
import com.helospark.spark.converter.handlers.service.emitter.unittestemitter.impl.mockvalueprovider.domain.MockValuePair;

public class ClassMockValueProvider implements MockValueProvider {
    private ImportPopulator importPopulator;
    private FieldDeclarationBuilder fieldDeclarationBuilder;
    private MarkerAnnotationBuilder markerAnnotationBuilder;

    public ClassMockValueProvider(ImportPopulator importPopulator, FieldDeclarationBuilder fieldDeclarationBuilder, MarkerAnnotationBuilder markerAnnotationBuilder) {
        this.importPopulator = importPopulator;
        this.fieldDeclarationBuilder = fieldDeclarationBuilder;
        this.markerAnnotationBuilder = markerAnnotationBuilder;
    }

    @Override
    public MockValuePair provideMockValue(TypeDeclaration unitTest, CompilationUnitModificationDomain compilationUnit, ConvertableDomainParameter parameter) {
        TemplatedIType sourceType = parameter.getSourceType();
        String mockName = "mock" + sourceType.getType().getElementName();
        FieldDeclaration fieldDeclaration = fieldDeclarationBuilder.getFieldDeclaration(compilationUnit, mockName, sourceType.getType().getElementName(),
                sourceType.getAsString());
        Annotation mockAnnotation = markerAnnotationBuilder.buildAnnotation(compilationUnit, "org.mockito.Mock");
        fieldDeclaration.modifiers().add(mockAnnotation);

        SimpleName mock = compilationUnit.getAst().newSimpleName(mockName);
        return new MockValuePair(mock, mock, parameter);
    }

    @Override
    public boolean canHandle(TemplatedIType type) {
        return true;
    }

}

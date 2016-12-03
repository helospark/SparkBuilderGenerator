package com.helospark.spark.converter.handlers.service.emitter.helper;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.converter.handlers.domain.ConverterTypeCodeGenerationRequest;
import com.helospark.spark.converter.handlers.service.common.domain.CompilationUnitModificationDomain;

public class ConverterFieldEmitter {
    private FieldDeclarationBuilder fieldDeclarationBuilder;

    public ConverterFieldEmitter(FieldDeclarationBuilder fieldDeclarationBuilder) {
        this.fieldDeclarationBuilder = fieldDeclarationBuilder;
    }

    public void addFields(TypeDeclaration newType, CompilationUnitModificationDomain compilationUnit, ConverterTypeCodeGenerationRequest generationRequest) {
        for (ConverterTypeCodeGenerationRequest dependency : generationRequest.getDependencies()) {
            newType.bodyDeclarations().add(findLastFieldIndex(newType), fieldDeclarationBuilder.getFieldDeclaration(compilationUnit, dependency));
        }
    }

    private int findLastFieldIndex(TypeDeclaration newType) {
        return ((List<BodyDeclaration>) newType.bodyDeclarations())
                .stream()
                .filter(element -> element instanceof FieldDeclaration)
                .collect(Collectors.toList())
                .size();
    }

}

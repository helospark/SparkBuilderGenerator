package com.helospark.spark.converter.handlers.service;

import static com.helospark.spark.converter.handlers.domain.FieldCollectorResultKind.SOURCE_CONTAINS_DESTIONATION_DOES_NOT;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import com.helospark.spark.converter.handlers.domain.AccessFieldDeclaration;
import com.helospark.spark.converter.handlers.domain.FieldCollectorResult;
import com.helospark.spark.converter.handlers.domain.FieldCollectorResultKind;
import com.helospark.spark.converter.handlers.service.fieldcollector.FieldCollectorResultKindFinder;

public class FieldCollector {
    private List<FieldCollectorResultKindFinder> fieldCollectorResultKindFinders;

    public List<FieldCollectorResult> collect(IType source, IType destination) throws JavaModelException {
        IType correctSource = source;
        if (hasBuilder(source)) {
            correctSource = getBuilder(source);
        }
        List<AccessFieldDeclaration> sourceFieldDeclatation = extractFields(source);
        List<AccessFieldDeclaration> destinationFieldDeclatation = extractFields(destination);
        List<FieldCollectorResult> result = new ArrayList<>();
        for (AccessFieldDeclaration decl : sourceFieldDeclatation) {
            if (containsFieldWithName(destinationFieldDeclatation, decl.getName())) {
                AccessFieldDeclaration dest = findFieldDeclaration(destinationFieldDeclatation, decl.getName());
                FieldCollectorResult fieldCollectorResult = FieldCollectorResult.builder()
                        .withSource(decl)
                        .withFieldCollectorResultKind(findSuitableConversionStrategy(decl, dest))
                        .build();
            } else {
                FieldCollectorResult fieldCollectorResult = FieldCollectorResult.builder()
                        .withSource(decl)
                        .withFieldCollectorResultKind(SOURCE_CONTAINS_DESTIONATION_DOES_NOT)
                        .build();
                result.add(fieldCollectorResult);
            }
        }
        for (AccessFieldDeclaration decl : destinationFieldDeclatation) {
            if (containsFieldWithName(sourceFieldDeclatation, decl.getName())) {
                FieldCollectorResult fieldCollectorResult = FieldCollectorResult.builder()
                        .withDestination(decl)
                        .withFieldCollectorResultKind(FieldCollectorResultKind.DESTIONATION_CONTAINS_SOURCE_DOES_NOT)
                        .build();
                result.add(fieldCollectorResult);
            }
        }
    }

    private List<AccessFieldDeclaration> extractFields(IType source) {
        // TODO Auto-generated method stub
        return null;
    }

    private AccessFieldDeclaration findFieldDeclaration(List<AccessFieldDeclaration> destinationFieldDeclatation, String name) {
        // TODO Auto-generated method stub
        return null;
    }

    private boolean containsFieldWithName(List<AccessFieldDeclaration> sourceFieldDeclatation, String name) {
        // TODO Auto-generated method stub
        return false;
    }

    private FieldCollectorResultKind findSuitableConversionStrategy(AccessFieldDeclaration source, AccessFieldDeclaration dest) {
        return fieldCollectorResultKindFinders.stream()
                .filter(a -> a.isApplicable(source, dest))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cannot find strategy for conversion"))
                .getValue();
    }

    private boolean hasBuilder(IType source) throws JavaModelException {
        return source.getTypes().length > 0;
    }

    private IType getBuilder(IType source) throws JavaModelException {
        return source.getTypes()[0];
    }
}

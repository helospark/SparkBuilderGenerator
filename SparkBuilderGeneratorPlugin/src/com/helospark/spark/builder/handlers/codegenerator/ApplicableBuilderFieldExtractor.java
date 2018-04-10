package com.helospark.spark.builder.handlers.codegenerator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.builderfieldcollector.ClassFieldCollector;
import com.helospark.spark.builder.handlers.codegenerator.builderfieldcollector.SuperClassSetterFieldCollector;
import com.helospark.spark.builder.handlers.codegenerator.builderfieldcollector.SuperConstructorParameterCollector;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.ConstructorParameterSetterBuilderField;

/**
 * Filters and converts given fields to {@link BuilderField}.
 *
 * @author helospark
 */
public class ApplicableBuilderFieldExtractor {
    private ClassFieldCollector classFieldCollector;
    private SuperConstructorParameterCollector superConstructorParameterCollector;
    private SuperClassSetterFieldCollector superClassSetterFieldCollector;

    public ApplicableBuilderFieldExtractor(ClassFieldCollector classFieldCollector, SuperConstructorParameterCollector superConstructorParameterCollector,
            SuperClassSetterFieldCollector superClassSetterFieldCollector) {
        this.classFieldCollector = classFieldCollector;
        this.superConstructorParameterCollector = superConstructorParameterCollector;
        this.superClassSetterFieldCollector = superClassSetterFieldCollector;
    }

    public List<BuilderField> findBuilderFields(TypeDeclaration typeDeclaration) {
        List<BuilderField> applicableFieldDeclarations = new ArrayList<>();

        // TODO: replace with chain:
        List<? extends BuilderField> superConstructorFields = superConstructorParameterCollector.findSuperclassConstructorDeclaration(typeDeclaration);
        applicableFieldDeclarations.addAll(superConstructorFields);

        List<? extends BuilderField> classFields = filterFieldsNotAlreadyInList(applicableFieldDeclarations,
                classFieldCollector.findBuilderFieldsRecursively(typeDeclaration, typeDeclaration));
        applicableFieldDeclarations.addAll(classFields);

        List<? extends BuilderField> setterFields = filterFieldsNotAlreadyInList(applicableFieldDeclarations, superClassSetterFieldCollector.collectFields(typeDeclaration));
        applicableFieldDeclarations.addAll(setterFields);

        return applicableFieldDeclarations;
    }

    private List<? extends BuilderField> filterFieldsNotAlreadyInList(List<? extends BuilderField> applicableFields, List<? extends BuilderField> toFilter) {
        List<BuilderField> result = new ArrayList<>();
        for (BuilderField field : toFilter) {
            if (!fieldDeclarationsContainConstructorField(applicableFields, field)) {
                result.add(field);
            }
        }
        return result;
    }

    private boolean fieldDeclarationsContainConstructorField(List<? extends BuilderField> applicableFields, BuilderField field) {
        for (BuilderField currentField : applicableFields) {
            if (currentField instanceof ConstructorParameterSetterBuilderField && currentField.getBuilderFieldName().equals(field.getBuilderFieldName())) {
                return true;
            }
        }
        return false;
    }

}

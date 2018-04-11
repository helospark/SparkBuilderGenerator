package com.helospark.spark.builder.handlers.codegenerator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.builderfieldcollector.FieldCollectorChainItem;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;

/**
 * Filters and converts given fields to {@link BuilderField}.
 *
 * @author helospark
 */
public class ApplicableBuilderFieldExtractor {
    private List<FieldCollectorChainItem> fieldCollectorChain;

    public ApplicableBuilderFieldExtractor(List<FieldCollectorChainItem> fieldCollectorChain) {
        this.fieldCollectorChain = fieldCollectorChain;
    }

    public List<BuilderField> findBuilderFields(TypeDeclaration typeDeclaration) {
        List<BuilderField> applicableFieldDeclarations = new ArrayList<>();

        fieldCollectorChain.stream()
                .forEach(chainItem -> {
                    List<? extends BuilderField> newFields = chainItem.collectFields(typeDeclaration);
                    List<? extends BuilderField> deduplicatedFields = filterFieldsNotAlreadyInList(applicableFieldDeclarations, newFields);
                    applicableFieldDeclarations.addAll(deduplicatedFields);
                });

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
            if (currentField.getBuilderFieldName().equals(field.getBuilderFieldName())) {
                return true;
            }
        }
        return false;
    }

}

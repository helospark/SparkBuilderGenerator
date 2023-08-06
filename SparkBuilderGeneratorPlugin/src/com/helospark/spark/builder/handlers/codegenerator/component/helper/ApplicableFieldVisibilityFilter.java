package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.domain.BodyDeclarationVisibleFromPredicate;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.ClassFieldSetterBuilderField;

/**
 * Filters given list of fields to only those elements, which are visible from given type.
 * Note that the assumption of this class is that all fields are in the superclass hierarchy of the given type
 * @author helospark
 */
public class ApplicableFieldVisibilityFilter {
    private BodyDeclarationVisibleFromPredicate bodyDeclarationVisibleFromPredicate;

    public ApplicableFieldVisibilityFilter(BodyDeclarationVisibleFromPredicate bodyDeclarationVisibleFromPredicate) {
        this.bodyDeclarationVisibleFromPredicate = bodyDeclarationVisibleFromPredicate;
    }

    public List<BuilderField> filterSuperClassFieldsToVisibleFields(List<? extends BuilderField> toFilter, AbstractTypeDeclaration fromType) {
        return toFilter.stream()
                .filter(field -> field instanceof ClassFieldSetterBuilderField)
                .map(field -> (ClassFieldSetterBuilderField) field)
                .filter(field -> isFieldVisibleFrom(field, fromType))
                .collect(Collectors.toList());
    }

    private boolean isFieldVisibleFrom(ClassFieldSetterBuilderField field, AbstractTypeDeclaration fromType) {
        return bodyDeclarationVisibleFromPredicate.isDeclarationVisibleFrom(field.getFieldDeclaration(), fromType);
    }

}

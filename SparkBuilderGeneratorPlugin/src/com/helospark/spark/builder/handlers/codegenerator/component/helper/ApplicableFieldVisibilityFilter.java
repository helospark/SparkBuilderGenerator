package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.domain.JavaVisibilityScopeModifier;
import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

/**
 * Filters given list of fields to only those elements, which are visible from given type.
 * Note that the assumption of this class is that all fields are in the superclass hierarchy of the given type
 * @author helospark
 */
public class ApplicableFieldVisibilityFilter {

    public List<NamedVariableDeclarationField> filterSuperClassFieldsToVisibleFields(List<NamedVariableDeclarationField> toFilter, TypeDeclaration fromType) {
        return toFilter.stream()
                .filter(field -> isFieldVisibleFrom(field, fromType))
                .collect(Collectors.toList());
    }

    private boolean isFieldVisibleFrom(NamedVariableDeclarationField field, TypeDeclaration fromType) {
        JavaVisibilityScopeModifier fieldModifier = getFieldModifier(field.getFieldDeclaration());
        return fieldModifier.testIfVisibleFromSubclass(fromType, field.getFieldDeclaration());
    }

    private JavaVisibilityScopeModifier getFieldModifier(FieldDeclaration fieldDeclaration) {
        return ((List<IExtendedModifier>) fieldDeclaration.modifiers())
                .stream()
                .filter(modifier -> modifier instanceof Modifier)
                .map(modifier -> ((Modifier) modifier).getKeyword().toString())
                .filter(modifierKeyword -> JavaVisibilityScopeModifier.isValid(modifierKeyword))
                .map(modifierKeyword -> JavaVisibilityScopeModifier.convert(modifierKeyword))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElse(JavaVisibilityScopeModifier.DEFAULT_MODIFIER);
    }
}

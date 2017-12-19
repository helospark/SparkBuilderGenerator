package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.domain.JavaVisibilityScopeModifier;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.ClassFieldSetterBuilderField;

/**
 * Filters given list of fields to only those elements, which are visible from given type.
 * Note that the assumption of this class is that all fields are in the superclass hierarchy of the given type
 * @author helospark
 */
public class ApplicableFieldVisibilityFilter {

    public List<BuilderField> filterSuperClassFieldsToVisibleFields(List<? extends BuilderField> toFilter, TypeDeclaration fromType) {
        return toFilter.stream()
                .filter(field -> field instanceof ClassFieldSetterBuilderField)
                .map(field -> (ClassFieldSetterBuilderField) field)
                .filter(field -> isFieldVisibleFrom(field, fromType))
                .collect(Collectors.toList());
    }

    private boolean isFieldVisibleFrom(ClassFieldSetterBuilderField field, TypeDeclaration fromType) {
        return isAstNodeVisibleFrom(field.getFieldDeclaration(), fromType);
    }

    public boolean isAstNodeVisibleFrom(BodyDeclaration declaration, TypeDeclaration fromType) {
        JavaVisibilityScopeModifier fieldModifier = getFieldModifier(declaration);
        return fieldModifier.testIfVisibleFromSubclass(fromType, declaration);
    }

    private JavaVisibilityScopeModifier getFieldModifier(BodyDeclaration fieldDeclaration) {
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

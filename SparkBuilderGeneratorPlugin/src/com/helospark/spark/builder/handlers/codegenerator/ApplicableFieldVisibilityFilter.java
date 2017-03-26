package com.helospark.spark.builder.handlers.codegenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.IsPackageEqualsPredicate;
import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

/**
 * Filters given list of fields to only those elements, which are visible from given type.
 * Note that the assumption of this class is that all fields are in the superclass hierarchy of the given type
 * @author helospark
 */
public class ApplicableFieldVisibilityFilter {
    private enum JavaVisibilityScopeModifiers {
        PUBLIC_MODIFIER("public", (type, field) -> true),
        PRIVATE_MODIFIER("private", (type, field) -> false),
        PROTECTED_MODIFIER("protected", (type, field) -> true), // assumption of this class, that all fields are in the superclass of type
        DEFAULT_MODIFIER(null, (type, field) -> new IsPackageEqualsPredicate().test(type, field));

        String keyword;
        BiPredicate<TypeDeclaration, FieldDeclaration> isVisibleFromPredicate;

        private JavaVisibilityScopeModifiers(String keywordName, BiPredicate<TypeDeclaration, FieldDeclaration> isVisibleFromPredicate) {
            this.keyword = keywordName;
            this.isVisibleFromPredicate = isVisibleFromPredicate;
        }

        public String getKeyword() {
            return keyword;
        }

        public static boolean isValid(String modifierKeyword) {
            return convert(modifierKeyword)
                    .isPresent();
        }

        public static Optional<JavaVisibilityScopeModifiers> convert(String modifierKeyword) {
            return Arrays.stream(values())
                    .filter(keyword -> keyword.getKeyword() != null)
                    .filter(keyword -> keyword.getKeyword().equals(modifierKeyword))
                    .findFirst();
        }

        public boolean test(TypeDeclaration fromType, FieldDeclaration fieldDeclaration) {
            return isVisibleFromPredicate.test(fromType, fieldDeclaration);
        }
    };

    public List<NamedVariableDeclarationField> filterSuperClassFieldsToVisibleFields(List<NamedVariableDeclarationField> toFilter, TypeDeclaration fromType) {
        return toFilter.stream()
                .filter(field -> isFieldVisibleFrom(field, fromType))
                .collect(Collectors.toList());
    }

    private boolean isFieldVisibleFrom(NamedVariableDeclarationField field, TypeDeclaration fromType) {
        JavaVisibilityScopeModifiers fieldModifier = getFieldModifier(field.getFieldDeclaration());
        return fieldModifier.test(fromType, field.getFieldDeclaration());
    }

    private JavaVisibilityScopeModifiers getFieldModifier(FieldDeclaration fieldDeclaration) {
        return ((List<IExtendedModifier>) fieldDeclaration.modifiers())
                .stream()
                .filter(modifier -> modifier instanceof Modifier)
                .map(modifier -> ((Modifier) modifier).getKeyword().toString())
                .filter(modifierKeyword -> JavaVisibilityScopeModifiers.isValid(modifierKeyword))
                .map(modifierKeyword -> JavaVisibilityScopeModifiers.convert(modifierKeyword).get())
                .findFirst()
                .orElse(JavaVisibilityScopeModifiers.DEFAULT_MODIFIER);
    }
}

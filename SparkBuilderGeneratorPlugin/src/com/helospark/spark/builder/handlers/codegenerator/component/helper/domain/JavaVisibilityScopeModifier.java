package com.helospark.spark.builder.handlers.codegenerator.component.helper.domain;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiPredicate;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.IsPackageEqualsPredicate;

/**
 * Contains the Java modifiers visibility modifiers.
 * @author helospark
 */
public enum JavaVisibilityScopeModifier {
    PUBLIC_MODIFIER("public", (type, field) -> true),
    PRIVATE_MODIFIER("private", (type, field) -> false),
    PROTECTED_MODIFIER("protected", (type, field) -> true), // assumption of this class, that all fields are in the superclass of type
    DEFAULT_MODIFIER(null, (type, field) -> new IsPackageEqualsPredicate().test(type, field));

    String keyword;
    BiPredicate<TypeDeclaration, BodyDeclaration> isVisibleFromPredicate;

    private JavaVisibilityScopeModifier(String keywordName, BiPredicate<TypeDeclaration, BodyDeclaration> isVisibleFromPredicate) {
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

    public static Optional<JavaVisibilityScopeModifier> convert(String modifierKeyword) {
        return Arrays.stream(values())
                .filter(keyword -> keyword.getKeyword() != null)
                .filter(keyword -> keyword.getKeyword().equals(modifierKeyword))
                .findFirst();
    }

    public boolean testIfVisibleFromSubclass(TypeDeclaration subclass, BodyDeclaration field) {
        return isVisibleFromPredicate.test(subclass, field);
    }
};

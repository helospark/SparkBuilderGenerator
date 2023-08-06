package com.helospark.spark.builder.handlers.codegenerator.component.helper.domain;

import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;

/**
 * Predicate to check whether the given body declaration is visible from the given class.
 * @author helospark
 */
public class BodyDeclarationVisibleFromPredicate {

    public boolean isDeclarationVisibleFrom(BodyDeclaration declaration, AbstractTypeDeclaration fromType) {
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

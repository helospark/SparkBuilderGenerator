package com.helospark.spark.builder.handlers.codegenerator.component.remover.helper;

import java.util.List;
import java.util.function.Predicate;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.Modifier;

/**
 * Predicate that return whether the given predicate is true for at least one modifier.
 * @author helospark
 */
public class GenericModifierPredicate {

    public boolean test(BodyDeclaration method, Predicate<IExtendedModifier> modifiedPredicate) {
        return ((List<IExtendedModifier>) method.modifiers())
                .stream()
                .filter(modifier -> modifier instanceof Modifier)
                .filter(modifiedPredicate)
                .findFirst()
                .isPresent();
    }
}

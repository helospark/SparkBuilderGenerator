package com.helospark.spark.builder.handlers.codegenerator.component.remover.helper;

import java.util.function.Predicate;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;

public class IsPrivatePredicate implements Predicate<BodyDeclaration> {
    private GenericModifierPredicate genericModifierPredicate;

    public IsPrivatePredicate(GenericModifierPredicate genericModifierPredicate) {
        this.genericModifierPredicate = genericModifierPredicate;
    }

    @Override
    public boolean test(BodyDeclaration bodyDeclaration) {
        return genericModifierPredicate.test(bodyDeclaration, element -> ((Modifier) element).getKeyword() == ModifierKeyword.PRIVATE_KEYWORD);
    }

}

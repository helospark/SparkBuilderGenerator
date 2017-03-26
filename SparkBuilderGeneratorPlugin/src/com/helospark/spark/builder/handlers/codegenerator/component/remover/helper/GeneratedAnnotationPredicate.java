package com.helospark.spark.builder.handlers.codegenerator.component.remover.helper;

import static com.helospark.spark.builder.preferences.StaticPreferences.PLUGIN_GENERATED_ANNOTATION_NAME;

import java.util.List;
import java.util.function.Predicate;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

/**
 * Predicate to indicate the the given body declaration contains @Generated("SparkTools") annotation.
 * @author helospark
 */
public class GeneratedAnnotationPredicate implements Predicate<BodyDeclaration> {

    @Override
    public boolean test(BodyDeclaration bodyDeclaration) {
        return ((List<IExtendedModifier>) bodyDeclaration.modifiers())
                .stream()
                .filter(modifier -> modifier instanceof SingleMemberAnnotation)
                .filter(annotation -> ((SingleMemberAnnotation) annotation).getValue().equals(PLUGIN_GENERATED_ANNOTATION_NAME))
                .findFirst()
                .isPresent();
    }

}

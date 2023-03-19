package com.helospark.spark.builder.handlers.codegenerator.component.remover.helper;

import static com.helospark.spark.builder.preferences.StaticPreferences.PLUGIN_GENERATED_ANNOTATION_NAME;

import java.util.List;
import java.util.function.Predicate;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.StringLiteral;

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
                .map(modifier -> (SingleMemberAnnotation) modifier)
                .filter(modifier -> isGeneratedAnnotation(modifier))
                .filter(modifier -> modifier.getValue() instanceof StringLiteral)
                .filter(annotation -> ((StringLiteral) annotation.getValue()).getLiteralValue().equals(PLUGIN_GENERATED_ANNOTATION_NAME))
                .findFirst()
                .isPresent();
    }

    private boolean isGeneratedAnnotation(SingleMemberAnnotation modifier) {
        String fqn = modifier.getTypeName().getFullyQualifiedName();
        return fqn.equals("javax.annotation.Generated") || fqn.equals("jakarta.annotation.Generated") || fqn.equals("Generated");
    }

}

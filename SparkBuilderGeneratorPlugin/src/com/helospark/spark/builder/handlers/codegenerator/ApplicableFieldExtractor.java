package com.helospark.spark.builder.handlers.codegenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.VariableDeclarationToFieldNameConverter;
import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

/**
 * Extracts fields which can be added to the builder.
 *
 * @author helospark
 */
public class ApplicableFieldExtractor {
    private VariableDeclarationToFieldNameConverter variableDeclarationToFieldNameConverter;

    public ApplicableFieldExtractor(VariableDeclarationToFieldNameConverter variableDeclarationToFieldNameConverter) {
        this.variableDeclarationToFieldNameConverter = variableDeclarationToFieldNameConverter;
    }

    @SuppressWarnings("unchecked")
    public List<NamedVariableDeclarationField> filterApplicableFields(FieldDeclaration[] fields) {
        List<NamedVariableDeclarationField> namedVariableDeclarations = new ArrayList<>();
        for (FieldDeclaration field : fields) {
            List<VariableDeclarationFragment> fragments = field.fragments();
            namedVariableDeclarations.addAll(getFilteredDeclarations(field, fragments));
        }
        return namedVariableDeclarations;
    }

    private List<NamedVariableDeclarationField> getFilteredDeclarations(FieldDeclaration field, List<VariableDeclarationFragment> fragments) {
        return fragments.stream()
                .filter(variableFragment -> !isStatic(field))
                .map(variableFragment -> createNamedVariableDeclarations(variableFragment, field))
                .collect(Collectors.toList());
    }

    private NamedVariableDeclarationField createNamedVariableDeclarations(VariableDeclarationFragment variableDeclarationFragment, FieldDeclaration fieldDeclaration) {
        String fieldName = variableDeclarationToFieldNameConverter.convertToFieldName(variableDeclarationFragment);
        return new NamedVariableDeclarationField(fieldDeclaration, fieldName);
    }

    private boolean isStatic(FieldDeclaration field) {
        List<IExtendedModifier> fieldModifiers = field.modifiers();
        return fieldModifiers.stream()
                .filter(modifier -> modifier instanceof Modifier)
                .filter(modifer -> ((Modifier) modifer).getKeyword().equals(ModifierKeyword.STATIC_KEYWORD))
                .findFirst()
                .isPresent();
    }
}

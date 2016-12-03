package com.helospark.spark.builder.handlers.codegenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

/**
 * Extracts fields which can be added to the builder.
 * 
 * @author helospark
 */
public class ApplicableFieldExtractor {

    @SuppressWarnings("unchecked")
    public List<NamedVariableDeclarationField> filterApplicableFields(FieldDeclaration[] fields) {
        List<NamedVariableDeclarationField> namedVariableDeclarations = new ArrayList<>();
        for (FieldDeclaration field : fields) {
            Object o = field.fragments().get(0);
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
        String fieldName = variableDeclarationFragment.getName().toString();
        return new NamedVariableDeclarationField(fieldDeclaration, fieldName);
    }

    private boolean isStatic(FieldDeclaration field) {
        List<Modifier> fieldModifiers = field.modifiers();
        return fieldModifiers.stream()
                .filter(modifer -> modifer.getKeyword().equals(ModifierKeyword.STATIC_KEYWORD))
                .findFirst()
                .isPresent();
    }
}

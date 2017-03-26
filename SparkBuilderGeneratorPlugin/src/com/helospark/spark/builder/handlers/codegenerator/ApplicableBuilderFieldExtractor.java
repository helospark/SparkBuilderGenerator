package com.helospark.spark.builder.handlers.codegenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.FieldNameToBuilderFieldNameConverter;
import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

/**
 * Filters and converts given fields to {@link NamedVariableDeclarationField}.
 *
 * @author helospark
 */
public class ApplicableBuilderFieldExtractor {
    private FieldNameToBuilderFieldNameConverter fieldNameToBuilderFieldNameConverter;

    public ApplicableBuilderFieldExtractor(FieldNameToBuilderFieldNameConverter fieldNameToBuilderFieldNameConverter) {
        this.fieldNameToBuilderFieldNameConverter = fieldNameToBuilderFieldNameConverter;
    }

    @SuppressWarnings("unchecked")
    public List<NamedVariableDeclarationField> findBuilderFields(TypeDeclaration typeDeclaration) {
        FieldDeclaration[] fields = typeDeclaration.getFields();
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
        String originalFieldName = variableDeclarationFragment.getName().toString();
        String builderFieldName = fieldNameToBuilderFieldNameConverter.convertFieldName(originalFieldName);
        return NamedVariableDeclarationField.builder()
                .withFieldDeclaration(fieldDeclaration)
                .withOriginalFieldName(originalFieldName)
                .withBuilderFieldName(builderFieldName)
                .build();
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

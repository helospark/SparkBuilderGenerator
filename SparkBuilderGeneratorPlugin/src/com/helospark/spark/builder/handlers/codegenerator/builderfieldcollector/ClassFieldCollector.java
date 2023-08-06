package com.helospark.spark.builder.handlers.codegenerator.builderfieldcollector;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.INCLUDE_VISIBLE_FIELDS_FROM_SUPERCLASS;
import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.ApplicableFieldVisibilityFilter;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.FieldExtractor;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.FieldNameToBuilderFieldNameConverter;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TypeDeclarationFromSuperclassExtractor;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.ClassFieldSetterBuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.instancefieldaccess.DirectFieldAccessStrategy;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Collects the field parameters.
 * @author helospark
 */
public class ClassFieldCollector implements FieldCollectorChainItem {
    private FieldNameToBuilderFieldNameConverter fieldNameToBuilderFieldNameConverter;
    private PreferencesManager preferencesManager;
    private TypeDeclarationFromSuperclassExtractor typeDeclarationFromSuperclassExtractor;
    private ApplicableFieldVisibilityFilter applicableFieldVisibilityFilter;

    public ClassFieldCollector(FieldNameToBuilderFieldNameConverter fieldNameToBuilderFieldNameConverter, PreferencesManager preferencesManager,
            TypeDeclarationFromSuperclassExtractor typeDeclarationFromSuperclassExtractor, ApplicableFieldVisibilityFilter applicableFieldVisibilityFilter) {
        this.fieldNameToBuilderFieldNameConverter = fieldNameToBuilderFieldNameConverter;
        this.preferencesManager = preferencesManager;
        this.typeDeclarationFromSuperclassExtractor = typeDeclarationFromSuperclassExtractor;
        this.applicableFieldVisibilityFilter = applicableFieldVisibilityFilter;
    }

    @Override
    public List<? extends BuilderField> collect(AbstractTypeDeclaration typeDeclaration) {
        return findBuilderFieldsRecursively(typeDeclaration, typeDeclaration.getAST());
    }

    private List<? extends BuilderField> findBuilderFieldsRecursively(AbstractTypeDeclaration currentOwnerClass, AST originalAst) {
        List<BuilderField> builderFields = new ArrayList<>();

        if (preferencesManager.getPreferenceValue(INCLUDE_VISIBLE_FIELDS_FROM_SUPERCLASS)) {
            builderFields.addAll(getFieldsFromSuperclass(currentOwnerClass, originalAst));
        }

        FieldDeclaration[] fields = FieldExtractor.getFields(currentOwnerClass);
        for (FieldDeclaration field : fields) {
            List<VariableDeclarationFragment> fragments = field.fragments();
            builderFields.addAll(getFilteredDeclarations(field, fragments, originalAst));
        }
        return builderFields;
    }

    private List<BuilderField> getFieldsFromSuperclass(AbstractTypeDeclaration currentTypeDeclaration, AST originalAst) {
        return typeDeclarationFromSuperclassExtractor.extractTypeDeclarationFromSuperClass(currentTypeDeclaration)
                .map(parentTypeDeclaration -> findBuilderFieldsRecursively(parentTypeDeclaration, originalAst))
                .map(fields -> applicableFieldVisibilityFilter.filterSuperClassFieldsToVisibleFields(fields, currentTypeDeclaration))
                .orElse(emptyList());
    }

    private List<BuilderField> getFilteredDeclarations(FieldDeclaration field, List<VariableDeclarationFragment> fragments, AST originalAst) {
        return fragments.stream()
                .filter(variableFragment -> !isStatic(field))
                .map(variableFragment -> createNamedVariableDeclarations(variableFragment, field, originalAst))
                .collect(Collectors.toList());
    }

    private BuilderField createNamedVariableDeclarations(VariableDeclarationFragment variableDeclarationFragment, FieldDeclaration fieldDeclaration, AST originalAst) {
        String originalFieldName = variableDeclarationFragment.getName().toString();
        String builderFieldName = fieldNameToBuilderFieldNameConverter.convertFieldName(originalFieldName);
        Expression defaultValue = Optional.ofNullable(variableDeclarationFragment.getInitializer())
                .map(initializer -> (Expression) ASTNode.copySubtree(originalAst, initializer))
                .orElse(null);
        return ClassFieldSetterBuilderField.builder()
                .withFieldType(fieldDeclaration.getType())
                .withFieldDeclaration(fieldDeclaration)
                .withOriginalFieldName(originalFieldName)
                .withBuilderFieldName(builderFieldName)
                .withDefaultValue(defaultValue)
                .withOriginalFieldAccessStrategy(Optional.of(new DirectFieldAccessStrategy(originalFieldName))) // Only visible fields are included, therefore they can always be accessed
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

package com.helospark.spark.builder.handlers.codegenerator.builderfieldcollector;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.INCLUDE_PARAMETERS_FROM_SUPERCLASS_CONSTRUCTOR;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.PREFER_TO_USE_EMPTY_SUPERCLASS_CONSTRUCTOR;
import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.FieldNameToBuilderFieldNameConverter;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TypeDeclarationFromSuperclassExtractor;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.domain.BodyDeclarationVisibleFromPredicate;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.ConstructorParameterSetterBuilderField;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Collects the parameters in the superclass constructor if applicable, otherwise returns empty list.
 * @author helospark
 */
public class SuperConstructorParameterCollector {
    private FieldNameToBuilderFieldNameConverter fieldNameToBuilderFieldNameConverter;
    private PreferencesManager preferencesManager;
    private TypeDeclarationFromSuperclassExtractor typeDeclarationFromSuperclassExtractor;
    private BodyDeclarationVisibleFromPredicate bodyDeclarationVisibleFromPredicate;

    public SuperConstructorParameterCollector(FieldNameToBuilderFieldNameConverter fieldNameToBuilderFieldNameConverter, PreferencesManager preferencesManager,
            TypeDeclarationFromSuperclassExtractor typeDeclarationFromSuperclassExtractor, BodyDeclarationVisibleFromPredicate bodyDeclarationVisibleFromPredicate) {
        this.fieldNameToBuilderFieldNameConverter = fieldNameToBuilderFieldNameConverter;
        this.preferencesManager = preferencesManager;
        this.typeDeclarationFromSuperclassExtractor = typeDeclarationFromSuperclassExtractor;
        this.bodyDeclarationVisibleFromPredicate = bodyDeclarationVisibleFromPredicate;
    }

    public List<? extends BuilderField> findSuperclassConstructorDeclaration(TypeDeclaration typeDeclaration) {
        if (preferencesManager.getPreferenceValue(INCLUDE_PARAMETERS_FROM_SUPERCLASS_CONSTRUCTOR)) {
            return typeDeclarationFromSuperclassExtractor.extractTypeDeclarationFromSuperClass(typeDeclaration)
                    .map(parentTypeDeclaration -> findConstructorToUse(typeDeclaration, parentTypeDeclaration))
                    .map(constructor -> extractArguments(constructor))
                    .orElse(emptyList());
        } else {
            return Collections.emptyList();
        }
    }

    private List<ConstructorParameterSetterBuilderField> extractArguments(MethodDeclaration constructor) {
        List<ConstructorParameterSetterBuilderField> result = new ArrayList<>();
        List<SingleVariableDeclaration> parameters = constructor.parameters();
        for (int i = 0; i < parameters.size(); ++i) {
            result.add(createConstructorParameterSetterBuilderField(parameters.get(i), i));
        }
        return result;
    }

    private ConstructorParameterSetterBuilderField createConstructorParameterSetterBuilderField(SingleVariableDeclaration element, int index) {
        String originalFieldName = element.getName().toString();
        String builderFieldName = fieldNameToBuilderFieldNameConverter.convertFieldName(originalFieldName);

        return ConstructorParameterSetterBuilderField.builder()
                .withFieldType(element.getType())
                .withOriginalFieldName(originalFieldName)
                .withBuilderFieldName(builderFieldName)
                .withIndex(index)
                .build();
    }

    private MethodDeclaration findConstructorToUse(TypeDeclaration currentType, TypeDeclaration parentTypeDeclaration) {
        List<MethodDeclaration> applicableConstructors = Arrays.stream(parentTypeDeclaration.getMethods())
                .filter(method -> method.isConstructor())
                .filter(constructor -> bodyDeclarationVisibleFromPredicate.isDeclarationVisibleFrom(constructor, currentType))
                .collect(Collectors.toList());

        if (preferencesManager.getPreferenceValue(PREFER_TO_USE_EMPTY_SUPERCLASS_CONSTRUCTOR)) {
            return findEmptyConstructor(applicableConstructors)
                    .orElse(findLargestConstructor(applicableConstructors));
        } else {
            return findLargestConstructor(applicableConstructors);
        }
    }

    private Optional<MethodDeclaration> findEmptyConstructor(List<MethodDeclaration> applicableConstructors) {
        return applicableConstructors.stream()
                .filter(constructor -> constructor.parameters().size() == 0)
                .findFirst();
    }

    // @Nullable
    private MethodDeclaration findLargestConstructor(List<MethodDeclaration> applicableConstructors) {
        MethodDeclaration result = null;
        int maxConstructorParameterCount = 0;
        for (MethodDeclaration method : applicableConstructors) {
            if (method.parameters().size() > maxConstructorParameterCount) {
                result = method;
                maxConstructorParameterCount = method.parameters().size();
            }
        }
        return result;
    }

}

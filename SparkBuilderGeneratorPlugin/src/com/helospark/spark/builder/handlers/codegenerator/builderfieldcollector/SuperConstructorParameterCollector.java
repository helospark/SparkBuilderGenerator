package com.helospark.spark.builder.handlers.codegenerator.builderfieldcollector;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.INCLUDE_PARAMETERS_FROM_SUPERCLASS_CONSTRUCTOR;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.PREFER_TO_USE_EMPTY_SUPERCLASS_CONSTRUCTOR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.FieldNameToBuilderFieldNameConverter;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.MethodExtractor;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TypeDeclarationFromSuperclassExtractor;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.domain.BodyDeclarationFinderUtil;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.domain.BodyDeclarationVisibleFromPredicate;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.ConstructorParameterSetterBuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.instancefieldaccess.DirectFieldAccessStrategy;
import com.helospark.spark.builder.handlers.codegenerator.domain.instancefieldaccess.GetterFieldAccessStrategy;
import com.helospark.spark.builder.handlers.codegenerator.domain.instancefieldaccess.InstanceFieldAccessStrategy;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Collects the parameters in the superclass constructor if applicable, otherwise returns empty list.
 * @author helospark
 */
public class SuperConstructorParameterCollector implements FieldCollectorChainItem {
    private FieldNameToBuilderFieldNameConverter fieldNameToBuilderFieldNameConverter;
    private PreferencesManager preferencesManager;
    private TypeDeclarationFromSuperclassExtractor typeDeclarationFromSuperclassExtractor;
    private BodyDeclarationVisibleFromPredicate bodyDeclarationVisibleFromPredicate;
    private BodyDeclarationFinderUtil bodyDeclarationFinderUtil;

    public SuperConstructorParameterCollector(FieldNameToBuilderFieldNameConverter fieldNameToBuilderFieldNameConverter, PreferencesManager preferencesManager,
            TypeDeclarationFromSuperclassExtractor typeDeclarationFromSuperclassExtractor, BodyDeclarationVisibleFromPredicate bodyDeclarationVisibleFromPredicate,
            BodyDeclarationFinderUtil bodyDeclarationFinderUtil) {
        this.fieldNameToBuilderFieldNameConverter = fieldNameToBuilderFieldNameConverter;
        this.preferencesManager = preferencesManager;
        this.typeDeclarationFromSuperclassExtractor = typeDeclarationFromSuperclassExtractor;
        this.bodyDeclarationVisibleFromPredicate = bodyDeclarationVisibleFromPredicate;
        this.bodyDeclarationFinderUtil = bodyDeclarationFinderUtil;
    }

    @Override
    public List<? extends BuilderField> collect(AbstractTypeDeclaration typeDeclaration) {
        if (preferencesManager.getPreferenceValue(INCLUDE_PARAMETERS_FROM_SUPERCLASS_CONSTRUCTOR)) {
            Optional<AbstractTypeDeclaration> parentTypeDeclaration2 = typeDeclarationFromSuperclassExtractor
                    .extractTypeDeclarationFromSuperClass(typeDeclaration);
            if (parentTypeDeclaration2.isPresent()) {
                AbstractTypeDeclaration parent = parentTypeDeclaration2.get();
                return Optional.ofNullable(findConstructorToUse(typeDeclaration, parent))
                        .map(constructorToUse -> extractArguments(constructorToUse, parent, typeDeclaration))
                        .orElse(Collections.emptyList());
            } else {
                return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }
    }

    private List<ConstructorParameterSetterBuilderField> extractArguments(MethodDeclaration constructor, AbstractTypeDeclaration parent,
            AbstractTypeDeclaration builderOwnerTypeDeclaration) {
        List<ConstructorParameterSetterBuilderField> result = new ArrayList<>();
        List<SingleVariableDeclaration> parameters = constructor.parameters();
        for (int i = 0; i < parameters.size(); ++i) {
            result.add(createConstructorParameterSetterBuilderField(parameters.get(i), i, parent, builderOwnerTypeDeclaration));
        }
        return result;
    }

    private ConstructorParameterSetterBuilderField createConstructorParameterSetterBuilderField(SingleVariableDeclaration element, int index, AbstractTypeDeclaration parent,
            AbstractTypeDeclaration builderOwnerTypeDeclaration) {
        String originalFieldName = element.getName().toString();
        String builderFieldName = fieldNameToBuilderFieldNameConverter.convertFieldName(originalFieldName);
        Type fieldType = element.getType();

        Optional<FieldDeclaration> field = bodyDeclarationFinderUtil.findFieldWithNameAndType(parent, originalFieldName, fieldType);

        Optional<InstanceFieldAccessStrategy> originalFieldAccessStrategy = Optional.empty();
        if (field.isPresent()) {
            if (bodyDeclarationVisibleFromPredicate.isDeclarationVisibleFrom(field.get(), builderOwnerTypeDeclaration)) {
                originalFieldAccessStrategy = Optional.of(new DirectFieldAccessStrategy(originalFieldName));
            }
        }

        Optional<MethodDeclaration> getterMethodDeclaration = bodyDeclarationFinderUtil.findGetterForFieldWithNameAndType(parent, originalFieldName, fieldType);

        if (getterMethodDeclaration.isPresent() && !originalFieldAccessStrategy.isPresent()) {
            if (bodyDeclarationVisibleFromPredicate.isDeclarationVisibleFrom(getterMethodDeclaration.get(), builderOwnerTypeDeclaration)) {
                originalFieldAccessStrategy = Optional.of(new GetterFieldAccessStrategy(getterMethodDeclaration.get().getName().toString()));
            }
        }

        return ConstructorParameterSetterBuilderField.builder()
                .withFieldType(fieldType)
                .withOriginalFieldName(originalFieldName)
                .withBuilderFieldName(builderFieldName)
                .withIndex(index)
                .withOriginalFieldAccessStrategy(originalFieldAccessStrategy)
                .build();
    }

    private MethodDeclaration findConstructorToUse(AbstractTypeDeclaration currentType, AbstractTypeDeclaration parentTypeDeclaration) {
        List<MethodDeclaration> applicableConstructors = Arrays.stream(MethodExtractor.getMethods(parentTypeDeclaration))
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

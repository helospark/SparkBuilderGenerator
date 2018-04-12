package com.helospark.spark.builder.handlers.codegenerator.builderfieldcollector;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.INCLUDE_SETTER_FIELDS_FROM_SUPERCLASS;
import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.CamelCaseConverter;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TypeDeclarationFromSuperclassExtractor;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.SuperSetterBasedBuilderField;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Collects fields with setters from superclass. 
 * @author helospark
 */
public class SuperClassSetterFieldCollector implements FieldCollectorChainItem {
    private static final String SETTER_METHOD_PREFIX = "set";

    private PreferencesManager preferencesManager;
    private TypeDeclarationFromSuperclassExtractor typeDeclarationFromSuperclassExtractor;
    private CamelCaseConverter camelCaseConverter;

    public SuperClassSetterFieldCollector(PreferencesManager preferencesManager, TypeDeclarationFromSuperclassExtractor typeDeclarationFromSuperclassExtractor,
            CamelCaseConverter camelCaseConverter) {
        this.preferencesManager = preferencesManager;
        this.typeDeclarationFromSuperclassExtractor = typeDeclarationFromSuperclassExtractor;
        this.camelCaseConverter = camelCaseConverter;
    }

    @Override
    public List<? extends BuilderField> collect(TypeDeclaration typeDeclaration) {
        if (preferencesManager.getPreferenceValue(INCLUDE_SETTER_FIELDS_FROM_SUPERCLASS)) {
            return collectFieldsRecursively(typeDeclaration);
        } else {
            return emptyList();
        }
    }

    private List<SuperSetterBasedBuilderField> collectFieldsRecursively(TypeDeclaration typeDeclaration) {
        List<SuperSetterBasedBuilderField> result = new ArrayList<>();
        Optional<TypeDeclaration> superClassType = typeDeclarationFromSuperclassExtractor.extractTypeDeclarationFromSuperClass(typeDeclaration);

        superClassType.ifPresent(type -> {
            result.addAll(findParametersWithSettersInType(type));
            result.addAll(collectFieldsRecursively(type));
        });

        return result;
    }

    private List<SuperSetterBasedBuilderField> findParametersWithSettersInType(TypeDeclaration parentTypeDeclaration) {
        return ((List<BodyDeclaration>) parentTypeDeclaration.bodyDeclarations())
                .stream()
                .filter(declaration -> isMethod(declaration))
                .map(declaration -> (MethodDeclaration) declaration)
                .filter(method -> isSetter(method))
                .map(method -> createBuilderField(method))
                .collect(Collectors.toList());
    }

    private boolean isMethod(BodyDeclaration declaration) {
        return declaration instanceof MethodDeclaration;
    }

    private boolean isSetter(MethodDeclaration method) {
        String methodName = method.getName().toString();
        return method.parameters().size() == 1 && methodName.startsWith(SETTER_METHOD_PREFIX);
    }

    private SuperSetterBasedBuilderField createBuilderField(MethodDeclaration method) {
        String methodName = method.getName().toString();
        String upperCamelCaseFieldName = methodName.replaceFirst(SETTER_METHOD_PREFIX, "");
        String fieldName = camelCaseConverter.toLowerCamelCase(upperCamelCaseFieldName);

        SingleVariableDeclaration parameter = (SingleVariableDeclaration) method.parameters().get(0);

        return SuperSetterBasedBuilderField.builder()
                .withBuilderFieldName(fieldName)
                .withOriginalFieldName(fieldName)
                .withFieldType(parameter.getType())
                .withSetterName(methodName)
                .build();
    }
}

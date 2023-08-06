package com.helospark.spark.builder.handlers.codegenerator.builderfieldcollector;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.INCLUDE_SETTER_FIELDS_FROM_SUPERCLASS;
import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.CamelCaseConverter;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TypeDeclarationFromSuperclassExtractor;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.domain.BodyDeclarationFinderUtil;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.SuperSetterBasedBuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.instancefieldaccess.GetterFieldAccessStrategy;
import com.helospark.spark.builder.handlers.codegenerator.domain.instancefieldaccess.InstanceFieldAccessStrategy;
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
    private BodyDeclarationFinderUtil bodyDeclarationFinderUtil;

    public SuperClassSetterFieldCollector(PreferencesManager preferencesManager, TypeDeclarationFromSuperclassExtractor typeDeclarationFromSuperclassExtractor,
            CamelCaseConverter camelCaseConverter, BodyDeclarationFinderUtil bodyDeclarationFinderUtil) {
        this.preferencesManager = preferencesManager;
        this.typeDeclarationFromSuperclassExtractor = typeDeclarationFromSuperclassExtractor;
        this.camelCaseConverter = camelCaseConverter;
        this.bodyDeclarationFinderUtil = bodyDeclarationFinderUtil;
    }

    @Override
    public List<? extends BuilderField> collect(AbstractTypeDeclaration typeDeclaration) {
        if (preferencesManager.getPreferenceValue(INCLUDE_SETTER_FIELDS_FROM_SUPERCLASS)) {
            List<SuperSetterBasedBuilderField> foundFields = collectFieldsRecursively(typeDeclaration, typeDeclaration);
            return deduplicateByName(foundFields);
        } else {
            return emptyList();
        }
    }

    private List<SuperSetterBasedBuilderField> collectFieldsRecursively(AbstractTypeDeclaration currentTypeDeclaration,
            AbstractTypeDeclaration builderOwnerTypeDeclaration) {
        List<SuperSetterBasedBuilderField> result = new ArrayList<>();
        Optional<AbstractTypeDeclaration> superClassType = typeDeclarationFromSuperclassExtractor.extractTypeDeclarationFromSuperClass(currentTypeDeclaration);

        superClassType.ifPresent(superType -> {
            result.addAll(findParametersWithSettersInType(superType, builderOwnerTypeDeclaration));
            result.addAll(collectFieldsRecursively(superType, builderOwnerTypeDeclaration));
        });

        return result;
    }

    private List<SuperSetterBasedBuilderField> findParametersWithSettersInType(AbstractTypeDeclaration parentTypeDeclaration,
            AbstractTypeDeclaration builderOwnerTypeDeclaration) {
        return ((List<BodyDeclaration>) parentTypeDeclaration.bodyDeclarations())
                .stream()
                .filter(declaration -> isMethod(declaration))
                .map(declaration -> (MethodDeclaration) declaration)
                .filter(method -> isSetter(method))
                .map(method -> createBuilderField(method, parentTypeDeclaration, builderOwnerTypeDeclaration))
                .collect(Collectors.toList());
    }

    private boolean isMethod(BodyDeclaration declaration) {
        return declaration instanceof MethodDeclaration;
    }

    private boolean isSetter(MethodDeclaration method) {
        String methodName = method.getName().toString();
        return method.parameters().size() == 1 && methodName.startsWith(SETTER_METHOD_PREFIX);
    }

    private SuperSetterBasedBuilderField createBuilderField(MethodDeclaration method, AbstractTypeDeclaration parentTypeDeclaration,
            AbstractTypeDeclaration builderOwnerTypeDeclaration) {
        String methodName = method.getName().toString();
        String upperCamelCaseFieldName = methodName.replaceFirst(SETTER_METHOD_PREFIX, "");
        String fieldName = camelCaseConverter.toLowerCamelCase(upperCamelCaseFieldName);

        SingleVariableDeclaration parameter = (SingleVariableDeclaration) method.parameters().get(0);
        Type fieldType = parameter.getType();

        Optional<MethodDeclaration> getterMethodDeclaration = bodyDeclarationFinderUtil.findGetterForFieldWithNameAndType(parentTypeDeclaration, fieldName, fieldType);

        Optional<InstanceFieldAccessStrategy> originalFieldAccessStrategy = Optional.empty();
        if (getterMethodDeclaration.isPresent()) {
            String getterName = getterMethodDeclaration.get().getName().toString();
            originalFieldAccessStrategy = Optional.of(new GetterFieldAccessStrategy(getterName));
        } // it cannot be field access, because than we would include the whole field in the ClassFieldCollector instead

        return SuperSetterBasedBuilderField.builder()
                .withBuilderFieldName(fieldName)
                .withOriginalFieldName(fieldName)
                .withFieldType(fieldType)
                .withSetterName(methodName)
                .withOriginalFieldAccessStrategy(originalFieldAccessStrategy)
                .build();
    }

    // deduplication is required for overridden setters
    private List<SuperSetterBasedBuilderField> deduplicateByName(List<SuperSetterBasedBuilderField> fields) {
        List<SuperSetterBasedBuilderField> result = new ArrayList<>();
        for (SuperSetterBasedBuilderField field : fields) {
            if (!alreadyContainsField(result, field)) {
                result.add(field);
            }
        }
        return result;
    }

    private boolean alreadyContainsField(List<SuperSetterBasedBuilderField> result, SuperSetterBasedBuilderField field) {
        return result.stream()
                .filter(element -> element.getBuilderFieldName().equals(field.getBuilderFieldName()))
                .findFirst()
                .isPresent();
    }
}

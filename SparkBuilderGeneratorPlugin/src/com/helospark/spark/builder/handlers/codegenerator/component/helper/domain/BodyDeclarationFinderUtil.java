package com.helospark.spark.builder.handlers.codegenerator.component.helper.domain;

import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.CamelCaseConverter;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.FieldExtractor;

public class BodyDeclarationFinderUtil {
    private CamelCaseConverter camelCaseConverter;

    public BodyDeclarationFinderUtil(CamelCaseConverter camelCaseConverter) {
        this.camelCaseConverter = camelCaseConverter;
    }

    public Optional<MethodDeclaration> findGetterForFieldWithNameAndType(AbstractTypeDeclaration parentabstractTypeDeclaration, String fieldName, Type fieldType) {
        String getterName = "get" + camelCaseConverter.toUpperCamelCase(fieldName);
        return findGetterWithNameAndReturnType(parentabstractTypeDeclaration, getterName, fieldType);
    }

    public Optional<MethodDeclaration> findGetterWithNameAndReturnType(AbstractTypeDeclaration parentAbstractTypeDeclaration, String getterName, Type fieldType) {
        return ((List<BodyDeclaration>) parentAbstractTypeDeclaration.bodyDeclarations())
                .stream()
                .filter(declaration -> isMethod(declaration))
                .map(declaration -> (MethodDeclaration) declaration)
                .filter(method -> isGetter(method, getterName, fieldType))
                .findFirst();
    }

    private boolean isGetter(MethodDeclaration method, String getterName, Type fieldType) {
        boolean nameMatch = method.getName().toString().equals(getterName);
        boolean returnTypeMatch = method.getReturnType2() != null && method.getReturnType2().toString().equals(fieldType.toString());
        boolean parametersMatch = method.parameters().size() == 0;
        return nameMatch && returnTypeMatch && parametersMatch;
    }

    private boolean isMethod(BodyDeclaration declaration) {
        return declaration instanceof MethodDeclaration;
    }

    public Optional<FieldDeclaration> findFieldWithNameAndType(AbstractTypeDeclaration parent, String originalFieldName, Type fieldType) {
        FieldDeclaration[] fields = FieldExtractor.getFields(parent);
        for (FieldDeclaration field : fields) {
            List<VariableDeclarationFragment> fragments = field.fragments();

            boolean hasName = fragments.stream().filter(a -> a.getName().toString().equals(originalFieldName)).findAny().map(a -> true).orElse(false);
            boolean hasType = field.getType().toString().equals(fieldType.toString());

            if (hasName && hasType) {
                return Optional.of(field);
            }
        }
        return Optional.empty();
    }

}

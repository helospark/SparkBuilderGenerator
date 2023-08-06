package com.helospark.spark.builder.handlers.codegenerator.builderfieldcollector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.FieldNameToBuilderFieldNameConverter;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.IsRecordTypePredicate;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.RecordDeclarationWrapper;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.ThisConstructorParameterSetterBuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.instancefieldaccess.DirectFieldAccessStrategy;

/**
 * Collects the Java record fields.
 * @author helospark
 */
public class RecordFieldCollector implements FieldCollectorChainItem {
    private FieldNameToBuilderFieldNameConverter fieldNameToBuilderFieldNameConverter;

    public RecordFieldCollector(FieldNameToBuilderFieldNameConverter fieldNameToBuilderFieldNameConverter) {
        this.fieldNameToBuilderFieldNameConverter = fieldNameToBuilderFieldNameConverter;
    }

    @Override
    public List<? extends BuilderField> collect(AbstractTypeDeclaration typeDeclaration) {
        List<BuilderField> result = new ArrayList<>();
        if (IsRecordTypePredicate.isRecordDeclaration(typeDeclaration)) {
            List<SingleVariableDeclaration> recordComponents = RecordDeclarationWrapper.of(typeDeclaration).recordComponents();
            int i = 0;
            for (SingleVariableDeclaration declaration : recordComponents) {
                String originalFieldName = declaration.getName().toString();
                String builderFieldName = fieldNameToBuilderFieldNameConverter.convertFieldName(originalFieldName);
                ThisConstructorParameterSetterBuilderField builderField = ThisConstructorParameterSetterBuilderField.builder()
                        .withBuilderFieldName(builderFieldName)
                        .withFieldType(declaration.getType())
                        .withIndex(i)
                        .withOriginalFieldName(builderFieldName)
                        .withOriginalFieldAccessStrategy(Optional.of(new DirectFieldAccessStrategy(originalFieldName)))
                        .build();

                result.add(builderField);

                ++i;
            }
        }
        return result;
    }

}

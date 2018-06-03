package com.helospark.spark.builder.handlers.codegenerator.converter;

import java.util.List;
import java.util.stream.Collectors;

import com.helospark.spark.builder.dialogs.domain.RegularBuilderDialogData;
import com.helospark.spark.builder.dialogs.domain.RegularBuilderFieldIncludeFieldIncludeDomain;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.RegularBuilderUserPreference;

/**
 * Converts {@link RegularBuilderUserPreference} to {@link RegularBuilderDialogData}.
 * @author helospark
 */
public class RegularBuilderDialogDataConverter {

    public RegularBuilderDialogData convertInput(RegularBuilderUserPreference regularBuilderUserPreference) {
        return RegularBuilderDialogData.builder()
                .withShouldCreateCopyMethod(regularBuilderUserPreference.isGenerateCopyMethod())
                .withAddJacksonDeserializeAnnotation(regularBuilderUserPreference.isAddJacksonDeserializer())
                .withRegularBuilderFieldIncludeFieldIncludeDomains(convertFields(regularBuilderUserPreference.getBuilderFields()))
                .withCreateDefaultConstructor(regularBuilderUserPreference.isCreateDefaultConstructor())
                .build();
    }

    private List<RegularBuilderFieldIncludeFieldIncludeDomain> convertFields(List<BuilderField> builderFields) {
        return builderFields
                .stream()
                .map(field -> convert(field))
                .collect(Collectors.toList());
    }

    private RegularBuilderFieldIncludeFieldIncludeDomain convert(BuilderField namedField) {
        return new RegularBuilderFieldIncludeFieldIncludeDomain(true, namedField.getBuilderFieldName());
    }

}

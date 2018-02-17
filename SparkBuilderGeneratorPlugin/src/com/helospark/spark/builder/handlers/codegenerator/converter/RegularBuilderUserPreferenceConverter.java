package com.helospark.spark.builder.handlers.codegenerator.converter;

import java.util.ArrayList;
import java.util.List;

import com.helospark.spark.builder.dialogs.domain.RegularBuilderDialogData;
import com.helospark.spark.builder.dialogs.domain.RegularBuilderFieldIncludeFieldIncludeDomain;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.RegularBuilderUserPreference;

/**
 * Creates the result of the dialog as user preferences.
 * @author helospark
 */
public class RegularBuilderUserPreferenceConverter {

    public RegularBuilderUserPreference convertOutput(List<BuilderField> builderFields, RegularBuilderDialogData result) {
        return RegularBuilderUserPreference.builder()
                .withBuilderFields(filterFieldsBasedOnDialogOutput(builderFields, result.getRegularBuilderFieldIncludeFieldIncludeDomains()))
                .withGenerateCopyMethod(result.isShouldCreateCopyMethod())
                .build();
    }

    private List<BuilderField> filterFieldsBasedOnDialogOutput(List<BuilderField> builderFields,
            List<RegularBuilderFieldIncludeFieldIncludeDomain> dialogOutput) {
        if (dialogOutput.size() != builderFields.size()) {
            throw new IllegalStateException("Dialog should not change the number of elements in the list");
        }
        List<BuilderField> result = new ArrayList<>();
        for (int i = 0; i < dialogOutput.size(); ++i) {
            if (dialogOutput.get(i).isIncludeField()) {
                result.add(builderFields.get(i));
            }
        }
        return result;
    }

}

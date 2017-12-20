package com.helospark.spark.builder.handlers.codegenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.helospark.spark.builder.dialogs.domain.RegularBuilderFieldIncludeFieldIncludeDomain;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;

/**
 * Filters the fields to include in the builder.
 * @author helospark
 */
public class RegularBuilderFieldFilter {
    private RegularBuilderFieldFilterDialogOpener regularBuilderFieldFilterDialogOpener;

    public RegularBuilderFieldFilter(RegularBuilderFieldFilterDialogOpener regularBuilderFieldFilterDialogOpener) {
        this.regularBuilderFieldFilterDialogOpener = regularBuilderFieldFilterDialogOpener;
    }

    public Optional<List<BuilderField>> filterFields(List<BuilderField> builderFields) {
        List<RegularBuilderFieldIncludeFieldIncludeDomain> dialogInput = convert(builderFields);
        Optional<List<RegularBuilderFieldIncludeFieldIncludeDomain>> dialogOutput = regularBuilderFieldFilterDialogOpener.open(dialogInput);
        return dialogOutput
                .map(result -> filterFieldsBasedOnDialogOutput(builderFields, result));
    }

    private List<RegularBuilderFieldIncludeFieldIncludeDomain> convert(List<BuilderField> builderFields) {
        return builderFields.stream()
                .map(field -> convert(field))
                .collect(Collectors.toList());
    }

    private RegularBuilderFieldIncludeFieldIncludeDomain convert(BuilderField namedField) {
        return new RegularBuilderFieldIncludeFieldIncludeDomain(true, namedField.getBuilderFieldName());
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

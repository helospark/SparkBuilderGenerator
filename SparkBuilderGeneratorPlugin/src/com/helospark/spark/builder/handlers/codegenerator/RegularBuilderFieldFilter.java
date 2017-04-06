package com.helospark.spark.builder.handlers.codegenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.helospark.spark.builder.dialogs.domain.RegularBuilderFieldIncludeFieldIncludeDomain;
import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

public class RegularBuilderFieldFilter {
    private RegularBuilderFieldFilterDialogOpener regularBuilderFieldFilterDialogOpener;

    public RegularBuilderFieldFilter(RegularBuilderFieldFilterDialogOpener regularBuilderFieldFilterDialogOpener) {
        this.regularBuilderFieldFilterDialogOpener = regularBuilderFieldFilterDialogOpener;
    }

    public Optional<List<NamedVariableDeclarationField>> filterFields(List<NamedVariableDeclarationField> namedVariableDeclarations) {
        List<RegularBuilderFieldIncludeFieldIncludeDomain> dialogInput = convert(namedVariableDeclarations);
        Optional<List<RegularBuilderFieldIncludeFieldIncludeDomain>> dialogOutput = regularBuilderFieldFilterDialogOpener.open(dialogInput);
        return dialogOutput
                .map(result -> filterFieldsBasedOnDialogOutput(namedVariableDeclarations, result));
    }

    private List<RegularBuilderFieldIncludeFieldIncludeDomain> convert(List<NamedVariableDeclarationField> namedVariableDeclarations) {
        return namedVariableDeclarations.stream()
                .map(field -> convert(field))
                .collect(Collectors.toList());
    }

    private RegularBuilderFieldIncludeFieldIncludeDomain convert(NamedVariableDeclarationField namedField) {
        return new RegularBuilderFieldIncludeFieldIncludeDomain(true, namedField.getBuilderFieldName());
    }

    private List<NamedVariableDeclarationField> filterFieldsBasedOnDialogOutput(List<NamedVariableDeclarationField> namedVariableDeclarations,
            List<RegularBuilderFieldIncludeFieldIncludeDomain> dialogOutput) {
        if (dialogOutput.size() != namedVariableDeclarations.size()) {
            throw new IllegalStateException("Dialog should not change the number of elements in the list");
        }
        List<NamedVariableDeclarationField> result = new ArrayList<>();
        for (int i = 0; i < dialogOutput.size(); ++i) {
            if (dialogOutput.get(i).isIncludeField()) {
                result.add(namedVariableDeclarations.get(i));
            }
        }
        return result;
    }

}

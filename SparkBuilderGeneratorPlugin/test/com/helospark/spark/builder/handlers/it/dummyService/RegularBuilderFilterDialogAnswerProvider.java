package com.helospark.spark.builder.handlers.it.dummyService;

import static java.util.Optional.of;

import java.util.List;

import org.mockito.invocation.InvocationOnMock;

import com.helospark.spark.builder.dialogs.domain.RegularBuilderFieldIncludeFieldIncludeDomain;

public class RegularBuilderFilterDialogAnswerProvider {
    private List<Integer> fieldsToInclude;

    public RegularBuilderFilterDialogAnswerProvider(List<Integer> fieldsToInclude) {
        this.fieldsToInclude = fieldsToInclude;
    }

    public Object provideAnswer(InvocationOnMock inv) {
        List<RegularBuilderFieldIncludeFieldIncludeDomain> result = (List<RegularBuilderFieldIncludeFieldIncludeDomain>) inv.getArguments()[0];
        for (int i = 0; i < result.size(); ++i) {
            result.get(i).setIncludeField(false);
        }
        for (int i = 0; i < fieldsToInclude.size(); ++i) {
            result.get(i).setIncludeField(true);
        }
        return of(result);
    }

}

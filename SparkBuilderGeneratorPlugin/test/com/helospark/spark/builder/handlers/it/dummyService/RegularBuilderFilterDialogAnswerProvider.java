package com.helospark.spark.builder.handlers.it.dummyService;

import static java.util.Optional.of;

import java.util.List;

import org.mockito.invocation.InvocationOnMock;

import com.helospark.spark.builder.dialogs.domain.RegularBuilderDialogData;
import com.helospark.spark.builder.dialogs.domain.RegularBuilderFieldIncludeFieldIncludeDomain;

public class RegularBuilderFilterDialogAnswerProvider {
    private List<Integer> fieldsToInclude;
    private boolean generateCopyMethod;

    public RegularBuilderFilterDialogAnswerProvider(List<Integer> fieldsToInclude, boolean generateCopyMethod) {
        this.fieldsToInclude = fieldsToInclude;
        this.generateCopyMethod = generateCopyMethod;
    }

    public Object provideAnswer(InvocationOnMock inv) {
        List<RegularBuilderFieldIncludeFieldIncludeDomain> result = ((RegularBuilderDialogData) inv.getArguments()[0]).getRegularBuilderFieldIncludeFieldIncludeDomains();
        for (int i = 0; i < result.size(); ++i) {
            result.get(i).setIncludeField(false);
        }
        for (int i = 0; i < fieldsToInclude.size(); ++i) {
            result.get(i).setIncludeField(true);
        }
        return of(RegularBuilderDialogData.builder()
                .withRegularBuilderFieldIncludeFieldIncludeDomains(result)
                .withShouldCreateCopyMethod(generateCopyMethod)
                .build());
    }

}

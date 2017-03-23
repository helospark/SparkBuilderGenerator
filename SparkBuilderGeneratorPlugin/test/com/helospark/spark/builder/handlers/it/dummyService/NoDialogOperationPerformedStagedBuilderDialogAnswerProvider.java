package com.helospark.spark.builder.handlers.it.dummyService;

import org.mockito.invocation.InvocationOnMock;

public class NoDialogOperationPerformedStagedBuilderDialogAnswerProvider {

    public Object provideAnswer(InvocationOnMock inv) {
        return inv.getArguments()[0];
    }

}

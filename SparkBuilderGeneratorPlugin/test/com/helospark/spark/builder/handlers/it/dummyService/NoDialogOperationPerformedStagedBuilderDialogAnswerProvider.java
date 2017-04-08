package com.helospark.spark.builder.handlers.it.dummyService;

import java.util.Optional;

import org.mockito.invocation.InvocationOnMock;

public class NoDialogOperationPerformedStagedBuilderDialogAnswerProvider {

    public Object provideAnswer(InvocationOnMock inv) {
        return Optional.of(inv.getArguments()[0]);
    }

}

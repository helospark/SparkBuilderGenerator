package com.helospark.spark.builder.handlers.it.dummyService;

import java.util.ArrayList;
import java.util.List;

import org.mockito.invocation.InvocationOnMock;

import com.helospark.spark.builder.dialogs.StagedBuilderStagePropertiesDialogResult;

public class ModifiedStagedDialogSettingsAnswerProvider {
    private List<Integer> newOrderIndices;
    private List<Integer> mandatoryFieldIndices;

    public ModifiedStagedDialogSettingsAnswerProvider(List<Integer> newOrder, List<Integer> mandatoryFieldIndices) {
        this.newOrderIndices = newOrder;
        this.mandatoryFieldIndices = mandatoryFieldIndices;
    }

    public Object provideAnswer(InvocationOnMock inv) {
        List<StagedBuilderStagePropertiesDialogResult> request = (List<StagedBuilderStagePropertiesDialogResult>) inv.getArguments()[0];
        List<StagedBuilderStagePropertiesDialogResult> result = new ArrayList<>();
        for (int i = 0; i < newOrderIndices.size(); ++i) {
            StagedBuilderStagePropertiesDialogResult otherRequest = request.get(newOrderIndices.get(i));
            otherRequest.setSelected(false);
            result.add(otherRequest);

        }
        for (int i = 0; i < mandatoryFieldIndices.size(); ++i) {
            result.get(i).setSelected(true);
        }
        return result;
    }

}

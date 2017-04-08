package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.helospark.spark.builder.dialogs.domain.StagedBuilderStagePropertiesDialogResult;
import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

/**
 * Provides stage properties (field order and mandatory setting).
 * Current implementation gets field order from user using a dialog.
 * This implementation might return null, to indicate that the builder generation is no longer required.
 * @author helospark
 */
public class StagedBuilderStagePropertiesProvider {
    private StagedBuilderInterfaceNameProvider stagedBuilderInterfaceNameProvider;
    private StagedBuilderStagePropertyInputDialogOpener dialogOpener;

    public StagedBuilderStagePropertiesProvider(StagedBuilderInterfaceNameProvider stagedBuilderInterfaceNameProvider, StagedBuilderStagePropertyInputDialogOpener dialogOpener) {
        this.stagedBuilderInterfaceNameProvider = stagedBuilderInterfaceNameProvider;
        this.dialogOpener = dialogOpener;
    }

    public Optional<List<StagedBuilderProperties>> build(List<NamedVariableDeclarationField> namedVariableDeclarations) {
        Optional<List<StagedBuilderStagePropertiesDialogResult>> stagePropertiesFromDialog = getStagePropertiesFromUser(namedVariableDeclarations);

        return stagePropertiesFromDialog
                .map(output -> createStages(namedVariableDeclarations, output));
    }

    private List<StagedBuilderProperties> createStages(List<NamedVariableDeclarationField> namedVariableDeclarations,
            List<StagedBuilderStagePropertiesDialogResult> stagePropertiesFromDialog) {
        List<StagedBuilderProperties> result = createMandatoryStagedBuilderProperties(namedVariableDeclarations, stagePropertiesFromDialog);
        StagedBuilderProperties buildStageWithOptionalParameters = createBuildStageWithOptionalParameters(namedVariableDeclarations, stagePropertiesFromDialog);
        result.add(buildStageWithOptionalParameters);
        setNextNextElementReferences(result);

        return result;
    }

    private Optional<List<StagedBuilderStagePropertiesDialogResult>> getStagePropertiesFromUser(List<NamedVariableDeclarationField> namedVariableDeclarations) {
        List<StagedBuilderStagePropertiesDialogResult> dialogRequest = new ArrayList<>();
        for (int i = 0; i < namedVariableDeclarations.size(); ++i) {
            dialogRequest.add(new StagedBuilderStagePropertiesDialogResult(true, namedVariableDeclarations.get(i).getOriginalFieldName(), i));
        }
        return dialogOpener.open(dialogRequest);
    }

    private List<StagedBuilderProperties> createMandatoryStagedBuilderProperties(List<NamedVariableDeclarationField> namedVariableDeclarations,
            List<StagedBuilderStagePropertiesDialogResult> list) {
        return list.stream()
                .filter(a -> a.isMandatory())
                .map(i -> namedVariableDeclarations.get(i.getOriginalIndex()))
                .map(a -> createStagedBuilderProperties(a))
                .collect(Collectors.toList());
    }

    private StagedBuilderProperties createBuildStageWithOptionalParameters(List<NamedVariableDeclarationField> namedVariableDeclarations,
            List<StagedBuilderStagePropertiesDialogResult> list) {
        List<NamedVariableDeclarationField> optionalParameters = getOptionalNamedVariableDeclarations(namedVariableDeclarations, list);
        return StagedBuilderProperties.builder()
                .withInterfaceName(stagedBuilderInterfaceNameProvider.provideBuildStageInterfaceName())
                .withIsBuildStage(true)
                .withNamedVariableDeclarationField(optionalParameters)
                .build();
    }

    private List<NamedVariableDeclarationField> getOptionalNamedVariableDeclarations(List<NamedVariableDeclarationField> namedVariableDeclarations,
            List<StagedBuilderStagePropertiesDialogResult> list) {
        return list.stream()
                .filter(a -> !a.isMandatory())
                .map(i -> namedVariableDeclarations.get(i.getOriginalIndex()))
                .collect(Collectors.toList());
    }

    private StagedBuilderProperties createStagedBuilderProperties(NamedVariableDeclarationField namedVariableDeclarationField) {
        String interfaceName = stagedBuilderInterfaceNameProvider.provideInterfaceNameFrom(namedVariableDeclarationField);
        return StagedBuilderProperties.builder()
                .withInterfaceName(interfaceName)
                .withIsBuildStage(false)
                .withNamedVariableDeclarationField(Collections.singletonList(namedVariableDeclarationField))
                .build();
    }

    private void setNextNextElementReferences(List<StagedBuilderProperties> result) {
        for (int i = 0; i < result.size() - 1; ++i) {
            result.get(i).setNextStage(result.get(i + 1));
        }
    }
}

package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.helospark.spark.builder.dialogs.domain.StagedBuilderStagePropertiesDialogResult;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;

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

    public Optional<List<StagedBuilderProperties>> build(List<BuilderField> builderFields) {
        Optional<List<StagedBuilderStagePropertiesDialogResult>> stagePropertiesFromDialog = getStagePropertiesFromUser(builderFields);

        return stagePropertiesFromDialog
                .map(output -> createStages(builderFields, output));
    }

    private List<StagedBuilderProperties> createStages(List<BuilderField> builderFields,
            List<StagedBuilderStagePropertiesDialogResult> stagePropertiesFromDialog) {
        List<StagedBuilderProperties> result = createMandatoryStagedBuilderProperties(builderFields, stagePropertiesFromDialog);
        StagedBuilderProperties buildStageWithOptionalParameters = createBuildStageWithOptionalParameters(builderFields, stagePropertiesFromDialog);
        result.add(buildStageWithOptionalParameters);
        setNextNextElementReferences(result);

        return result;
    }

    private Optional<List<StagedBuilderStagePropertiesDialogResult>> getStagePropertiesFromUser(List<BuilderField> builderFields) {
        List<StagedBuilderStagePropertiesDialogResult> dialogRequest = new ArrayList<>();
        for (int i = 0; i < builderFields.size(); ++i) {
            dialogRequest.add(new StagedBuilderStagePropertiesDialogResult(true, builderFields.get(i).getOriginalFieldName(), i));
        }
        return dialogOpener.open(dialogRequest);
    }

    private List<StagedBuilderProperties> createMandatoryStagedBuilderProperties(List<BuilderField> builderFields,
            List<StagedBuilderStagePropertiesDialogResult> list) {
        return list.stream()
                .filter(a -> a.isMandatory())
                .map(i -> builderFields.get(i.getOriginalIndex()))
                .map(a -> createStagedBuilderProperties(a))
                .collect(Collectors.toList());
    }

    private StagedBuilderProperties createBuildStageWithOptionalParameters(List<BuilderField> builderFields,
            List<StagedBuilderStagePropertiesDialogResult> list) {
        List<BuilderField> optionalParameters = getOptionalNamedVariableDeclarations(builderFields, list);
        return StagedBuilderProperties.builder()
                .withInterfaceName(stagedBuilderInterfaceNameProvider.provideBuildStageInterfaceName())
                .withIsBuildStage(true)
                .withNamedVariableDeclarationField(optionalParameters)
                .build();
    }

    private List<BuilderField> getOptionalNamedVariableDeclarations(List<BuilderField> builderFields,
            List<StagedBuilderStagePropertiesDialogResult> list) {
        return list.stream()
                .filter(a -> !a.isMandatory())
                .map(i -> builderFields.get(i.getOriginalIndex()))
                .collect(Collectors.toList());
    }

    private StagedBuilderProperties createStagedBuilderProperties(BuilderField builderField) {
        String interfaceName = stagedBuilderInterfaceNameProvider.provideInterfaceNameFrom(builderField);
        return StagedBuilderProperties.builder()
                .withInterfaceName(interfaceName)
                .withIsBuildStage(false)
                .withNamedVariableDeclarationField(Collections.singletonList(builderField))
                .build();
    }

    private void setNextNextElementReferences(List<StagedBuilderProperties> result) {
        for (int i = 0; i < result.size() - 1; ++i) {
            result.get(i).setNextStage(result.get(i + 1));
        }
    }
}

package com.helospark.spark.builder.handlers.codegenerator.component;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.STAGED_BUILDER_SKIP_STATIC_BUILDER_METHOD;

import java.util.List;

import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderProperties;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Adds static builder creator method, which is either
 * <pre>
 * public static IFirstStage builder() ...
 * </pre>
 * or
 * <pre>
 * public static ISecondStage withFirstField(...)
 * </pre>
 * @author helospark
 */
public class StagedBuilderStaticBuilderCreatorMethodCreator {
    private StagedBuilderCreationBuilderMethodAdder blockWithNewBuilderCreationFragment;
    private StagedBuilderCreationWithMethodAdder builderMethodDefinitionCreatorFragment;
    private PreferencesManager preferencesManager;

    public StagedBuilderStaticBuilderCreatorMethodCreator(StagedBuilderCreationBuilderMethodAdder blockWithNewBuilderCreationFragment,
            StagedBuilderCreationWithMethodAdder builderMethodDefinitionCreatorFragment, PreferencesManager preferencesManager) {
        this.blockWithNewBuilderCreationFragment = blockWithNewBuilderCreationFragment;
        this.builderMethodDefinitionCreatorFragment = builderMethodDefinitionCreatorFragment;
        this.preferencesManager = preferencesManager;
    }

    public void addBuilderMethodToCompilationUnit(CompilationUnitModificationDomain modificationDomain, TypeDeclaration builderType,
            List<StagedBuilderProperties> stagedBuilderStages) {
        if (preferencesManager.getPreferenceValue(STAGED_BUILDER_SKIP_STATIC_BUILDER_METHOD) &&
                hasFieldsInFirstStage(stagedBuilderStages)) {
            builderMethodDefinitionCreatorFragment.addBuilderMethodToCompilationUnit(modificationDomain, builderType, stagedBuilderStages.get(0));
        } else {
            blockWithNewBuilderCreationFragment.addBuilderMethodToCompilationUnit(modificationDomain, builderType, stagedBuilderStages.get(0));
        }
    }

    private boolean hasFieldsInFirstStage(List<StagedBuilderProperties> stagedBuilderStages) {
        return !stagedBuilderStages.get(0).getNamedVariableDeclarationField().isEmpty();
    }

}

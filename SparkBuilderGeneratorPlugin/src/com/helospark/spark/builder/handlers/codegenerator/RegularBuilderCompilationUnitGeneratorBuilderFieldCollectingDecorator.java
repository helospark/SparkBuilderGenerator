package com.helospark.spark.builder.handlers.codegenerator;

import static com.helospark.spark.builder.handlers.BuilderType.REGULAR;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.REGULAR_BUILDER_SHOW_FIELD_FILTERING_DIALOG;

import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.BuilderType;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;
import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;
import com.helospark.spark.builder.preferences.PreferencesManager;

public class RegularBuilderCompilationUnitGeneratorBuilderFieldCollectingDecorator implements BuilderCompilationUnitGenerator {
    private ApplicableBuilderFieldExtractor applicableBuilderFieldExtractor;
    private RegularBuilderCompilationUnitGenerator decoratedBuilderGenerator;
    private PreferencesManager preferencesManager;
    private RegularBuilderFieldFilter regularBuilderFieldFilter;

    public RegularBuilderCompilationUnitGeneratorBuilderFieldCollectingDecorator(ApplicableBuilderFieldExtractor applicableBuilderFieldExtractor,
            RegularBuilderCompilationUnitGenerator decoratedBuilderGenerator, PreferencesManager preferencesManager, RegularBuilderFieldFilter regularBuilderFieldFilter) {
        this.applicableBuilderFieldExtractor = applicableBuilderFieldExtractor;
        this.decoratedBuilderGenerator = decoratedBuilderGenerator;
        this.preferencesManager = preferencesManager;
        this.regularBuilderFieldFilter = regularBuilderFieldFilter;
    }

    @Override
    public void generateBuilder(CompilationUnitModificationDomain compilationUnitModificationDomain) {
        TypeDeclaration originalType = compilationUnitModificationDomain.getOriginalType();
        List<NamedVariableDeclarationField> namedVariableDeclarations = applicableBuilderFieldExtractor.findBuilderFields(originalType);
        if (preferencesManager.getPreferenceValue(REGULAR_BUILDER_SHOW_FIELD_FILTERING_DIALOG)) {
            Optional<List<NamedVariableDeclarationField>> filteredFields = regularBuilderFieldFilter.filterFields(namedVariableDeclarations);
            if (filteredFields.isPresent()) {
                decoratedBuilderGenerator.generateBuilder(compilationUnitModificationDomain, filteredFields.get());
            }
        } else {
            decoratedBuilderGenerator.generateBuilder(compilationUnitModificationDomain, namedVariableDeclarations);
        }
    }

    @Override
    public boolean canHandle(BuilderType builderType) {
        return REGULAR.equals(builderType);
    }
}

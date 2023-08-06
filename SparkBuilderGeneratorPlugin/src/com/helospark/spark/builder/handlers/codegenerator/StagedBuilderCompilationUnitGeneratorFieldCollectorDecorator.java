package com.helospark.spark.builder.handlers.codegenerator;

import static com.helospark.spark.builder.handlers.BuilderType.STAGED;

import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;

import com.helospark.spark.builder.handlers.BuilderType;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderProperties;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderStagePropertiesProvider;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;

/**
 * Decorator around {@link StagedBuilderCompilationUnitGenerator} that collects the fields and their
 * properties for staged builder generation.
 * @author helospark
 */
public class StagedBuilderCompilationUnitGeneratorFieldCollectorDecorator implements BuilderCompilationUnitGenerator {
    private StagedBuilderCompilationUnitGenerator decoratedBuilderGenerator;
    private ApplicableBuilderFieldExtractor applicableBuilderFieldExtractor;
    private StagedBuilderStagePropertiesProvider stagedBuilderStagePropertiesProvider;

    public StagedBuilderCompilationUnitGeneratorFieldCollectorDecorator(StagedBuilderCompilationUnitGenerator decoratedBuilderGenerator,
            ApplicableBuilderFieldExtractor applicableBuilderFieldExtractor, StagedBuilderStagePropertiesProvider stagedBuilderStagePropertiesProvider) {
        this.decoratedBuilderGenerator = decoratedBuilderGenerator;
        this.applicableBuilderFieldExtractor = applicableBuilderFieldExtractor;
        this.stagedBuilderStagePropertiesProvider = stagedBuilderStagePropertiesProvider;
    }

    @Override
    public void generateBuilder(CompilationUnitModificationDomain compilationUnitModificationDomain) {
        AbstractTypeDeclaration originalType = compilationUnitModificationDomain.getOriginalType();
        List<BuilderField> fieldToIncludeInBuilder = applicableBuilderFieldExtractor.findBuilderFields(originalType);
        Optional<List<StagedBuilderProperties>> stagedBuilderStages = stagedBuilderStagePropertiesProvider.build(fieldToIncludeInBuilder);

        if (stagedBuilderStages.isPresent()) {
            decoratedBuilderGenerator.generateBuilder(compilationUnitModificationDomain, stagedBuilderStages.get());
        }
    }

    @Override
    public boolean canHandle(BuilderType builderType) {
        return STAGED.equals(builderType);
    }

}

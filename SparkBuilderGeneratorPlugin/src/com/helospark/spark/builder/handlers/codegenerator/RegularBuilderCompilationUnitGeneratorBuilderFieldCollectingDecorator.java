package com.helospark.spark.builder.handlers.codegenerator;

import static com.helospark.spark.builder.handlers.BuilderType.REGULAR;

import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;

import com.helospark.spark.builder.handlers.BuilderType;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;
import com.helospark.spark.builder.handlers.codegenerator.domain.RegularBuilderUserPreference;

/**
 * Decorator around {@link RegularBuilderCompilationUnitGenerator} that collect the fields to include in the regular builder.
 * @author helospark
 */
public class RegularBuilderCompilationUnitGeneratorBuilderFieldCollectingDecorator implements BuilderCompilationUnitGenerator {
    private ApplicableBuilderFieldExtractor applicableBuilderFieldExtractor;
    private RegularBuilderCompilationUnitGenerator decoratedBuilderGenerator;
    private RegularBuilderUserPreferenceProvider preferenceProvider;

    public RegularBuilderCompilationUnitGeneratorBuilderFieldCollectingDecorator(ApplicableBuilderFieldExtractor applicableBuilderFieldExtractor,
            RegularBuilderCompilationUnitGenerator decoratedBuilderGenerator,
            RegularBuilderUserPreferenceProvider regularBuilderUserPreferenceProvider) {
        this.applicableBuilderFieldExtractor = applicableBuilderFieldExtractor;
        this.decoratedBuilderGenerator = decoratedBuilderGenerator;
        this.preferenceProvider = regularBuilderUserPreferenceProvider;
    }

    @Override
    public void generateBuilder(CompilationUnitModificationDomain compilationUnitModificationDomain) {
        AbstractTypeDeclaration originalType = compilationUnitModificationDomain.getOriginalType();
        List<BuilderField> builderFields = applicableBuilderFieldExtractor.findBuilderFields(originalType);
        Optional<RegularBuilderUserPreference> userPreference = preferenceProvider.getPreference(builderFields);
        userPreference.ifPresent(preference -> decoratedBuilderGenerator.generateBuilder(compilationUnitModificationDomain, preference));
    }

    @Override
    public boolean canHandle(BuilderType builderType) {
        return REGULAR.equals(builderType);
    }
}

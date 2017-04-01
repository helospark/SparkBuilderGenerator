package com.helospark.spark.builder.handlers.codegenerator;

import com.helospark.spark.builder.handlers.BuilderType;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;

/**
 * Generates the builder to the given compilation unit.
 * @author helospark
 */
public interface BuilderCompilationUnitGenerator {

    void generateBuilder(CompilationUnitModificationDomain compilationUnitModificationDomain);

    boolean canHandle(BuilderType builderType);
}
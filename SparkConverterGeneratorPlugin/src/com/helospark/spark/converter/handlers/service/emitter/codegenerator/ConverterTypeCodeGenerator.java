package com.helospark.spark.converter.handlers.service.emitter.codegenerator;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;

import com.helospark.spark.converter.handlers.service.domain.CompilationUnitModificationDomain;
import com.helospark.spark.converter.handlers.service.domain.ConvertType;
import com.helospark.spark.converter.handlers.service.domain.ConvertableDomainParameter;

public interface ConverterTypeCodeGenerator {

    public Expression generate(CompilationUnitModificationDomain compilationUnitModificationDomain, Block body, Expression expression, Expression sourceObject,
            ConvertableDomainParameter parameter);

    public boolean canHandle(ConvertType type);

}

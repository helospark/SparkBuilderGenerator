package com.helospark.spark.builder.handlers.codegenerator.builderfieldcollector;

import java.util.List;

import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;

/**
 * Common interface to collect fields for the builder.
 * @author helospark
 */
public interface FieldCollectorChainItem {

    /**
     * Collect field for type.
     * @param typeDeclaration to collect fields from
     * @return collected fields
     */
    public List<? extends BuilderField> collect(TypeDeclaration typeDeclaration);

}

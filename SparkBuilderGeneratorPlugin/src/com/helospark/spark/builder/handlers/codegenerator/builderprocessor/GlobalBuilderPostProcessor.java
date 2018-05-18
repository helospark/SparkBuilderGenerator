package com.helospark.spark.builder.handlers.codegenerator.builderprocessor;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.ADD_JACKSON_DESERIALIZE_ANNOTATION;

import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Post process compilation unit, after the builder class is already added.
 * @author helospark
 */
public class GlobalBuilderPostProcessor {
    private PreferencesManager preferencesManager;
    private JsonDeserializeAdder jsonDeserializeAdder;

    public GlobalBuilderPostProcessor(PreferencesManager preferencesManager, JsonDeserializeAdder jsonDeserializeAdder) {
        this.preferencesManager = preferencesManager;
        this.jsonDeserializeAdder = jsonDeserializeAdder;
    }

    public void postProcessBuilder(CompilationUnitModificationDomain compilationUnitModificationDomain, TypeDeclaration builderType) {
        if (preferencesManager.getPreferenceValue(ADD_JACKSON_DESERIALIZE_ANNOTATION)) {
            jsonDeserializeAdder.addJsonDeserializeAnnotation(compilationUnitModificationDomain, builderType);
        }
    }

}

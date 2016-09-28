package com.helospark.spark.builder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.helospark.spark.builder.handlers.codegenerator.ApplicableFieldExtractor;
import com.helospark.spark.builder.handlers.codegenerator.BuilderPatternCodeGenerator;
import com.helospark.spark.builder.handlers.codegenerator.BuilderRemover;
import com.helospark.spark.builder.handlers.codegenerator.CompilationUnitParser;
import com.helospark.spark.builder.handlers.codegenerator.component.BuilderClassCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.BuilderMethodListRewritePopulator;
import com.helospark.spark.builder.handlers.codegenerator.component.PrivateConstructorListRewritePopulator;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.BuilderMethodNameBuilder;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.ClassNameToVariableNameConverter;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.JavadocGenerator;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.NonNullAnnotationAttacher;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TemplateResolver;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.VariableNameToUpperCamelCaseConverter;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Activator extends AbstractUIPlugin {
    public static List<Object> diContainer = new ArrayList<>();
    public static final String PLUGIN_ID = "com.helospark.SparkBuilderPlugin";

    private static Activator plugin;

    public Activator() {
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        initializeDiContainer();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    public static Activator getDefault() {
        return plugin;
    }

    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    private void initializeDiContainer() {
        diContainer.add(new ClassNameToVariableNameConverter());
        diContainer.add(new VariableNameToUpperCamelCaseConverter());
        diContainer.add(new JavadocGenerator());
        diContainer.add(new TemplateResolver());
        diContainer.add(new PreferencesManager());
        diContainer.add(new BuilderRemover());
        diContainer.add(new CompilationUnitParser());
        diContainer.add(new NonNullAnnotationAttacher());
        diContainer.add(new BuilderMethodNameBuilder(getDependency(VariableNameToUpperCamelCaseConverter.class), getDependency(PreferencesManager.class),
                getDependency(TemplateResolver.class)));
        diContainer.add(new BuilderClassCreator(getDependency(BuilderMethodNameBuilder.class), getDependency(TemplateResolver.class), getDependency(PreferencesManager.class),
                getDependency(JavadocGenerator.class), getDependency(NonNullAnnotationAttacher.class)));
        diContainer.add(new BuilderMethodListRewritePopulator(getDependency(TemplateResolver.class), getDependency(PreferencesManager.class),
                getDependency(JavadocGenerator.class)));
        diContainer.add(new PrivateConstructorListRewritePopulator(getDependency(ClassNameToVariableNameConverter.class)));
        diContainer.add(new ApplicableFieldExtractor());
        diContainer.add(new BuilderPatternCodeGenerator(getDependency(ApplicableFieldExtractor.class), getDependency(BuilderClassCreator.class),
                getDependency(PrivateConstructorListRewritePopulator.class), getDependency(BuilderMethodListRewritePopulator.class)));

    }

    /**
     * Probably will be deprecated after I will be able to create e4 plugin.
     * 
     * @param clazz
     *            type to get
     * @return dependency of that class
     */
    @SuppressWarnings("unchecked")
    public static <T> T getDependency(Class<T> clazz) {
        return (T) diContainer.stream()
                .filter(value -> value.getClass().equals(clazz))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unable to initialize"));
    }
}

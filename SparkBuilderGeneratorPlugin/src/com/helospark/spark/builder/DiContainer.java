package com.helospark.spark.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.eclipse.ui.preferences.WorkingCopyManager;

import com.helospark.spark.builder.handlers.CompilationUnitSourceSetter;
import com.helospark.spark.builder.handlers.CurrentShellProvider;
import com.helospark.spark.builder.handlers.DialogWrapper;
import com.helospark.spark.builder.handlers.ErrorHandlerHook;
import com.helospark.spark.builder.handlers.HandlerUtilWrapper;
import com.helospark.spark.builder.handlers.WorkingCopyManagerWrapper;
import com.helospark.spark.builder.handlers.codegenerator.ApplicableBuilderFieldConverter;
import com.helospark.spark.builder.handlers.codegenerator.BuilderPatternCodeGenerator;
import com.helospark.spark.builder.handlers.codegenerator.BuilderRemover;
import com.helospark.spark.builder.handlers.codegenerator.CompilationUnitParser;
import com.helospark.spark.builder.handlers.codegenerator.component.BuilderClassCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.BuilderMethodListRewritePopulator;
import com.helospark.spark.builder.handlers.codegenerator.component.ImportPopulator;
import com.helospark.spark.builder.handlers.codegenerator.component.PrivateConstructorListRewritePopulator;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.BuilderMethodNameBuilder;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.CamelCaseConverter;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.FieldPrefixSuffixPreferenceProvider;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.GeneratedAnnotationPopulator;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.JavadocGenerator;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.NonNullAnnotationAttacher;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.PreferenceStoreProvider;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TemplateResolver;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.FieldNameToBuilderFieldNameConverter;
import com.helospark.spark.builder.preferences.PreferencesManager;

public class DiContainer {
    public static Map<String, Object> diContainer = new HashMap<>();

    public static void clearDiContainer() {
        diContainer.clear();
    }

    // Visible for testing
    public static void initializeDiContainer() {
        addDependency(new CamelCaseConverter());
        addDependency(new JavadocGenerator());
        addDependency(new TemplateResolver());
        addDependency(new PreferenceStoreProvider());
        addDependency(new PreferencesManager(getDependency(PreferenceStoreProvider.class)));
        addDependency(new BuilderRemover());
        addDependency(new CompilationUnitSourceSetter());
        addDependency(new CurrentShellProvider());
        addDependency(new DialogWrapper(getDependency(CurrentShellProvider.class)));
        addDependency(new ErrorHandlerHook(getDependency(DialogWrapper.class)));
        addDependency(new HandlerUtilWrapper());
        addDependency(new WorkingCopyManager());
        addDependency(new WorkingCopyManagerWrapper(getDependency(HandlerUtilWrapper.class)));
        addDependency(new CompilationUnitParser());
        addDependency(new GeneratedAnnotationPopulator());
        addDependency(new NonNullAnnotationAttacher());
        addDependency(new ImportPopulator(getDependency(PreferencesManager.class)));
        addDependency(new BuilderMethodNameBuilder(getDependency(CamelCaseConverter.class), getDependency(PreferencesManager.class),
                getDependency(TemplateResolver.class)));
        addDependency(new BuilderClassCreator(getDependency(BuilderMethodNameBuilder.class), getDependency(TemplateResolver.class), getDependency(PreferencesManager.class),
                getDependency(JavadocGenerator.class), getDependency(NonNullAnnotationAttacher.class), getDependency(GeneratedAnnotationPopulator.class)));
        addDependency(new BuilderMethodListRewritePopulator(getDependency(TemplateResolver.class), getDependency(PreferencesManager.class),
                getDependency(JavadocGenerator.class), getDependency(GeneratedAnnotationPopulator.class), getDependency(PreferencesManager.class)));
        addDependency(new PrivateConstructorListRewritePopulator(getDependency(CamelCaseConverter.class), getDependency(GeneratedAnnotationPopulator.class),
                getDependency(PreferencesManager.class)));
        addDependency(new FieldPrefixSuffixPreferenceProvider(getDependency(PreferenceStoreProvider.class)));
        addDependency(new FieldNameToBuilderFieldNameConverter(getDependency(PreferencesManager.class), getDependency(FieldPrefixSuffixPreferenceProvider.class),
                getDependency(CamelCaseConverter.class)));
        addDependency(new ApplicableBuilderFieldConverter(getDependency(FieldNameToBuilderFieldNameConverter.class)));
        addDependency(new BuilderPatternCodeGenerator(getDependency(ApplicableBuilderFieldConverter.class), getDependency(BuilderClassCreator.class),
                getDependency(PrivateConstructorListRewritePopulator.class), getDependency(BuilderMethodListRewritePopulator.class), getDependency(ImportPopulator.class)));

    }

    // Visible for testing
    public static void addDependency(Object dependecy) {
        String correctedName = getClassNameWithoutTestPostfix(dependecy);
        diContainer.putIfAbsent(correctedName, dependecy);
    }

    private static String getClassNameWithoutTestPostfix(Object dependecy) {
        String fullName = dependecy.getClass().getName();
        int mockitoNameIndex = fullName.indexOf("$$EnhancerByMockito");
        if (mockitoNameIndex != -1) {
            return fullName.substring(0, mockitoNameIndex);
        }
        return fullName;
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
        return (T) Optional.ofNullable(diContainer.get(clazz.getName()))
                .orElseThrow(() -> new RuntimeException("Unable to initialize"));
    }

}

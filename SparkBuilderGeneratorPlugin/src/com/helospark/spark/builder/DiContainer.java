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
import com.helospark.spark.builder.handlers.codegenerator.ApplicableFieldExtractor;
import com.helospark.spark.builder.handlers.codegenerator.BuilderPatternCodeGenerator;
import com.helospark.spark.builder.handlers.codegenerator.BuilderRemover;
import com.helospark.spark.builder.handlers.codegenerator.CompilationUnitParser;
import com.helospark.spark.builder.handlers.codegenerator.component.BuilderClassCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.BuilderMethodListRewritePopulator;
import com.helospark.spark.builder.handlers.codegenerator.component.ImportPopulator;
import com.helospark.spark.builder.handlers.codegenerator.component.PrivateConstructorListRewritePopulator;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.BuilderMethodNameBuilder;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.ClassNameToVariableNameConverter;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.GeneratedAnnotationPopulator;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.JavadocGenerator;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.NonNullAnnotationAttacher;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TemplateResolver;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.VariableNameToUpperCamelCaseConverter;
import com.helospark.spark.builder.preferences.PreferencesManager;

public class DiContainer {
    public static Map<String, Object> diContainer = new HashMap<>();

    // Visible for testing
    public static void initializeDiContainer() {
        addDepenency(new ClassNameToVariableNameConverter());
        addDepenency(new VariableNameToUpperCamelCaseConverter());
        addDepenency(new JavadocGenerator());
        addDepenency(new TemplateResolver());
        addDepenency(new PreferencesManager());
        addDepenency(new BuilderRemover());
        addDepenency(new CompilationUnitSourceSetter());
        addDepenency(new CurrentShellProvider());
        addDepenency(new DialogWrapper(getDependency(CurrentShellProvider.class)));
        addDepenency(new ErrorHandlerHook(getDependency(DialogWrapper.class)));
        addDepenency(new HandlerUtilWrapper());
        addDepenency(new WorkingCopyManager());
        addDepenency(new WorkingCopyManagerWrapper(getDependency(HandlerUtilWrapper.class)));
        addDepenency(new CompilationUnitParser());
        addDepenency(new GeneratedAnnotationPopulator());
        addDepenency(new NonNullAnnotationAttacher());
        addDepenency(new ImportPopulator(getDependency(PreferencesManager.class)));
        addDepenency(new BuilderMethodNameBuilder(getDependency(VariableNameToUpperCamelCaseConverter.class), getDependency(PreferencesManager.class),
                getDependency(TemplateResolver.class)));
        addDepenency(new BuilderClassCreator(getDependency(BuilderMethodNameBuilder.class), getDependency(TemplateResolver.class), getDependency(PreferencesManager.class),
                getDependency(JavadocGenerator.class), getDependency(NonNullAnnotationAttacher.class), getDependency(GeneratedAnnotationPopulator.class)));
        addDepenency(new BuilderMethodListRewritePopulator(getDependency(TemplateResolver.class), getDependency(PreferencesManager.class),
                getDependency(JavadocGenerator.class), getDependency(GeneratedAnnotationPopulator.class), getDependency(PreferencesManager.class)));
        addDepenency(new PrivateConstructorListRewritePopulator(getDependency(ClassNameToVariableNameConverter.class), getDependency(GeneratedAnnotationPopulator.class),
                getDependency(PreferencesManager.class)));
        addDepenency(new ApplicableFieldExtractor());
        addDepenency(new BuilderPatternCodeGenerator(getDependency(ApplicableFieldExtractor.class), getDependency(BuilderClassCreator.class),
                getDependency(PrivateConstructorListRewritePopulator.class), getDependency(BuilderMethodListRewritePopulator.class), getDependency(ImportPopulator.class)));

    }

    // Visible for testing
    public static void addDepenency(Object dependecy) {
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

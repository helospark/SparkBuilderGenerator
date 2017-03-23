package com.helospark.spark.builder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.preferences.WorkingCopyManager;

import com.helospark.spark.builder.handlers.CompilationUnitSourceSetter;
import com.helospark.spark.builder.handlers.CurrentShellProvider;
import com.helospark.spark.builder.handlers.DialogWrapper;
import com.helospark.spark.builder.handlers.ErrorHandlerHook;
import com.helospark.spark.builder.handlers.GenerateBuilderExecutorImpl;
import com.helospark.spark.builder.handlers.GenerateBuilderHandlerErrorHandlerDecorator;
import com.helospark.spark.builder.handlers.HandlerUtilWrapper;
import com.helospark.spark.builder.handlers.IsEventOnJavaFilePredicate;
import com.helospark.spark.builder.handlers.StateInitializerGenerateBuilderExecutorDecorator;
import com.helospark.spark.builder.handlers.StatefulBeanHandler;
import com.helospark.spark.builder.handlers.WorkingCopyManagerWrapper;
import com.helospark.spark.builder.handlers.codegenerator.ApplicableBuilderFieldExtractor;
import com.helospark.spark.builder.handlers.codegenerator.BuilderCompilationUnitGenerator;
import com.helospark.spark.builder.handlers.codegenerator.BuilderOwnerClassFinder;
import com.helospark.spark.builder.handlers.codegenerator.BuilderRemover;
import com.helospark.spark.builder.handlers.codegenerator.CompilationUnitParser;
import com.helospark.spark.builder.handlers.codegenerator.RegularBuilderCompilationUnitGenerator;
import com.helospark.spark.builder.handlers.codegenerator.StagedBuilderCompilationUnitGenerator;
import com.helospark.spark.builder.handlers.codegenerator.component.BuilderAstRemover;
import com.helospark.spark.builder.handlers.codegenerator.component.ImportPopulator;
import com.helospark.spark.builder.handlers.codegenerator.component.PrivateInitializingConstructorCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.RegularBuilderBuilderMethodCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.RegularBuilderClassCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.StagedBuilderBuilderMethodCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.StagedBuilderClassCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.EmptyBuilderClassGeneratorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.buildmethod.BuildMethodAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.buildmethod.BuildMethodBodyCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.buildmethod.BuildMethodDeclarationCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.constructor.PrivateConstructorAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.field.BuilderFieldAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.stagedinterface.StagedBuilderInterfaceCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.withmethod.RegularBuilderWithMethodAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.withmethod.StagedBuilderWithMethodAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.BlockWithNewBuilderCreationFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.BuilderMethodDefinitionCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.StagedBuilderMethodDefiniationCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.constructor.PrivateConstructorBodyCreationFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.constructor.PrivateConstructorInsertionFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.constructor.PrivateConstructorMethodDefinitionCreationFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.BuilderMethodNameBuilder;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.CamelCaseConverter;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.FieldNameToBuilderFieldNameConverter;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.FieldPrefixSuffixPreferenceProvider;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.GeneratedAnnotationPopulator;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.InterfaceSetter;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.JavadocGenerator;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.NonNullAnnotationAttacher;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.PreferenceStoreProvider;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderFieldOrderProvider;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderInterfaceNameProvider;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderStagePropertyInputDialogOpener;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TemplateResolver;
import com.helospark.spark.builder.preferences.PreferencesManager;

public class DiContainer {
    public static List<Object> diContainer = new ArrayList<>();

    public static void clearDiContainer() {
        diContainer.clear();
    }

    // Visible for testing
    public static void initializeDiContainer() {
        addDependency(new CamelCaseConverter());
        addDependency(new JavadocGenerator());
        addDependency(new TemplateResolver());
        addDependency(new PreferenceStoreProvider());
        addDependency(new CurrentShellProvider());
        addDependency(new DialogWrapper(getDependency(CurrentShellProvider.class)));
        addDependency(new PreferencesManager(getDependency(PreferenceStoreProvider.class)));
        addDependency(new ErrorHandlerHook(getDependency(DialogWrapper.class)));
        addDependency(new BuilderAstRemover());
        addDependency(new BuilderRemover(getDependency(PreferencesManager.class), getDependency(ErrorHandlerHook.class),
                getDependency(BuilderAstRemover.class)));
        addDependency(new CompilationUnitSourceSetter());
        addDependency(new HandlerUtilWrapper());
        addDependency(new WorkingCopyManager());
        addDependency(new WorkingCopyManagerWrapper(getDependency(HandlerUtilWrapper.class)));
        addDependency(new CompilationUnitParser());
        addDependency(new GeneratedAnnotationPopulator());
        addDependency(new NonNullAnnotationAttacher());
        addDependency(new ImportPopulator(getDependency(PreferencesManager.class)));
        addDependency(new BuilderMethodNameBuilder(getDependency(CamelCaseConverter.class),
                getDependency(PreferencesManager.class),
                getDependency(TemplateResolver.class)));
        addDependency(new PrivateConstructorAdderFragment());
        addDependency(new EmptyBuilderClassGeneratorFragment(getDependency(GeneratedAnnotationPopulator.class),
                getDependency(PreferencesManager.class),
                getDependency(JavadocGenerator.class), getDependency(TemplateResolver.class)));
        addDependency(new BuildMethodBodyCreator());
        addDependency(new BuildMethodDeclarationCreatorFragment(getDependency(PreferencesManager.class),
                getDependency(NonNullAnnotationAttacher.class), getDependency(JavadocGenerator.class),
                getDependency(TemplateResolver.class)));
        addDependency(new BuildMethodAdderFragment(getDependency(BuildMethodDeclarationCreatorFragment.class),
                getDependency(BuildMethodBodyCreator.class)));
        addDependency(new BuilderFieldAdderFragment());
        addDependency(new RegularBuilderWithMethodAdderFragment(getDependency(PreferencesManager.class),
                getDependency(JavadocGenerator.class),
                getDependency(NonNullAnnotationAttacher.class), getDependency(BuilderMethodNameBuilder.class)));
        addDependency(new RegularBuilderClassCreator(getDependency(PrivateConstructorAdderFragment.class),
                getDependency(EmptyBuilderClassGeneratorFragment.class),
                getDependency(BuildMethodAdderFragment.class),
                getDependency(BuilderFieldAdderFragment.class),
                getDependency(RegularBuilderWithMethodAdderFragment.class)));
        addDependency(new BuilderMethodDefinitionCreatorFragment(getDependency(TemplateResolver.class),
                getDependency(PreferencesManager.class),
                getDependency(JavadocGenerator.class), getDependency(GeneratedAnnotationPopulator.class),
                getDependency(PreferencesManager.class)));
        addDependency(new BlockWithNewBuilderCreationFragment());
        addDependency(
                new RegularBuilderBuilderMethodCreator(getDependency(BlockWithNewBuilderCreationFragment.class),
                        getDependency(BuilderMethodDefinitionCreatorFragment.class)));
        addDependency(new PrivateConstructorMethodDefinitionCreationFragment(getDependency(PreferencesManager.class),
                getDependency(GeneratedAnnotationPopulator.class),
                getDependency(CamelCaseConverter.class)));
        addDependency(new PrivateConstructorBodyCreationFragment(getDependency(CamelCaseConverter.class)));
        addDependency(new PrivateConstructorInsertionFragment());
        addDependency(new PrivateInitializingConstructorCreator(
                getDependency(PrivateConstructorMethodDefinitionCreationFragment.class),
                getDependency(PrivateConstructorBodyCreationFragment.class),
                getDependency(PrivateConstructorInsertionFragment.class)));
        addDependency(new FieldPrefixSuffixPreferenceProvider(getDependency(PreferenceStoreProvider.class)));
        addDependency(new FieldNameToBuilderFieldNameConverter(getDependency(PreferencesManager.class),
                getDependency(FieldPrefixSuffixPreferenceProvider.class),
                getDependency(CamelCaseConverter.class)));
        addDependency(new ApplicableBuilderFieldExtractor(getDependency(FieldNameToBuilderFieldNameConverter.class)));
        addDependency(new BuilderOwnerClassFinder());
        addDependency(new RegularBuilderCompilationUnitGenerator(getDependency(ApplicableBuilderFieldExtractor.class),
                getDependency(RegularBuilderClassCreator.class),
                getDependency(PrivateInitializingConstructorCreator.class),
                getDependency(RegularBuilderBuilderMethodCreator.class), getDependency(ImportPopulator.class),
                getDependency(BuilderOwnerClassFinder.class)));
        addDependency(new IsEventOnJavaFilePredicate(getDependency(HandlerUtilWrapper.class)));

        // staged builder dependencies
        addDependency(new StagedBuilderBuilderMethodCreator(getDependency(BlockWithNewBuilderCreationFragment.class),
                getDependency(BuilderMethodDefinitionCreatorFragment.class)));
        addDependency(new StagedBuilderInterfaceNameProvider(getDependency(CamelCaseConverter.class)));
        addDependency(new StagedBuilderMethodDefiniationCreatorFragment(getDependency(PreferencesManager.class),
                getDependency(BuilderMethodNameBuilder.class),
                getDependency(NonNullAnnotationAttacher.class),
                getDependency(StagedBuilderInterfaceNameProvider.class)));
        addDependency(new StagedBuilderInterfaceCreatorFragment(getDependency(StagedBuilderMethodDefiniationCreatorFragment.class),
                getDependency(BuildMethodDeclarationCreatorFragment.class)));
        addDependency(new StagedBuilderWithMethodAdderFragment(
                getDependency(StagedBuilderMethodDefiniationCreatorFragment.class)));
        addDependency(new InterfaceSetter());
        addDependency(new StagedBuilderClassCreator(getDependency(PrivateConstructorAdderFragment.class),
                getDependency(EmptyBuilderClassGeneratorFragment.class),
                getDependency(BuildMethodAdderFragment.class),
                getDependency(BuilderFieldAdderFragment.class),
                getDependency(StagedBuilderWithMethodAdderFragment.class),
                getDependency(InterfaceSetter.class)));
        addDependency(new StagedBuilderStagePropertyInputDialogOpener(getDependency(CurrentShellProvider.class)));
        addDependency(new StagedBuilderFieldOrderProvider(getDependency(StagedBuilderInterfaceNameProvider.class),
                getDependency(StagedBuilderStagePropertyInputDialogOpener.class)));
        addDependency(new StagedBuilderCompilationUnitGenerator(getDependency(ApplicableBuilderFieldExtractor.class),
                getDependency(StagedBuilderClassCreator.class),
                getDependency(PrivateInitializingConstructorCreator.class),
                getDependency(StagedBuilderBuilderMethodCreator.class), getDependency(ImportPopulator.class),
                getDependency(BuilderOwnerClassFinder.class), getDependency(StagedBuilderFieldOrderProvider.class),
                getDependency(StagedBuilderInterfaceCreatorFragment.class)));

        // Generator chain
        addDependency(new GenerateBuilderExecutorImpl(getDependency(CompilationUnitParser.class),
                getDependencyList(BuilderCompilationUnitGenerator.class),
                getDependency(BuilderRemover.class),
                getDependency(IsEventOnJavaFilePredicate.class), getDependency(WorkingCopyManagerWrapper.class),
                getDependency(CompilationUnitSourceSetter.class)));
        addDependency(new GenerateBuilderHandlerErrorHandlerDecorator(getDependency(GenerateBuilderExecutorImpl.class),
                getDependency(ErrorHandlerHook.class)));
        addDependency(new StatefulBeanHandler(getDependency(PreferenceStoreProvider.class),
                getDependency(WorkingCopyManagerWrapper.class)));
        addDependency(
                new StateInitializerGenerateBuilderExecutorDecorator(
                        getDependency(GenerateBuilderHandlerErrorHandlerDecorator.class),
                        getDependency(StatefulBeanHandler.class)));
    }

    // Visible for testing
    public static void addDependency(Object dependency) {
        boolean alreadyHasDependency = diContainer.stream()
                .filter(value -> isSameMockitoMockDependency(dependency.getClass().toString(),
                        value.getClass().toString()))
                .findFirst()
                .isPresent();

        if (!alreadyHasDependency) {
            diContainer.add(dependency);
        }
    }

    private static boolean isSameMockitoMockDependency(String newDependency, String oldDependency) {
        int mockitoClassNameStartIndex = oldDependency.indexOf("$$EnhancerByMockitoWithCGLIB");
        if (mockitoClassNameStartIndex != -1) {
            String mockitolessClassName = oldDependency.substring(0, mockitoClassNameStartIndex);
            return newDependency.equals(mockitolessClassName);
        } else {
            return false;
        }
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
                .filter(value -> clazz.isAssignableFrom(value.getClass()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unable to initialize " + clazz.getName() + " not found"));
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getDependencyList(Class<T> classToFind) {
        List<Object> result = new ArrayList<>();
        for (Object o : diContainer) {
            if (classToFind.isAssignableFrom(o.getClass())) {
                result.add(o);
            }
        }
        return (List<T>) result;
    }

}

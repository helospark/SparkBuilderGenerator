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
import com.helospark.spark.builder.handlers.ImportRepository;
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
import com.helospark.spark.builder.handlers.codegenerator.RegularBuilderCompilationUnitGeneratorBuilderFieldCollectingDecorator;
import com.helospark.spark.builder.handlers.codegenerator.RegularBuilderUserPreferenceDialogOpener;
import com.helospark.spark.builder.handlers.codegenerator.RegularBuilderUserPreferenceProvider;
import com.helospark.spark.builder.handlers.codegenerator.StagedBuilderCompilationUnitGenerator;
import com.helospark.spark.builder.handlers.codegenerator.StagedBuilderCompilationUnitGeneratorFieldCollectorDecorator;
import com.helospark.spark.builder.handlers.codegenerator.builderfieldcollector.ClassFieldCollector;
import com.helospark.spark.builder.handlers.codegenerator.builderfieldcollector.SuperConstructorParameterCollector;
import com.helospark.spark.builder.handlers.codegenerator.builderprocessor.GlobalBuilderPostProcessor;
import com.helospark.spark.builder.handlers.codegenerator.builderprocessor.JsonDeserializeAdder;
import com.helospark.spark.builder.handlers.codegenerator.component.BuilderAstRemover;
import com.helospark.spark.builder.handlers.codegenerator.component.ImportPopulator;
import com.helospark.spark.builder.handlers.codegenerator.component.PrivateInitializingConstructorCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.RegularBuilderBuilderMethodCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.RegularBuilderClassCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.RegularBuilderCopyInstanceBuilderMethodCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.StagedBuilderClassCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.StagedBuilderCreationBuilderMethodAdder;
import com.helospark.spark.builder.handlers.codegenerator.component.StagedBuilderCreationWithMethodAdder;
import com.helospark.spark.builder.handlers.codegenerator.component.StagedBuilderStaticBuilderCreatorMethodCreator;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.EmptyBuilderClassGeneratorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.builderclass.JsonPOJOBuilderAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.buildmethod.BuildMethodBodyCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.buildmethod.BuildMethodCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.buildmethod.BuildMethodDeclarationCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.constructor.PrivateConstructorAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.constructor.RegularBuilderCopyInstanceConstructorAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.field.BuilderFieldAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.field.FieldDeclarationPostProcessor;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.field.FullyQualifiedNameExtractor;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.field.StaticMethodInvocationFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.field.chain.BuiltInCollectionsInitializerChainitem;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.field.chain.FieldDeclarationPostProcessorChainItem;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.field.chain.OptionalInitializerChainItem;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.stagedinterface.StagedBuilderInterfaceCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.stagedinterface.StagedBuilderInterfaceTypeDefinitionCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.withmethod.RegularBuilderWithMethodAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.withmethod.StagedBuilderWithMethodAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.withmethod.StagedBuilderWithMethodDefiniationCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.withmethod.WithMethodParameterCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.StaticBuilderMethodSignatureGeneratorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.copy.BlockWithNewCopyInstanceConstructorCreationFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.copy.CopyInstanceBuilderMethodDefinitionCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.empty.BlockWithNewBuilderCreationFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.empty.BuilderMethodDefinitionCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.empty.NewBuilderAndWithMethodCallCreationFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.constructor.BuilderFieldAccessCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.constructor.FieldSetterAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.constructor.PrivateConstructorBodyCreationFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.constructor.PrivateConstructorInsertionFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.constructor.PrivateConstructorMethodDefinitionCreationFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.ActiveJavaEditorOffsetProvider;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.ApplicableFieldVisibilityFilter;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.BuilderMethodNameBuilder;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.CamelCaseConverter;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.CurrentlySelectedApplicableClassesClassNameProvider;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.FieldNameToBuilderFieldNameConverter;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.FieldPrefixSuffixPreferenceProvider;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.GeneratedAnnotationPopulator;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.ITypeExtractor;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.InterfaceSetter;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.IsRegularBuilderInstanceCopyEnabledPredicate;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.IsTypeApplicableForBuilderGenerationPredicate;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.JavadocAdder;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.JavadocGenerator;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.MarkerAnnotationAttacher;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.ParentITypeExtractor;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.PreferenceStoreProvider;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderInterfaceNameProvider;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderStagePropertiesProvider;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderStagePropertyInputDialogOpener;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TemplateResolver;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TypeDeclarationFromSuperclassExtractor;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TypeDeclarationToVariableNameConverter;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.domain.BodyDeclarationVisibleFromPredicate;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.BuilderClassRemover;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.BuilderRemoverChainItem;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.JsonDeserializeRemover;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.PrivateConstructorRemover;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.StagedBuilderInterfaceRemover;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.StaticBuilderMethodRemover;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.BodyDeclarationOfTypeExtractor;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.GeneratedAnnotationContainingBodyDeclarationFilter;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.GeneratedAnnotationPredicate;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.GenericModifierPredicate;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.IsPrivatePredicate;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.IsPublicPredicate;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.IsStaticPredicate;
import com.helospark.spark.builder.handlers.codegenerator.converter.RegularBuilderDialogDataConverter;
import com.helospark.spark.builder.handlers.codegenerator.converter.RegularBuilderUserPreferenceConverter;
import com.helospark.spark.builder.preferences.PreferencesManager;

public class DiContainer {
    public static List<Object> diContainer = new ArrayList<>();

    public static void clearDiContainer() {
        diContainer.clear();
    }

    // Visible for testing
    // TODO: Introduce LightDi here
    public static void initializeDiContainer() {
        addDependency(new CamelCaseConverter());
        addDependency(new JavadocGenerator());
        addDependency(new TemplateResolver());
        addDependency(new PreferenceStoreProvider());
        addDependency(new CurrentShellProvider());
        addDependency(new ITypeExtractor());
        addDependency(new DialogWrapper(getDependency(CurrentShellProvider.class)));
        addDependency(new PreferencesManager(getDependency(PreferenceStoreProvider.class)));
        addDependency(new ErrorHandlerHook(getDependency(DialogWrapper.class)));

        addDependency(new BodyDeclarationOfTypeExtractor());
        addDependency(new GeneratedAnnotationPredicate());
        addDependency(new GenericModifierPredicate());
        addDependency(new IsPrivatePredicate(getDependency(GenericModifierPredicate.class)));
        addDependency(new IsStaticPredicate(getDependency(GenericModifierPredicate.class)));
        addDependency(new IsPublicPredicate(getDependency(GenericModifierPredicate.class)));
        addDependency(new GeneratedAnnotationContainingBodyDeclarationFilter(getDependency(GeneratedAnnotationPredicate.class)));
        addDependency(new PrivateConstructorRemover(getDependency(IsPrivatePredicate.class),
                getDependency(GeneratedAnnotationContainingBodyDeclarationFilter.class)));
        addDependency(new BodyDeclarationOfTypeExtractor());
        addDependency(new BuilderClassRemover(getDependency(BodyDeclarationOfTypeExtractor.class),
                getDependency(GeneratedAnnotationContainingBodyDeclarationFilter.class),
                getDependency(IsPrivatePredicate.class)));
        addDependency(new JsonDeserializeRemover(getDependency(PreferencesManager.class)));
        addDependency(new StagedBuilderInterfaceRemover(getDependency(BodyDeclarationOfTypeExtractor.class),
                getDependency(GeneratedAnnotationContainingBodyDeclarationFilter.class)));
        addDependency(new StaticBuilderMethodRemover(getDependency(IsStaticPredicate.class), getDependency(IsPublicPredicate.class),
                getDependency(GeneratedAnnotationContainingBodyDeclarationFilter.class)));
        addDependency(new BuilderAstRemover(getDependencyList(BuilderRemoverChainItem.class)));

        addDependency(new BuilderRemover(getDependency(PreferencesManager.class), getDependency(ErrorHandlerHook.class),
                getDependency(BuilderAstRemover.class)));
        addDependency(new CompilationUnitSourceSetter());
        addDependency(new HandlerUtilWrapper());
        addDependency(new WorkingCopyManager());
        addDependency(new WorkingCopyManagerWrapper(getDependency(HandlerUtilWrapper.class)));
        addDependency(new CompilationUnitParser());
        addDependency(new GeneratedAnnotationPopulator(getDependency(PreferencesManager.class)));
        addDependency(new MarkerAnnotationAttacher());
        addDependency(new ImportRepository());
        addDependency(new ImportPopulator(getDependency(PreferencesManager.class), getDependency(ImportRepository.class)));
        addDependency(new BuilderMethodNameBuilder(getDependency(CamelCaseConverter.class),
                getDependency(PreferencesManager.class),
                getDependency(TemplateResolver.class)));
        addDependency(new PrivateConstructorAdderFragment());
        addDependency(new JsonPOJOBuilderAdderFragment(getDependency(PreferencesManager.class), getDependency(ImportRepository.class)));
        addDependency(new EmptyBuilderClassGeneratorFragment(getDependency(GeneratedAnnotationPopulator.class),
                getDependency(PreferencesManager.class),
                getDependency(JavadocGenerator.class),
                getDependency(TemplateResolver.class),
                getDependency(JsonPOJOBuilderAdderFragment.class)));
        addDependency(new BuildMethodBodyCreatorFragment());
        addDependency(new BuildMethodDeclarationCreatorFragment(getDependency(PreferencesManager.class),
                getDependency(MarkerAnnotationAttacher.class),
                getDependency(TemplateResolver.class)));
        addDependency(new JavadocAdder(getDependency(JavadocGenerator.class), getDependency(PreferencesManager.class)));
        addDependency(new BuildMethodCreatorFragment(getDependency(BuildMethodDeclarationCreatorFragment.class),
                getDependency(BuildMethodBodyCreatorFragment.class)));
        addDependency(new FullyQualifiedNameExtractor());
        addDependency(new StaticMethodInvocationFragment());
        addDependency(new OptionalInitializerChainItem(getDependency(StaticMethodInvocationFragment.class), getDependency(PreferencesManager.class)));
        addDependency(new BuiltInCollectionsInitializerChainitem(getDependency(StaticMethodInvocationFragment.class), getDependency(PreferencesManager.class)));
        addDependency(new FieldDeclarationPostProcessor(getDependency(PreferencesManager.class), getDependency(FullyQualifiedNameExtractor.class),
                getDependency(ImportRepository.class), getDependencyList(FieldDeclarationPostProcessorChainItem.class)));
        addDependency(new BuilderFieldAdderFragment(getDependency(FieldDeclarationPostProcessor.class)));
        addDependency(new WithMethodParameterCreatorFragment(getDependency(PreferencesManager.class), getDependency(MarkerAnnotationAttacher.class)));
        addDependency(new RegularBuilderWithMethodAdderFragment(getDependency(PreferencesManager.class),
                getDependency(JavadocAdder.class),
                getDependency(MarkerAnnotationAttacher.class),
                getDependency(BuilderMethodNameBuilder.class),
                getDependency(WithMethodParameterCreatorFragment.class)));
        addDependency(new BuilderFieldAccessCreatorFragment());
        addDependency(new TypeDeclarationToVariableNameConverter(getDependency(CamelCaseConverter.class)));
        addDependency(new FieldSetterAdderFragment(getDependency(BuilderFieldAccessCreatorFragment.class)));
        addDependency(new IsRegularBuilderInstanceCopyEnabledPredicate(getDependency(PreferencesManager.class)));
        addDependency(
                new RegularBuilderCopyInstanceConstructorAdderFragment(getDependency(FieldSetterAdderFragment.class), getDependency(TypeDeclarationToVariableNameConverter.class),
                        getDependency(IsRegularBuilderInstanceCopyEnabledPredicate.class)));
        addDependency(new RegularBuilderClassCreator(getDependency(PrivateConstructorAdderFragment.class),
                getDependency(EmptyBuilderClassGeneratorFragment.class),
                getDependency(BuildMethodCreatorFragment.class),
                getDependency(BuilderFieldAdderFragment.class),
                getDependency(RegularBuilderWithMethodAdderFragment.class),
                getDependency(JavadocAdder.class),
                getDependency(RegularBuilderCopyInstanceConstructorAdderFragment.class)));
        addDependency(new StaticBuilderMethodSignatureGeneratorFragment(getDependency(GeneratedAnnotationPopulator.class), getDependency(PreferencesManager.class)));
        addDependency(new BuilderMethodDefinitionCreatorFragment(getDependency(TemplateResolver.class),
                getDependency(PreferencesManager.class),
                getDependency(JavadocAdder.class), getDependency(StaticBuilderMethodSignatureGeneratorFragment.class)));
        addDependency(new BlockWithNewBuilderCreationFragment());
        addDependency(
                new RegularBuilderBuilderMethodCreator(getDependency(BlockWithNewBuilderCreationFragment.class),
                        getDependency(BuilderMethodDefinitionCreatorFragment.class)));
        addDependency(new PrivateConstructorMethodDefinitionCreationFragment(getDependency(PreferencesManager.class),
                getDependency(GeneratedAnnotationPopulator.class),
                getDependency(CamelCaseConverter.class)));
        addDependency(new PrivateConstructorBodyCreationFragment(getDependency(TypeDeclarationToVariableNameConverter.class),
                getDependency(FieldSetterAdderFragment.class),
                getDependency(BuilderFieldAccessCreatorFragment.class)));
        addDependency(new PrivateConstructorInsertionFragment());
        addDependency(new PrivateInitializingConstructorCreator(
                getDependency(PrivateConstructorMethodDefinitionCreationFragment.class),
                getDependency(PrivateConstructorBodyCreationFragment.class),
                getDependency(PrivateConstructorInsertionFragment.class)));
        addDependency(new FieldPrefixSuffixPreferenceProvider(getDependency(PreferenceStoreProvider.class)));
        addDependency(new FieldNameToBuilderFieldNameConverter(getDependency(PreferencesManager.class),
                getDependency(FieldPrefixSuffixPreferenceProvider.class),
                getDependency(CamelCaseConverter.class)));
        addDependency(new TypeDeclarationFromSuperclassExtractor(getDependency(CompilationUnitParser.class),
                getDependency(ITypeExtractor.class)));
        addDependency(new BodyDeclarationVisibleFromPredicate());
        addDependency(new ApplicableFieldVisibilityFilter(getDependency(BodyDeclarationVisibleFromPredicate.class)));
        addDependency(new ClassFieldCollector(getDependency(FieldNameToBuilderFieldNameConverter.class),
                getDependency(PreferencesManager.class), getDependency(TypeDeclarationFromSuperclassExtractor.class),
                getDependency(ApplicableFieldVisibilityFilter.class)));
        addDependency(new SuperConstructorParameterCollector(getDependency(FieldNameToBuilderFieldNameConverter.class),
                getDependency(PreferencesManager.class), getDependency(TypeDeclarationFromSuperclassExtractor.class),
                getDependency(BodyDeclarationVisibleFromPredicate.class)));
        addDependency(new ApplicableBuilderFieldExtractor(getDependency(ClassFieldCollector.class), getDependency(SuperConstructorParameterCollector.class)));
        addDependency(new ActiveJavaEditorOffsetProvider());
        addDependency(new ParentITypeExtractor());
        addDependency(new IsTypeApplicableForBuilderGenerationPredicate(getDependency(ParentITypeExtractor.class)));
        addDependency(new CurrentlySelectedApplicableClassesClassNameProvider(getDependency(ActiveJavaEditorOffsetProvider.class),
                getDependency(IsTypeApplicableForBuilderGenerationPredicate.class),
                getDependency(ParentITypeExtractor.class)));
        addDependency(new BuilderOwnerClassFinder(getDependency(CurrentlySelectedApplicableClassesClassNameProvider.class),
                getDependency(PreferencesManager.class), getDependency(GeneratedAnnotationPredicate.class)));
        addDependency(new BlockWithNewCopyInstanceConstructorCreationFragment());
        addDependency(new CopyInstanceBuilderMethodDefinitionCreatorFragment(getDependency(TemplateResolver.class),
                getDependency(PreferencesManager.class),
                getDependency(JavadocAdder.class), getDependency(StaticBuilderMethodSignatureGeneratorFragment.class)));
        addDependency(new RegularBuilderCopyInstanceBuilderMethodCreator(getDependency(BlockWithNewCopyInstanceConstructorCreationFragment.class),
                getDependency(CopyInstanceBuilderMethodDefinitionCreatorFragment.class),
                getDependency(TypeDeclarationToVariableNameConverter.class),
                getDependency(IsRegularBuilderInstanceCopyEnabledPredicate.class)));
        addDependency(new JsonDeserializeAdder(getDependency(ImportRepository.class)));
        addDependency(new GlobalBuilderPostProcessor(getDependency(PreferencesManager.class), getDependency(JsonDeserializeAdder.class)));
        addDependency(new RegularBuilderCompilationUnitGenerator(getDependency(RegularBuilderClassCreator.class),
                getDependency(RegularBuilderCopyInstanceBuilderMethodCreator.class),
                getDependency(PrivateInitializingConstructorCreator.class),
                getDependency(RegularBuilderBuilderMethodCreator.class), getDependency(ImportPopulator.class),
                getDependency(BuilderRemover.class),
                getDependency(GlobalBuilderPostProcessor.class)));
        addDependency(new RegularBuilderUserPreferenceDialogOpener(getDependency(CurrentShellProvider.class)));
        addDependency(new RegularBuilderDialogDataConverter());
        addDependency(new RegularBuilderUserPreferenceConverter(getDependency(PreferencesManager.class)));
        addDependency(new RegularBuilderUserPreferenceProvider(getDependency(RegularBuilderUserPreferenceDialogOpener.class),
                getDependency(PreferencesManager.class),
                getDependency(RegularBuilderDialogDataConverter.class),
                getDependency(RegularBuilderUserPreferenceConverter.class)));
        addDependency(new RegularBuilderCompilationUnitGeneratorBuilderFieldCollectingDecorator(getDependency(ApplicableBuilderFieldExtractor.class),
                getDependency(RegularBuilderCompilationUnitGenerator.class),
                getDependency(RegularBuilderUserPreferenceProvider.class)));
        addDependency(new IsEventOnJavaFilePredicate(getDependency(HandlerUtilWrapper.class)));

        // staged builder dependencies
        addDependency(new StagedBuilderInterfaceNameProvider(getDependency(PreferencesManager.class),
                getDependency(CamelCaseConverter.class),
                getDependency(TemplateResolver.class)));
        addDependency(new StagedBuilderWithMethodDefiniationCreatorFragment(getDependency(PreferencesManager.class),
                getDependency(BuilderMethodNameBuilder.class),
                getDependency(MarkerAnnotationAttacher.class),
                getDependency(StagedBuilderInterfaceNameProvider.class),
                getDependency(WithMethodParameterCreatorFragment.class)));
        addDependency(new StagedBuilderInterfaceTypeDefinitionCreatorFragment(getDependency(JavadocAdder.class)));
        addDependency(new StagedBuilderInterfaceCreatorFragment(getDependency(StagedBuilderWithMethodDefiniationCreatorFragment.class),
                getDependency(StagedBuilderInterfaceTypeDefinitionCreatorFragment.class),
                getDependency(StagedBuilderInterfaceTypeDefinitionCreatorFragment.class),
                getDependency(BuildMethodDeclarationCreatorFragment.class),
                getDependency(JavadocAdder.class),
                getDependency(GeneratedAnnotationPopulator.class)));
        addDependency(new StagedBuilderCreationBuilderMethodAdder(getDependency(BlockWithNewBuilderCreationFragment.class),
                getDependency(BuilderMethodDefinitionCreatorFragment.class)));
        addDependency(new NewBuilderAndWithMethodCallCreationFragment());
        addDependency(new StagedBuilderCreationWithMethodAdder(getDependency(StagedBuilderWithMethodDefiniationCreatorFragment.class),
                getDependency(NewBuilderAndWithMethodCallCreationFragment.class), getDependency(JavadocAdder.class)));
        addDependency(new StagedBuilderStaticBuilderCreatorMethodCreator(getDependency(StagedBuilderCreationBuilderMethodAdder.class),
                getDependency(StagedBuilderCreationWithMethodAdder.class),
                getDependency(PreferencesManager.class)));
        addDependency(new StagedBuilderWithMethodAdderFragment(
                getDependency(StagedBuilderWithMethodDefiniationCreatorFragment.class),
                getDependency(MarkerAnnotationAttacher.class)));
        addDependency(new InterfaceSetter());
        addDependency(new StagedBuilderClassCreator(getDependency(PrivateConstructorAdderFragment.class),
                getDependency(EmptyBuilderClassGeneratorFragment.class),
                getDependency(BuildMethodCreatorFragment.class),
                getDependency(BuilderFieldAdderFragment.class),
                getDependency(StagedBuilderWithMethodAdderFragment.class),
                getDependency(InterfaceSetter.class),
                getDependency(MarkerAnnotationAttacher.class)));
        addDependency(new StagedBuilderStagePropertyInputDialogOpener(getDependency(CurrentShellProvider.class)));
        addDependency(new StagedBuilderStagePropertiesProvider(getDependency(StagedBuilderInterfaceNameProvider.class),
                getDependency(StagedBuilderStagePropertyInputDialogOpener.class)));
        addDependency(new StagedBuilderCompilationUnitGenerator(getDependency(StagedBuilderClassCreator.class),
                getDependency(PrivateInitializingConstructorCreator.class),
                getDependency(StagedBuilderStaticBuilderCreatorMethodCreator.class),
                getDependency(ImportPopulator.class),
                getDependency(StagedBuilderInterfaceCreatorFragment.class),
                getDependency(BuilderRemover.class),
                getDependency(GlobalBuilderPostProcessor.class)));
        addDependency(new StagedBuilderCompilationUnitGeneratorFieldCollectorDecorator(
                getDependency(StagedBuilderCompilationUnitGenerator.class),
                getDependency(ApplicableBuilderFieldExtractor.class),
                getDependency(StagedBuilderStagePropertiesProvider.class)));

        // Generator chain
        addDependency(new GenerateBuilderExecutorImpl(getDependency(CompilationUnitParser.class),
                getDependencyList(BuilderCompilationUnitGenerator.class),
                getDependency(IsEventOnJavaFilePredicate.class), getDependency(WorkingCopyManagerWrapper.class),
                getDependency(CompilationUnitSourceSetter.class),
                getDependency(ErrorHandlerHook.class),
                getDependency(BuilderOwnerClassFinder.class)));
        addDependency(new GenerateBuilderHandlerErrorHandlerDecorator(getDependency(GenerateBuilderExecutorImpl.class),
                getDependency(ErrorHandlerHook.class)));
        addDependency(new StatefulBeanHandler(getDependency(PreferenceStoreProvider.class),
                getDependency(WorkingCopyManagerWrapper.class), getDependency(ImportRepository.class)));
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

package com.helospark.spark.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.helospark.spark.converter.handlers.DefaultCompilationUnitProvider;
import com.helospark.spark.converter.handlers.InputParameterGetter;
import com.helospark.spark.converter.handlers.service.collector.MethodCollector;
import com.helospark.spark.converter.handlers.service.collector.collectors.ConvertMethodCollector;
import com.helospark.spark.converter.handlers.service.collector.collectors.ListMethodCollector;
import com.helospark.spark.converter.handlers.service.collector.collectors.MethodCollectorChain;
import com.helospark.spark.converter.handlers.service.collector.collectors.OptionalMethodCollector;
import com.helospark.spark.converter.handlers.service.collector.collectors.helper.ConverterMethodLocator;
import com.helospark.spark.converter.handlers.service.collector.converttype.ConvertTypeFinder;
import com.helospark.spark.converter.handlers.service.collector.converttype.ConvertableDomainBuilderChainItem;
import com.helospark.spark.converter.handlers.service.collector.converttype.impl.CollectionConvertTypeBuilder;
import com.helospark.spark.converter.handlers.service.collector.converttype.impl.ConverterRequiredConvertTypeBuilder;
import com.helospark.spark.converter.handlers.service.collector.converttype.impl.OptionalConvertConvertTypeBuilder;
import com.helospark.spark.converter.handlers.service.collector.converttype.impl.OptionalUnwrapConvertTypeBuilder;
import com.helospark.spark.converter.handlers.service.collector.converttype.impl.OptionalWrapConvertTypeBuilder;
import com.helospark.spark.converter.handlers.service.collector.converttype.impl.ReferenceCopyConvertTypeBuilder;
import com.helospark.spark.converter.handlers.service.common.ClassNameToVariableNameConverter;
import com.helospark.spark.converter.handlers.service.common.ConvertableParametersGenerator;
import com.helospark.spark.converter.handlers.service.common.ImportPopulator;
import com.helospark.spark.converter.handlers.service.emitter.CodeEmitter;
import com.helospark.spark.converter.handlers.service.emitter.UnitTestCodeEmitter;
import com.helospark.spark.converter.handlers.service.emitter.helper.AstHelper;
import com.helospark.spark.converter.handlers.service.emitter.helper.ClassTypeAppender;
import com.helospark.spark.converter.handlers.service.emitter.helper.CompilationUnitCreator;
import com.helospark.spark.converter.handlers.service.emitter.helper.CompilationUnitParser;
import com.helospark.spark.converter.handlers.service.emitter.helper.ConverterConstructorEmitter;
import com.helospark.spark.converter.handlers.service.emitter.helper.ConverterFieldEmitter;
import com.helospark.spark.converter.handlers.service.emitter.helper.ImportAppender;
import com.helospark.spark.converter.handlers.service.emitter.helper.MethodsEmitter;
import com.helospark.spark.converter.handlers.service.emitter.helper.ModifiableCompilationUnitCreator;
import com.helospark.spark.converter.handlers.service.emitter.helper.PackageRootFinder;
import com.helospark.spark.converter.handlers.service.emitter.helper.SignatureToTypeResolver;
import com.helospark.spark.converter.handlers.service.emitter.helper.TypeDeclarationGenerator;
import com.helospark.spark.converter.handlers.service.emitter.methodemitter.MethodEmitter;
import com.helospark.spark.converter.handlers.service.emitter.methodemitter.impl.CollectionConvertMethodEmitter;
import com.helospark.spark.converter.handlers.service.emitter.methodemitter.impl.OptionalConvertMethodEmitter;
import com.helospark.spark.converter.handlers.service.emitter.methodemitter.impl.RegularConvertMethodEmitter;
import com.helospark.spark.converter.handlers.service.emitter.methodemitter.impl.helper.ConverterConvertMethodGenerator;
import com.helospark.spark.converter.handlers.service.emitter.parametercodegenerator.ParameterConvertCodeGenerator;
import com.helospark.spark.converter.handlers.service.emitter.parametercodegenerator.impl.ConvertCopyCodeGenerator;
import com.helospark.spark.converter.handlers.service.emitter.parametercodegenerator.impl.NoDestinationCodeGenerator;
import com.helospark.spark.converter.handlers.service.emitter.parametercodegenerator.impl.NoSourceCodeGenerator;
import com.helospark.spark.converter.handlers.service.emitter.parametercodegenerator.impl.OptionalUnwrapCodeGenerator;
import com.helospark.spark.converter.handlers.service.emitter.parametercodegenerator.impl.OptionalWrapCodeGenerator;
import com.helospark.spark.converter.handlers.service.emitter.parametercodegenerator.impl.ReferenceCopyCodeGenerator;
import com.helospark.spark.converter.handlers.service.emitter.parametercodegenerator.impl.helper.CallableMethodGenerator;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {
    public static List<Object> diContainer = new ArrayList<>();
    // The plug-in ID
    public static final String PLUGIN_ID = "com.helospark.SparkConverterGenerator";

    // The shared instance
    private static Activator plugin;

    /**
     * The constructor
     */
    public Activator() {
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        initializeDiContainer();
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.
     * BundleContext)
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in
     * relative path
     *
     * @param path
     *            the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    private void initializeDiContainer() {
        diContainer.add(new DefaultCompilationUnitProvider());
        diContainer.add(new InputParameterGetter(getDependency(DefaultCompilationUnitProvider.class)));
        diContainer.add(new PackageRootFinder());
        diContainer.add(new ClassNameToVariableNameConverter());
        diContainer.add(new CompilationUnitCreator());
        diContainer.add(new ImportPopulator());
        diContainer.add(new CompilationUnitParser());
        diContainer.add(new ClassTypeAppender());
        diContainer.add(new SignatureToTypeResolver());

        diContainer.add(new ReferenceCopyCodeGenerator());
        diContainer.add(new OptionalWrapCodeGenerator(getDependency(ImportPopulator.class)));
        diContainer.add(new OptionalUnwrapCodeGenerator(getDependency(ImportPopulator.class)));
        diContainer.add(new TypeDeclarationGenerator());

        List<ConvertableDomainBuilderChainItem> convertableDomainBuilderChain = Arrays.asList(
                new ReferenceCopyConvertTypeBuilder(),
                new OptionalUnwrapConvertTypeBuilder(),
                new OptionalWrapConvertTypeBuilder(),
                new OptionalConvertConvertTypeBuilder(),
                new CollectionConvertTypeBuilder(),
                new ConverterRequiredConvertTypeBuilder());

        diContainer.add(new ConvertTypeFinder(convertableDomainBuilderChain));
        diContainer.add(new ConverterMethodLocator());
        diContainer.add(new CallableMethodGenerator(getDependency(ConverterMethodLocator.class)));

        diContainer.add(new ConvertCopyCodeGenerator(getDependency(CallableMethodGenerator.class)));
        diContainer.add(new NoDestinationCodeGenerator());
        diContainer.add(new NoSourceCodeGenerator());

        diContainer.add(new ConvertMethodCollector(getDependency(ConverterMethodLocator.class), getDependency(ClassNameToVariableNameConverter.class)));
        diContainer.add(new ConvertableParametersGenerator(getDependency(SignatureToTypeResolver.class), getDependency(ConvertTypeFinder.class)));
        com.helospark.spark.converter.handlers.service.collector.MethodCollector methodCollector = new com.helospark.spark.converter.handlers.service.collector.MethodCollector(
                getDependency(ConvertableParametersGenerator.class), getDependency(ConvertTypeFinder.class));
        diContainer.add(new ListMethodCollector(getDependency(ConverterMethodLocator.class), methodCollector, getDependency(ClassNameToVariableNameConverter.class)));
        diContainer.add(new OptionalMethodCollector(getDependency(ConverterMethodLocator.class), methodCollector, getDependency(ClassNameToVariableNameConverter.class)));
        methodCollector.setMethodCollectorChain(getDependencyList(MethodCollectorChain.class));
        diContainer.add(methodCollector);
        diContainer.add(new ConverterConstructorEmitter());
        diContainer.add(new PackageRootFinder());
        diContainer.add(new CompilationUnitCreator());
        diContainer.add(new CompilationUnitParser());
        diContainer.add(new AstHelper());
        diContainer.add(new ModifiableCompilationUnitCreator(getDependency(CompilationUnitParser.class), getDependency(CompilationUnitCreator.class)));
        diContainer.add(new MethodCollector(getDependency(ConvertableParametersGenerator.class), getDependency(ConvertTypeFinder.class)));
        diContainer.add(new ConvertableParametersGenerator(getDependency(SignatureToTypeResolver.class), getDependency(ConvertTypeFinder.class)));
        diContainer.add(new ConverterConvertMethodGenerator(getDependencyList(ParameterConvertCodeGenerator.class), getDependency(ImportPopulator.class)));

        // Method emitters---
        diContainer.add(new RegularConvertMethodEmitter(getDependency(ConvertableParametersGenerator.class), getDependency(ConverterConvertMethodGenerator.class)));
        diContainer.add(new CollectionConvertMethodEmitter(getDependency(ImportPopulator.class), getDependency(ClassNameToVariableNameConverter.class),
                getDependency(CallableMethodGenerator.class), getDependency(AstHelper.class)));
        diContainer.add(new OptionalConvertMethodEmitter(getDependency(ImportPopulator.class), getDependency(CallableMethodGenerator.class), getDependency(AstHelper.class)));
        // ---

        diContainer.add(new MethodsEmitter(getDependencyList(MethodEmitter.class)));
        diContainer.add(new ConverterFieldEmitter(getDependency(ImportPopulator.class)));
        diContainer.add(new ImportAppender());
        diContainer.add(new CodeEmitter(getDependency(TypeDeclarationGenerator.class),
                getDependency(ClassTypeAppender.class),
                getDependency(ModifiableCompilationUnitCreator.class),
                getDependency(PackageRootFinder.class),
                getDependency(MethodsEmitter.class),
                getDependency(ConverterConstructorEmitter.class),
                getDependency(ConverterFieldEmitter.class),
                getDependency(ImportAppender.class)));
        diContainer.add(new UnitTestCodeEmitter(getDependency(TypeDeclarationGenerator.class),
                getDependency(ClassTypeAppender.class),
                getDependency(ModifiableCompilationUnitCreator.class),
                getDependency(PackageRootFinder.class)));
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
                .orElseThrow(() -> new RuntimeException("Unable to initialize " + clazz.getName() + " not found"));
    }

    private <T> List<T> getDependencyList(Class<T> classToFind) {
        List<Object> result = new ArrayList<>();
        for (Object o : diContainer) {
            if (classToFind.isAssignableFrom(o.getClass())) {
                result.add(o);
            }
        }
        return (List<T>) result;
    }
}

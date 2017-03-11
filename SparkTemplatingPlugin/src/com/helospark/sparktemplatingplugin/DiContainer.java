package com.helospark.sparktemplatingplugin;

import java.util.ArrayList;
import java.util.List;

import com.helospark.sparktemplatingplugin.execute.templater.GlobalConfiguration;
import com.helospark.sparktemplatingplugin.execute.templater.ScriptExposed;
import com.helospark.sparktemplatingplugin.execute.templater.ScriptExposedObjectProvider;
import com.helospark.sparktemplatingplugin.execute.templater.ScriptExposedProvider;
import com.helospark.sparktemplatingplugin.execute.templater.ScriptInterpreter;
import com.helospark.sparktemplatingplugin.execute.templater.ScriptPreProcessor;
import com.helospark.sparktemplatingplugin.execute.templater.Templater;
import com.helospark.sparktemplatingplugin.execute.templater.TemplatingResultFactory;
import com.helospark.sparktemplatingplugin.execute.templater.helper.CompilationUnitCreator;
import com.helospark.sparktemplatingplugin.execute.templater.helper.PackageRootFinder;
import com.helospark.sparktemplatingplugin.execute.templater.provider.CompilationUnitProvider;
import com.helospark.sparktemplatingplugin.execute.templater.provider.CurrentClassProvider;
import com.helospark.sparktemplatingplugin.execute.templater.provider.CurrentProjectProvider;
import com.helospark.sparktemplatingplugin.initializer.BundleInitializedHook;
import com.helospark.sparktemplatingplugin.initializer.examplescript.BundleClasspathFileLoader;
import com.helospark.sparktemplatingplugin.initializer.examplescript.ExampleScriptInitializer;
import com.helospark.sparktemplatingplugin.initializer.examplescript.ExampleScriptInitializerVersionFilteringDecorator;
import com.helospark.sparktemplatingplugin.initializer.examplescript.UniqueCommandNameFinder;
import com.helospark.sparktemplatingplugin.initializer.examplescript.xml.ExampleScriptXmlLoader;
import com.helospark.sparktemplatingplugin.initializer.examplescript.xml.ExampleScriptXmlParser;
import com.helospark.sparktemplatingplugin.initializer.examplescript.xml.XmlDocumentParser;
import com.helospark.sparktemplatingplugin.preferences.PreferenceStore;
import com.helospark.sparktemplatingplugin.repository.CommandNameToFilenameMapper;
import com.helospark.sparktemplatingplugin.repository.EclipseRootFolderProvider;
import com.helospark.sparktemplatingplugin.repository.FileSystemBackedScriptRepository;
import com.helospark.sparktemplatingplugin.repository.ScriptRepository;
import com.helospark.sparktemplatingplugin.repository.zip.ScriptUnzipper;
import com.helospark.sparktemplatingplugin.repository.zip.ScriptZipper;
import com.helospark.sparktemplatingplugin.scriptexport.job.ExportJobWorker;
import com.helospark.sparktemplatingplugin.scriptimport.job.ImportJobWorker;
import com.helospark.sparktemplatingplugin.support.BundleVersionProvider;
import com.helospark.sparktemplatingplugin.support.classpath.ClassInClasspathLocator;
import com.helospark.sparktemplatingplugin.support.fileoperation.FileContentReader;
import com.helospark.sparktemplatingplugin.support.fileoperation.FileOutputWriter;
import com.helospark.sparktemplatingplugin.ui.dialog.DialogUiHandler;
import com.helospark.sparktemplatingplugin.ui.editor.DocumentationProvider;
import com.helospark.sparktemplatingplugin.ui.editor.cache.EditorCacheInitializer;
import com.helospark.sparktemplatingplugin.ui.editor.completition.CompletitionChain;
import com.helospark.sparktemplatingplugin.ui.editor.completition.ProposalToDocumentationConverter;
import com.helospark.sparktemplatingplugin.ui.editor.completition.TemplatingToolCompletionProcessor;
import com.helospark.sparktemplatingplugin.ui.editor.completition.chain.ImportedPackageClassCompletitionChainItem;
import com.helospark.sparktemplatingplugin.ui.editor.completition.chain.MethodCallCompletitionChainItem;
import com.helospark.sparktemplatingplugin.ui.editor.completition.chain.ScriptExposedObjectCompletitionChainItem;

public class DiContainer {
    private static List<Object> diContainer = new ArrayList<>();

    public static void clearDiContainer() {
        diContainer.clear();
    }

    public static void initializeDiContainer() {
        addDependency(new ScriptPreProcessor());
        addDependency(new GlobalConfiguration());
        addDependency(new CompilationUnitCreator());
        addDependency(new PackageRootFinder());
        addDependency(new CompilationUnitProvider());
        addDependency(new ClassInClasspathLocator());
        addDependency(
                new TemplatingResultFactory(getDependency(CompilationUnitProvider.class),
                        getDependency(CompilationUnitCreator.class), getDependency(PackageRootFinder.class)));
        addDependency(new CurrentProjectProvider(getDependency(CompilationUnitProvider.class)));
        addDependency(new CurrentClassProvider(getDependency(CompilationUnitProvider.class)));
        addDependency(new ScriptExposedObjectProvider(getDependency(TemplatingResultFactory.class),
                getDependencyList(ScriptExposed.class),
                getDependencyList(ScriptExposedProvider.class)));
        addDependency(new ScriptInterpreter(getDependency(ScriptExposedObjectProvider.class)));
        addDependency(new DocumentationProvider(getDependencyList(IDocumented.class)));
        addDependency(new ProposalToDocumentationConverter());
        addDependency(new MethodCallCompletitionChainItem(getDependency(ProposalToDocumentationConverter.class)));
        addDependency(new ScriptExposedObjectCompletitionChainItem(getDependency(ScriptExposedObjectProvider.class)));
        addDependency(new ImportedPackageClassCompletitionChainItem(getDependency(ClassInClasspathLocator.class)));
        addDependency(new TemplatingToolCompletionProcessor(getDependencyList(CompletitionChain.class)));
        addDependency(new DialogUiHandler());
        addDependency(new Templater(getDependency(ScriptPreProcessor.class), getDependency(ScriptInterpreter.class), getDependency(DialogUiHandler.class)));

        addDependency(new EclipseRootFolderProvider());
        addDependency(new CommandNameToFilenameMapper());
        addDependency(new ScriptZipper(getDependency(CommandNameToFilenameMapper.class)));
        addDependency(new ScriptUnzipper());
        addDependency(new FileSystemBackedScriptRepository(getDependency(EclipseRootFolderProvider.class),
                getDependency(CommandNameToFilenameMapper.class)));
        addDependency(new TemplatingEditorOpener(getDependency(ScriptRepository.class)));
        addDependency(new EditorCacheInitializer(getDependency(ClassInClasspathLocator.class)));
        addDependency(new BundleClasspathFileLoader());
        addDependency(new UniqueCommandNameFinder(getDependency(ScriptRepository.class)));
        addDependency(new FileContentReader());
        addDependency(new XmlDocumentParser());
        addDependency(new ExampleScriptXmlParser(getDependency(XmlDocumentParser.class), getDependency(BundleClasspathFileLoader.class)));
        addDependency(
                new ExampleScriptXmlLoader(getDependency(ExampleScriptXmlParser.class), getDependency(FileContentReader.class), getDependency(BundleClasspathFileLoader.class)));
        addDependency(new PreferenceStore());
        addDependency(
                new ExampleScriptInitializer(getDependency(ExampleScriptXmlLoader.class), getDependency(ScriptRepository.class), getDependency(UniqueCommandNameFinder.class),
                        getDependency(PreferenceStore.class)));
        addDependency(new BundleVersionProvider());
        addDependency(new ExampleScriptInitializerVersionFilteringDecorator(getDependency(ExampleScriptInitializer.class), getDependency(PreferenceStore.class),
                getDependency(BundleVersionProvider.class)));
        addDependency(new FileOutputWriter());
        addDependency(new ImportJobWorker(getDependency(ScriptRepository.class), getDependency(ScriptUnzipper.class), getDependency(FileContentReader.class)));
        addDependency(new ExportJobWorker(getDependency(ScriptRepository.class), getDependency(ScriptZipper.class), getDependency(FileOutputWriter.class)));
        addDependency(new BundleInitializedHook(getDependency(ExampleScriptInitializerVersionFilteringDecorator.class)));
    }

    // Visible for testing
    public static void addDependency(Object dependency) {
        boolean alreadyHasDependency = diContainer.stream()
                .filter(value -> isSameMockitoMockDependency(dependency.getClass().toString(), value.getClass().toString()))
                .findFirst()
                .isPresent();

        if (!alreadyHasDependency) {
            diContainer.add(dependency);
        } else {
            System.out.println("[INFO] Skipping " + dependency + " because diContainer already contains it");
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

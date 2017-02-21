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
import com.helospark.sparktemplatingplugin.repository.CommandNameToFilenameMapper;
import com.helospark.sparktemplatingplugin.repository.EclipseRootFolderProvider;
import com.helospark.sparktemplatingplugin.repository.FileSystemBackedScriptRepository;
import com.helospark.sparktemplatingplugin.repository.ScriptRepository;
import com.helospark.sparktemplatingplugin.repository.zip.ScriptUnzipper;
import com.helospark.sparktemplatingplugin.repository.zip.ScriptZipper;
import com.helospark.sparktemplatingplugin.ui.editor.DocumentationProvider;
import com.helospark.sparktemplatingplugin.ui.editor.completition.CompletitionChain;
import com.helospark.sparktemplatingplugin.ui.editor.completition.ProposalToDocumentationConverter;
import com.helospark.sparktemplatingplugin.ui.editor.completition.TemplatingToolCompletionProcessor;
import com.helospark.sparktemplatingplugin.ui.editor.completition.chain.MethodCallCompletitionChainItem;
import com.helospark.sparktemplatingplugin.ui.editor.completition.chain.ScriptExposedObjectCompletitionChainItem;

public class DiContainer {
    private static List<Object> diContainer = new ArrayList<>();

    public static void initializeDiContainer() {
        diContainer.add(new ScriptPreProcessor());
        diContainer.add(new GlobalConfiguration());
        diContainer.add(new CompilationUnitCreator());
        diContainer.add(new PackageRootFinder());
        diContainer.add(new CompilationUnitProvider());
        diContainer.add(
                new TemplatingResultFactory(getDependency(CompilationUnitProvider.class), getDependency(CompilationUnitCreator.class), getDependency(PackageRootFinder.class)));
        diContainer.add(new CurrentProjectProvider());
        diContainer.add(new CurrentClassProvider(getDependency(CompilationUnitProvider.class)));
        diContainer.add(new ScriptExposedObjectProvider(getDependency(TemplatingResultFactory.class),
                getDependencyList(ScriptExposed.class),
                getDependencyList(ScriptExposedProvider.class)));
        diContainer.add(new ScriptInterpreter(getDependency(ScriptExposedObjectProvider.class)));
        diContainer.add(new DocumentationProvider(getDependencyList(IDocumented.class)));
        diContainer.add(new ProposalToDocumentationConverter());
        diContainer.add(new MethodCallCompletitionChainItem(getDependency(ProposalToDocumentationConverter.class)));
        diContainer.add(new ScriptExposedObjectCompletitionChainItem(getDependency(ScriptExposedObjectProvider.class)));
        diContainer.add(new TemplatingToolCompletionProcessor(getDependencyList(CompletitionChain.class)));
        diContainer.add(new Templater(getDependency(ScriptPreProcessor.class), getDependency(ScriptInterpreter.class)));

        diContainer.add(new EclipseRootFolderProvider());
        diContainer.add(new CommandNameToFilenameMapper());
        diContainer.add(new ScriptZipper(getDependency(CommandNameToFilenameMapper.class)));
        diContainer.add(new ScriptUnzipper());
        diContainer.add(new FileSystemBackedScriptRepository(getDependency(EclipseRootFolderProvider.class), getDependency(CommandNameToFilenameMapper.class)));
        diContainer.add(new TemplatingEditorOpener(getDependency(ScriptRepository.class)));
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

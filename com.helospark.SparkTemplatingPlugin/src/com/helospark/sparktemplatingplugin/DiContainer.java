package com.helospark.sparktemplatingplugin;

import java.util.ArrayList;
import java.util.List;

import com.helospark.sparktemplatingplugin.handlers.templater.GlobalConfiguration;
import com.helospark.sparktemplatingplugin.handlers.templater.ScriptExposed;
import com.helospark.sparktemplatingplugin.handlers.templater.ScriptExposedProvider;
import com.helospark.sparktemplatingplugin.handlers.templater.ScriptInterpreter;
import com.helospark.sparktemplatingplugin.handlers.templater.ScriptPreProcessor;
import com.helospark.sparktemplatingplugin.handlers.templater.Templater;
import com.helospark.sparktemplatingplugin.handlers.templater.TemplatingResultFactory;
import com.helospark.sparktemplatingplugin.handlers.templater.helper.CompilationUnitCreator;
import com.helospark.sparktemplatingplugin.handlers.templater.helper.PackageRootFinder;
import com.helospark.sparktemplatingplugin.handlers.templater.provider.CompilationUnitProvider;
import com.helospark.sparktemplatingplugin.handlers.templater.provider.CurrentProjectProvider;

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
        diContainer.add(new ScriptInterpreter(getDependency(TemplatingResultFactory.class),
                getDependencyList(ScriptExposed.class),
                getDependencyList(ScriptExposedProvider.class)));
        diContainer.add(new Templater(getDependency(ScriptPreProcessor.class), getDependency(ScriptInterpreter.class)));
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

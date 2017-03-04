package com.helospark.sparktemplatingplugin.support.classpath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

public class ClassInClasspathLocator {
    private Map<String, List<Class<?>>> classCache = new ConcurrentHashMap<>();

    public void preInitializeCache(List<String> basePackages) {
        for (String basePackage : basePackages) {
            getCachedValueOrCompute(basePackage);
        }
    }

    public List<Class<?>> findClassesByName(List<String> basePackages, String name) {
        return findClassesInPackageFilteredBy(basePackages, clazz -> clazz.getSimpleName().equals(name));
    }

    public List<Class<?>> getAllClassesInPackages(List<String> basePackages) {
        return findClassesInPackageFilteredBy(basePackages, clazz -> true);
    }

    public List<Class<?>> findClassesByFullyQualifiedName(List<String> basePackages, String fullyQualifiedName) {
        return findClassesInPackageFilteredBy(basePackages, clazz -> clazz.getName().equals(fullyQualifiedName));
    }

    public List<Class<?>> findClassesByRegex(List<String> basePackages, String regex) {
        return findClassesInPackageFilteredBy(basePackages, clazz -> clazz.getName().matches(regex));
    }

    private List<Class<?>> findClassesInPackageFilteredBy(List<String> basePackages, Predicate<Class<?>> predicate) {
        return basePackages.stream()
                .flatMap(basePackage -> getCachedValueOrCompute(basePackage).stream())
                .filter(predicate)
                .collect(Collectors.toList());
    }

    private List<Class<?>> getCachedValueOrCompute(String basePackage) {
        return classCache.computeIfAbsent(basePackage, bPackage -> loadClasses(removeTrailingWildcardIfPresent(bPackage)));
    }

    private String removeTrailingWildcardIfPresent(String bPackage) {
        return bPackage.replaceAll("\\.\\*$", "");
    }

    private List<Class<?>> loadClasses(String basePackage) {
        List<Class<?>> result = new ArrayList<>();
        try {
            new FastClasspathScanner(basePackage)
                    .overrideClassLoaders(this.getClass().getClassLoader())
                    .matchAllClasses(clazz -> {
                        if (clazz.getPackage().getName().equals(basePackage)) {
                            result.add(clazz);
                        }
                    })
                    .scan();
        } catch (Exception e) {
            System.out.println("[WARNING] " + e.getMessage());
        }
        return result;
    }

}

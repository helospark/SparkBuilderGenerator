package com.helospark.spark.builder.handlers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Stateful bean to contain required import during the current builder's generation.
 * Note: Since it is stateful, use with care.
 * @author helospark
 */
public class ImportRepository implements StatefulBean {
    private Set<String> imports = new ConcurrentSkipListSet<>();

    @Override
    public void clearState() {
        imports.clear();
    }

    public void addImport(String importToAdd) {
        imports.add(importToAdd);
    }

    public void addImports(Collection<String> importsToAdd) {
        imports.addAll(importsToAdd);
    }

    public Set<String> queryImports() {
        return new HashSet<>(imports);
    }
}

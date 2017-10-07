package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.field.chain;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.INITIALIZE_COLLECTIONS_TO_EMPTY_COLLECTIONS;
import static com.helospark.spark.builder.preferences.StaticPreferences.COLLECTIONS_CLASS_FULLY_QUALIFIED_NAME;
import static com.helospark.spark.builder.preferences.StaticPreferences.COLLECTIONS_CLASS_NAME;
import static java.util.Collections.singletonList;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;

import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.field.StaticMethodInvocationFragment;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Initializes empty collections using the java.util.Collections initializers.
 * @author helospark
 */
public class BuiltInCollectionsInitializerChainitem implements FieldDeclarationPostProcessorChainItem {
    private StaticMethodInvocationFragment staticMethodInvocationFragment;
    private PreferencesManager preferencesManager;
    private Map<String, String> fullyQualifiedNameToCollectionsMethodNameMapping;

    public BuiltInCollectionsInitializerChainitem(StaticMethodInvocationFragment staticMethodInvocationFragment, PreferencesManager preferencesManager) {
        this.staticMethodInvocationFragment = staticMethodInvocationFragment;
        this.preferencesManager = preferencesManager;
        fullyQualifiedNameToCollectionsMethodNameMapping = new HashMap<>();
        initializeMapping();
    }

    // TODO: this should come from a configuration class
    // when LightDi introduce, move these
    private void initializeMapping() {
        // Method names are in the Collections class
        fullyQualifiedNameToCollectionsMethodNameMapping.put(List.class.getName(), "emptyList");
        fullyQualifiedNameToCollectionsMethodNameMapping.put(Set.class.getName(), "emptySet");
        fullyQualifiedNameToCollectionsMethodNameMapping.put(Map.class.getName(), "emptyMap");
        fullyQualifiedNameToCollectionsMethodNameMapping.put(SortedSet.class.getName(), "emptySortedSet");
        fullyQualifiedNameToCollectionsMethodNameMapping.put(SortedMap.class.getName(), "emptySortedMap");
        fullyQualifiedNameToCollectionsMethodNameMapping.put(Collection.class.getName(), "emptyList");
        fullyQualifiedNameToCollectionsMethodNameMapping.put(Iterable.class.getName(), "emptyList");
    }

    @Override
    public boolean doesSupport(String fullyQualifiedName) {
        return fullyQualifiedNameToCollectionsMethodNameMapping.containsKey(fullyQualifiedName) &&
                preferencesManager.getPreferenceValue(INITIALIZE_COLLECTIONS_TO_EMPTY_COLLECTIONS);
    }

    @Override
    public Expression createExpression(AST ast, String fullyQualifiedName) {
        String result = fullyQualifiedNameToCollectionsMethodNameMapping.get(fullyQualifiedName);
        return staticMethodInvocationFragment.createStaticMethodInvocation(ast, COLLECTIONS_CLASS_NAME, result);
    }

    @Override
    public List<String> getImport(String fullyQualifiedName) {
        return singletonList(COLLECTIONS_CLASS_FULLY_QUALIFIED_NAME);
    }

}

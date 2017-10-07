package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.field;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.INITIALIZE_COLLECTIONS_TO_EMPTY_COLLECTIONS;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.INITIALIZE_OPTIONAL_FIELDS_TO_EMPTY;

import java.util.List;
import java.util.Optional;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.helospark.spark.builder.handlers.ImportRepository;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.field.chain.FieldDeclarationPostProcessorChainItem;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Post process field declaration in a builder.
 * Adds field initialization.
 * @author helospark
 */
public class FieldDeclarationPostProcessor {
    private PreferencesManager preferencesManager;
    private FullyQualifiedNameExtractor fullyQualifiedNameExtractor;
    private ImportRepository importRepository;
    private List<FieldDeclarationPostProcessorChainItem> fieldPostProcessorChain;

    public FieldDeclarationPostProcessor(PreferencesManager preferencesManager, FullyQualifiedNameExtractor fullyQualifiedNameExtractor,
            ImportRepository importRepository, List<FieldDeclarationPostProcessorChainItem> fullyQualifiedNameToExpression) {
        this.preferencesManager = preferencesManager;
        this.fullyQualifiedNameExtractor = fullyQualifiedNameExtractor;
        this.fieldPostProcessorChain = fullyQualifiedNameToExpression;
        this.importRepository = importRepository;
    }

    public VariableDeclarationFragment postProcessFragment(AST ast, FieldDeclaration originalFieldDeclaration, VariableDeclarationFragment variableDeclarationFragment) {
        if (isPostProcessingRequired()) {
            Optional<String> result = fullyQualifiedNameExtractor.getFullyQualifiedBaseTypeName(originalFieldDeclaration);
            if (result.isPresent()) {
                postProcessDeclaration(ast, variableDeclarationFragment, result.get());
            }
        }
        return variableDeclarationFragment;
    }

    private Boolean isPostProcessingRequired() {
        return preferencesManager.getPreferenceValue(INITIALIZE_OPTIONAL_FIELDS_TO_EMPTY)
                || preferencesManager.getPreferenceValue(INITIALIZE_COLLECTIONS_TO_EMPTY_COLLECTIONS);
    }

    private void postProcessDeclaration(AST ast, VariableDeclarationFragment variableDeclarationFragment, String fullyQualifiedName) {
        fieldPostProcessorChain.stream()
                .filter(chainItem -> chainItem.doesSupport(fullyQualifiedName))
                .findFirst()
                .ifPresent(chainItem -> postProcessUsingChainItem(ast, chainItem, variableDeclarationFragment, fullyQualifiedName));

    }

    private void postProcessUsingChainItem(AST ast, FieldDeclarationPostProcessorChainItem chainItemToUse, VariableDeclarationFragment variableDeclarationFragment,
            String fullyQualifiedName) {
        Expression expression = chainItemToUse.createExpression(ast, fullyQualifiedName);
        variableDeclarationFragment.setInitializer(expression);
        importRepository.addImports(chainItemToUse.getImport(fullyQualifiedName));
    }
}
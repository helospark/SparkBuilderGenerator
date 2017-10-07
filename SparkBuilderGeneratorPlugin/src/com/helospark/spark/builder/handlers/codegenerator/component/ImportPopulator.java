package com.helospark.spark.builder.handlers.codegenerator.component;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.ADD_GENERATED_ANNOTATION;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.ADD_NONNULL_ON_PARAMETERS;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.ADD_NONNULL_ON_RETURN;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import com.helospark.spark.builder.handlers.ImportRepository;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Add imports.
 *
 * @author helospark
 */
public class ImportPopulator {
    private static final String GENERATED_FULLY_QUALIFIED_NAME = "javax.annotation.Generated";
    private static final String NON_NULL_FULLY_QUALIFIED_NAME = "javax.annotation.Nonnull";
    private PreferencesManager preferencesManager;
    private ImportRepository importRepository;

    public ImportPopulator(PreferencesManager preferencesManager, ImportRepository importRepository) {
        this.preferencesManager = preferencesManager;
        this.importRepository = importRepository;
    }

    public void populateImports(CompilationUnitModificationDomain compilationUnitModificationDomain) {
        AST ast = compilationUnitModificationDomain.getAst();
        ASTRewrite rewriter = compilationUnitModificationDomain.getAstRewriter();
        CompilationUnit compilationUnit = compilationUnitModificationDomain.getCompilationUnit();
        ListRewrite importRewrite = rewriter.getListRewrite(compilationUnit, CompilationUnit.IMPORTS_PROPERTY);

        if (shouldAddNonnullAnnotation(compilationUnit)) {
            addImport(ast, importRewrite, NON_NULL_FULLY_QUALIFIED_NAME);
        }
        if (shouldAddGeneratedAnnotation(compilationUnit)) {
            addImport(ast, importRewrite, GENERATED_FULLY_QUALIFIED_NAME);
        }
        addImportsFromRepository(ast, importRewrite, compilationUnit);
    }

    private boolean shouldAddNonnullAnnotation(CompilationUnit compilationUnit) {
        return !isImportExists(compilationUnit, NON_NULL_FULLY_QUALIFIED_NAME) && (preferencesManager.getPreferenceValue(ADD_NONNULL_ON_PARAMETERS)
                || preferencesManager.getPreferenceValue(ADD_NONNULL_ON_RETURN));
    }

    private boolean shouldAddGeneratedAnnotation(CompilationUnit compilationUnit) {
        return !isImportExists(compilationUnit, GENERATED_FULLY_QUALIFIED_NAME) && preferencesManager.getPreferenceValue(ADD_GENERATED_ANNOTATION);
    }

    private void addImportsFromRepository(AST ast, ListRewrite importRewrite, CompilationUnit compilationUnit) {
        importRepository.queryImports()
                .stream()
                .filter(importName -> !isImportExists(compilationUnit, importName))
                .forEach(importName -> addImport(ast, importRewrite, importName));
    }

    private boolean isImportExists(CompilationUnit compilationUnit, String generatedFullyQualifiedName) {
        return ((List<ImportDeclaration>) compilationUnit.imports())
                .stream()
                .filter(importDeclaration -> importDeclaration.getName().toString().equals(generatedFullyQualifiedName))
                .findFirst()
                .map(found -> Boolean.TRUE)
                .orElse(Boolean.FALSE);
    }

    private void addImport(AST ast, ListRewrite importRewrite, String fullyQualifierImport) {
        ImportDeclaration importDeclaration = ast.newImportDeclaration();
        importDeclaration.setName(ast.newName(fullyQualifierImport));
        importRewrite.insertLast(importDeclaration, null);
    }

}

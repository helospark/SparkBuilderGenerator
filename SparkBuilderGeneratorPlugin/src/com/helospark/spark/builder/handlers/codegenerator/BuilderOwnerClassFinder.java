package com.helospark.spark.builder.handlers.codegenerator;

import static com.helospark.spark.builder.handlers.codegenerator.component.helper.IsRecordTypePredicate.isRecordDeclaration;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.ALWAYS_GENERATE_BUILDER_TO_FIRST_CLASS;
import static java.util.Optional.ofNullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import com.helospark.spark.builder.PluginLogger;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.CurrentlySelectedApplicableClassesClassNameProvider;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.RecordDeclarationWrapper;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TypeExtractor;
import com.helospark.spark.builder.handlers.codegenerator.component.remover.helper.GeneratedAnnotationPredicate;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;
import com.helospark.spark.builder.handlers.exception.PluginException;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Provides the owner class from the from the given compilation unit.
 * @author helospark
 */
public class BuilderOwnerClassFinder {
    private CurrentlySelectedApplicableClassesClassNameProvider currentlySelectedApplicableClassesClassNameProvider;
    private PreferencesManager preferencesManager;
    private GeneratedAnnotationPredicate generatedAnnotationPredicate;
    private PluginLogger pluginLogger;

    public BuilderOwnerClassFinder(CurrentlySelectedApplicableClassesClassNameProvider currentlySelectedApplicableClassesClassNameProvider, PreferencesManager preferencesManager,
            GeneratedAnnotationPredicate generatedAnnotationPredicate) {
        this.currentlySelectedApplicableClassesClassNameProvider = currentlySelectedApplicableClassesClassNameProvider;
        this.preferencesManager = preferencesManager;
        this.generatedAnnotationPredicate = generatedAnnotationPredicate;
        this.pluginLogger = new PluginLogger();
    }

    public CompilationUnitModificationDomain provideBuilderOwnerClass(CompilationUnit compilationUnit, AST ast, ASTRewrite rewriter, ICompilationUnit iCompilationUnit) {
        AbstractTypeDeclaration builderType = getBuilderOwnerType(compilationUnit, iCompilationUnit);

        ListRewrite listRewrite = getListRewrite(rewriter, builderType);

        return CompilationUnitModificationDomain.builder()
                .withAst(ast)
                .withAstRewriter(rewriter)
                .withListRewrite(listRewrite)
                .withOriginalType(builderType)
                .withCompilationUnit(compilationUnit)
                .build();
    }

    private ListRewrite getListRewrite(ASTRewrite rewriter, AbstractTypeDeclaration builderType) {
        if (builderType.getClass() == TypeDeclaration.class) {
            return rewriter.getListRewrite((TypeDeclaration) builderType, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
        } else {
            return rewriter.getListRewrite(builderType, RecordDeclarationWrapper.of(builderType).getBodyDeclarationProperty());
        }
    }

    private AbstractTypeDeclaration getBuilderOwnerType(CompilationUnit compilationUnit, ICompilationUnit iCompilationUnit) {
        if (preferencesManager.getPreferenceValue(ALWAYS_GENERATE_BUILDER_TO_FIRST_CLASS)) {
            return getFirstType(compilationUnit);
        } else {
            return getTypeAtCurrentSelection(iCompilationUnit, compilationUnit)
                    .orElse(getFirstType(compilationUnit));
        }
    }

    private Optional<AbstractTypeDeclaration> getTypeAtCurrentSelection(ICompilationUnit iCompilationUnit, CompilationUnit compilationUnit) {
        try {
            Optional<String> className = currentlySelectedApplicableClassesClassNameProvider.provideCurrentlySelectedClassName(iCompilationUnit);
            Optional<AbstractTypeDeclaration> foundType = className.flatMap(internalClassName -> findClassWithClassName(compilationUnit, internalClassName));
            if (foundType.isPresent()) {
                foundType = ofNullable(postProcessFoundType(foundType.get()));
            }
            return foundType;
        } catch (Exception e) {
            pluginLogger.warn("Cannot extract currently selected class based on offset", e);
            return Optional.empty();
        }
    }

    private AbstractTypeDeclaration postProcessFoundType(AbstractTypeDeclaration currentTypeDeclaration) {
        if (doesTypeHasGeneratedAnnotation(currentTypeDeclaration)) {
            return extractParentTypeDeclarationOrNull(currentTypeDeclaration);
        } else {
            return currentTypeDeclaration;
        }
    }

    private AbstractTypeDeclaration extractParentTypeDeclarationOrNull(AbstractTypeDeclaration typeDeclaration) {
        ASTNode result = typeDeclaration.getParent();
        while (result != null && !(result instanceof TypeDeclaration)) {
            result = result.getParent();
        }
        return (AbstractTypeDeclaration) result;
    }

    private boolean doesTypeHasGeneratedAnnotation(AbstractTypeDeclaration typeDeclaration) {
        return generatedAnnotationPredicate.test(typeDeclaration);
    }

    private Optional<AbstractTypeDeclaration> findClassWithClassName(CompilationUnit compilationUnit, String className) {
        return ((List<AbstractTypeDeclaration>) compilationUnit.types())
                .stream()
                .filter(abstractTypeDeclaration -> abstractTypeDeclaration instanceof TypeDeclaration || isRecordDeclaration(abstractTypeDeclaration))
                .map(type -> recursivelyFindClassByName(type, className))
                .filter(Objects::nonNull)
                .findFirst();
    }

    // Nullable
    private AbstractTypeDeclaration recursivelyFindClassByName(AbstractTypeDeclaration currentTypeDeclaration, String className) {
        if (currentTypeDeclaration.getName().toString().equals(className)) {
            return currentTypeDeclaration;
        } else {
            for (AbstractTypeDeclaration type : TypeExtractor.getTypes(currentTypeDeclaration)) {
                AbstractTypeDeclaration result = recursivelyFindClassByName(type, className);
                if (result != null) {
                    return result;
                }
            }
            return null;
        }
    }

    private AbstractTypeDeclaration getFirstType(CompilationUnit compilationUnit) {
        return ((List<AbstractTypeDeclaration>) compilationUnit.types())
                .stream()
                .filter(AbstractTypeDeclaration -> AbstractTypeDeclaration instanceof TypeDeclaration || isRecordDeclaration(AbstractTypeDeclaration))
                .findFirst()
                .orElseThrow(() -> new PluginException("No types are present in the current java file"));
    }
}

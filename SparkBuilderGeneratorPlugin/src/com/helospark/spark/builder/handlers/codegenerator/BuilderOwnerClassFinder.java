package com.helospark.spark.builder.handlers.codegenerator;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.ALWAYS_GENERATE_BUILDER_TO_FIRST_CLASS;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.CurrentlySelectedApplicableClassesClassNameProvider;
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

    public BuilderOwnerClassFinder(CurrentlySelectedApplicableClassesClassNameProvider currentlySelectedApplicableClassesClassNameProvider, PreferencesManager preferencesManager) {
        this.currentlySelectedApplicableClassesClassNameProvider = currentlySelectedApplicableClassesClassNameProvider;
        this.preferencesManager = preferencesManager;
    }

    public CompilationUnitModificationDomain provideBuilderOwnerClass(CompilationUnit compilationUnit, AST ast, ASTRewrite rewriter, ICompilationUnit iCompilationUnit) {
        TypeDeclaration builderType = getBuilderOwnerType(compilationUnit, iCompilationUnit);

        ListRewrite listRewrite = rewriter.getListRewrite(builderType, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);

        return CompilationUnitModificationDomain.builder()
                .withAst(ast)
                .withAstRewriter(rewriter)
                .withListRewrite(listRewrite)
                .withOriginalType(builderType)
                .withCompilationUnit(compilationUnit)
                .build();
    }

    private TypeDeclaration getBuilderOwnerType(CompilationUnit compilationUnit, ICompilationUnit iCompilationUnit) {
        if (preferencesManager.getPreferenceValue(ALWAYS_GENERATE_BUILDER_TO_FIRST_CLASS)) {
            return getFirstType(compilationUnit);
        } else {
            return getTypeAtCurrentSelection(iCompilationUnit, compilationUnit)
                    .orElse(getFirstType(compilationUnit));
        }
    }

    private Optional<TypeDeclaration> getTypeAtCurrentSelection(ICompilationUnit iCompilationUnit, CompilationUnit compilationUnit) {
        try {
            Optional<String> className = currentlySelectedApplicableClassesClassNameProvider.provideCurrentlySelectedClassName(iCompilationUnit);
            return className.flatMap(internalClassName -> findClassWithClassName(compilationUnit, internalClassName));
        } catch (Exception e) {
            System.out.println("[WARNING] Cannot extract currently selected class based on offset");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private Optional<TypeDeclaration> findClassWithClassName(CompilationUnit compilationUnit, String className) {
        return ((List<TypeDeclaration>) compilationUnit.types())
                .stream()
                .map(type -> recursivelyFindClassByName(type, className))
                .filter(Objects::nonNull)
                .findFirst();
    }

    // Nullable
    private TypeDeclaration recursivelyFindClassByName(TypeDeclaration currentTypeDeclaration, String className) {
        if (currentTypeDeclaration.getName().toString().equals(className)) {
            return currentTypeDeclaration;
        } else {
            for (TypeDeclaration type : currentTypeDeclaration.getTypes()) {
                TypeDeclaration result = recursivelyFindClassByName(type, className);
                if (result != null) {
                    return result;
                }
            }
            return null;
        }
    }

    private TypeDeclaration getFirstType(CompilationUnit compilationUnit) {
        List<TypeDeclaration> types = compilationUnit.types();
        if (types == null || types.size() == 0) {
            throw new PluginException("No types are present in the current java file");
        }
        return types.get(0);
    }
}

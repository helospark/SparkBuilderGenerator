package com.helospark.spark.builder.handlers.codegenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;
import com.helospark.spark.builder.handlers.exception.PluginException;

/**
 * Provides the owner class from the from the given compilation unit.
 * @author helospark
 */
public class BuilderOwnerClassFinder {
    private ActiveJavaEditorOffsetProvider activeJavaEditorOffsetProvider;

    public BuilderOwnerClassFinder(ActiveJavaEditorOffsetProvider activeJavaEditorOffsetProvider) {
        this.activeJavaEditorOffsetProvider = activeJavaEditorOffsetProvider;
    }

    public CompilationUnitModificationDomain provideBuilderOwnerClass(CompilationUnit compilationUnit, AST ast, ASTRewrite rewriter, ICompilationUnit iCompilationUnit) {
        TypeDeclaration builderType = getTypeAtCurrentSelection(iCompilationUnit, compilationUnit)
                .orElse(getFirstType(compilationUnit));

        ListRewrite listRewrite = rewriter.getListRewrite(builderType, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);

        return CompilationUnitModificationDomain.builder()
                .withAst(ast)
                .withAstRewriter(rewriter)
                .withListRewrite(listRewrite)
                .withOriginalType(builderType)
                .withCompilationUnit(compilationUnit)
                .build();
    }

    private Optional<TypeDeclaration> getTypeAtCurrentSelection(ICompilationUnit iCompilationUnit, CompilationUnit compilationUnit) {
        try {
            int offset = activeJavaEditorOffsetProvider.provideOffsetAtCurrentCursorPosition();
            Optional<String> className = getCurrentlySelectedClassName(iCompilationUnit, offset);
            if (!className.isPresent()) {
                return Optional.empty();
            } else {
                return findClassWithClassName(compilationUnit, className.get());
            }
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
            return Arrays.stream(currentTypeDeclaration.getTypes())
                    .map(type -> recursivelyFindClassByName(type, className))
                    .findFirst()
                    .orElse(null);
        }
    }

    private Optional<String> getCurrentlySelectedClassName(ICompilationUnit iCompilationUnit, int offset) throws JavaModelException {
        return Optional.ofNullable(iCompilationUnit.getElementAt(offset))
                .map(currentElement -> (IType) currentElement.getAncestor(IJavaElement.TYPE))
                .map(type -> type.getElementName());
    }

    private TypeDeclaration getFirstType(CompilationUnit compilationUnit) {
        List<TypeDeclaration> types = compilationUnit.types();
        if (types == null || types.size() == 0) {
            throw new PluginException("No types are present in the current java file");
        }
        return types.get(0);
    }
}

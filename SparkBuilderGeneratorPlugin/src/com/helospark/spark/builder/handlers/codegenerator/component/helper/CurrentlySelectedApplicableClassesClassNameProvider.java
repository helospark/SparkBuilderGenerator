package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import java.util.Optional;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Provides the class name of the currently selected class that can be used to generate builder based on the cursor position.
 * {@link IsTypeApplicableForBuilderGenerationPredicate} determines if the class is applicable for builde generation.
 * @author helospark
 */
public class CurrentlySelectedApplicableClassesClassNameProvider {
    private ActiveJavaEditorOffsetProvider activeJavaEditorOffsetProvider;
    private IsTypeApplicableForBuilderGenerationPredicate isTypeApplicableForBuilderGenerationPredicate;
    private ParentITypeExtractor parentITypeExtractor;

    public CurrentlySelectedApplicableClassesClassNameProvider(ActiveJavaEditorOffsetProvider activeJavaEditorOffsetProvider,
            IsTypeApplicableForBuilderGenerationPredicate isTypeApplicableForBuilderGenerationPredicate, ParentITypeExtractor parentITypeExtractor) {
        this.activeJavaEditorOffsetProvider = activeJavaEditorOffsetProvider;
        this.isTypeApplicableForBuilderGenerationPredicate = isTypeApplicableForBuilderGenerationPredicate;
        this.parentITypeExtractor = parentITypeExtractor;
    }

    public Optional<String> provideCurrentlySelectedClassName(ICompilationUnit iCompilationUnit) throws JavaModelException {
        int cursorOffset = activeJavaEditorOffsetProvider.provideOffsetOfCurrentCursorPosition();
        Optional<IType> currentlySelectedType = getCurrentlySelectedType(iCompilationUnit, cursorOffset);
        return currentlySelectedType.map(type -> type.getElementName());
    }

    private Optional<IType> getCurrentlySelectedType(ICompilationUnit iCompilationUnit, int offset) throws JavaModelException {
        Optional<IType> type = getITypeAtCurrentSelection(iCompilationUnit, offset);
        return recursivelyGetApplicableTypeInSuperClassHierarchy(type);
    }

    private Optional<IType> getITypeAtCurrentSelection(ICompilationUnit iCompilationUnit, int offset) throws JavaModelException {
        return ofNullable(iCompilationUnit.getElementAt(offset))
                .map(javaElement -> (IType) javaElement.getAncestor(IJavaElement.TYPE));
    }

    private Optional<IType> recursivelyGetApplicableTypeInSuperClassHierarchy(Optional<IType> optionalType) throws JavaModelException {
        if (!optionalType.isPresent()) {
            return empty();
        }
        IType type = optionalType.get();
        if (!isTypeApplicableForBuilderGenerationPredicate.test(type)) {
            Optional<IType> parentType = parentITypeExtractor.getParentType(type);
            return recursivelyGetApplicableTypeInSuperClassHierarchy(parentType);
        } else {
            return optionalType;
        }
    }

}

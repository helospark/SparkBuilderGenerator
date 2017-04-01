package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import static org.eclipse.jdt.core.Flags.isStatic;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Determines if the given IType is applicable for builder generation.
 * In the current implementation
 *  - Nonstatic nested classes (inner classes) are not applicable for builder generation, since they cannot contain static methods
 * @author helospark
 */
public class IsTypeApplicableForBuilderGenerationPredicate {
    private ParentITypeExtractor parentITypeExtractor;

    public IsTypeApplicableForBuilderGenerationPredicate(ParentITypeExtractor parentITypeExtractor) {
        this.parentITypeExtractor = parentITypeExtractor;
    }

    public boolean test(IType type) throws JavaModelException {
        return !isNestedType(type) || isStatic(type.getFlags());
    }

    private boolean isNestedType(IType iType) {
        return parentITypeExtractor.getParentType(iType).isPresent();
    }

}

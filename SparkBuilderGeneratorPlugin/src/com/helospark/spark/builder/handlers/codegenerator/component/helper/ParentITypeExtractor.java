package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import static java.util.Optional.ofNullable;

import java.util.Optional;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;

/**
 * Extracts the parent {@link IType} from the given IJavaElement.
 * @author helospark
 */
public class ParentITypeExtractor {

    public Optional<IType> getParentType(IJavaElement currentElement) {
        // getAncestor may return itself if it's already an IType
        // therefore we must get call getParent before
        return ofNullable(currentElement)
                .map(element -> element.getParent())
                .map(element -> element.getAncestor(IJavaElement.TYPE))
                .map(element -> (IType) element);
    }
}

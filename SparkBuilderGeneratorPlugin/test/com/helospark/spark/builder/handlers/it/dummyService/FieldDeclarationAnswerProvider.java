package com.helospark.spark.builder.handlers.it.dummyService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.mockito.invocation.InvocationOnMock;

import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;

/**
 * An answer provider for FullyQualifiedNameExtractor, that return the fully qualified name for some well known classes for testing purposes.
 * Returns irrelevant value for anything else.
 * @author helospark
 */
public class FieldDeclarationAnswerProvider {
    private static Set<String> recognisedClasses = new HashSet<>();

    static {
        recognisedClasses.add("java.util.Optional");
        recognisedClasses.add("java.util.List");
        recognisedClasses.add("java.util.Set");
        recognisedClasses.add("java.util.Collection");
        recognisedClasses.add("java.lang.Iterable");
        recognisedClasses.add("java.util.Map");
        recognisedClasses.add("java.util.SortedSet");
        recognisedClasses.add("java.util.SortedMap");
    }

    public static Object provideAnswer(InvocationOnMock inv) {
        Type type = ((BuilderField) inv.getArguments()[0]).getFieldType();
        if (type instanceof ParameterizedType) {
            Type baseType = ((ParameterizedType) type).getType();
            if (baseType instanceof SimpleType) {
                String name = ((SimpleType) baseType).getName().getFullyQualifiedName();

                // if name is fully qualified
                if (recognisedClasses.contains(name)) {
                    return Optional.ofNullable(name);
                }

                Optional<String> found = recognisedClasses.stream()
                        .filter(fqn -> fqn.endsWith("." + name))
                        .findFirst();
                if (found.isPresent()) {
                    return found;
                }
            }
        }
        return Optional.of("some.other.value");
    }

}

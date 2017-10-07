package com.helospark.spark.builder.handlers.it.dummyService;

import java.util.Optional;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.mockito.invocation.InvocationOnMock;

/**
 * An answer provider for FullyQualifiedNameExtractor, that return "java.util.Optional" for "Optional" and "java.util.Optional" type names
 * and irrelevant data for others.
 * @author helospark
 */
public class FieldDeclarationAnswerProvider {

    public static Object provideAnswer(InvocationOnMock inv) {
        FieldDeclaration fieldDeclaration = (FieldDeclaration) inv.getArguments()[0];
        Type asd = fieldDeclaration.getType();
        if (asd instanceof ParameterizedType) {
            Type baseType = ((ParameterizedType) asd).getType();
            if (baseType instanceof SimpleType) {
                String name = ((SimpleType) baseType).getName().getFullyQualifiedName();

                if (name.equals("Optional") || name.equals("java.util.Optional")) {
                    return Optional.of("java.util.Optional");
                }
            }
        }
        return Optional.of("some.other.value");
    }

}

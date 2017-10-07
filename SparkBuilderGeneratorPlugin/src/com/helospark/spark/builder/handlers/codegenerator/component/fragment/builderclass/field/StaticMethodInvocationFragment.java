package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.field;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;

/**
 * Creates a static method invocation.
 * Note that import is not added by this fragment.
 * @author helospark
 */
public class StaticMethodInvocationFragment {

    public MethodInvocation createStaticMethodInvocation(AST ast, String className, String methodName) {
        SimpleName typeName = ast.newSimpleName(className);

        MethodInvocation methodInvocation = ast.newMethodInvocation();
        methodInvocation.setName(ast.newSimpleName(methodName));
        methodInvocation.setExpression(typeName);

        return methodInvocation;
    }
}

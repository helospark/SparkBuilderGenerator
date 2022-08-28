package com.helospark.spark.builder.handlers.codegenerator.domain.instancefieldaccess;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;

/**
 * Accesses the original field via getter.
 * <pre>
 * rhsName.getField()
 * </pre>
 * 
 * @author helospark
 */
public class GetterFieldAccessStrategy implements InstanceFieldAccessStrategy {
    private String getterName;

    public GetterFieldAccessStrategy(String getterName) {
        this.getterName = getterName;
    }

    @Override
    public Expression createFieldAccessExpression(AST ast, String rhsName) {
        MethodInvocation getterMethodInvocation = ast.newMethodInvocation();
        getterMethodInvocation.setExpression(ast.newSimpleName(rhsName));
        getterMethodInvocation.setName(ast.newSimpleName(getterName));
        return getterMethodInvocation;
    }

}

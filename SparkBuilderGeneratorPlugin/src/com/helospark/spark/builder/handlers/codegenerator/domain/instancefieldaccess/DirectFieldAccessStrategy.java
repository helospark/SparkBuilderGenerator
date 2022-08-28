package com.helospark.spark.builder.handlers.codegenerator.domain.instancefieldaccess;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;

/**
 * Accesses the original field via field access.
 * <pre>
 * rhsName.field
 * </pre>
 * 
 * @author helospark
 */
public class DirectFieldAccessStrategy implements InstanceFieldAccessStrategy {
    private String originalFieldName;

    public DirectFieldAccessStrategy(String originalFieldName) {
        this.originalFieldName = originalFieldName;
    }

    @Override
    public Expression createFieldAccessExpression(AST ast, String rhsName) {
        FieldAccess builderFieldAccess = ast.newFieldAccess();
        builderFieldAccess.setExpression(ast.newSimpleName(rhsName));
        builderFieldAccess.setName(ast.newSimpleName(originalFieldName));
        return builderFieldAccess;
    }

}

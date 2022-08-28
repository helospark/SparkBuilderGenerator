package com.helospark.spark.builder.handlers.codegenerator.domain.instancefieldaccess;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;

public interface InstanceFieldAccessStrategy {

    public Expression createFieldAccessExpression(AST ast, String rhsName);

}

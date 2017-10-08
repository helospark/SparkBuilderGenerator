package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.field.chain;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;

public interface FieldDeclarationPostProcessorChainItem {

    public boolean doesSupport(String fullyQualifiedName);

    public Expression createExpression(AST ast, String fullyQualifiedName);

    public List<String> getImport(String fullyQualifiedName);

}

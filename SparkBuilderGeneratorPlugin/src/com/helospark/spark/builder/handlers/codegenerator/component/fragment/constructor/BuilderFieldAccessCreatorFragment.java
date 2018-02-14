package com.helospark.spark.builder.handlers.codegenerator.component.fragment.constructor;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.FieldAccess;

import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;

/**
 * Creates an access of the field in the builder.
 * <pre>
 * builderName.fieldName;
 * </pre>
 *
 * @author helospark
 */
public class BuilderFieldAccessCreatorFragment {

    public FieldAccess createBuilderFieldAccess(AST ast, String builderName, BuilderField field) {
        FieldAccess builderFieldAccess = ast.newFieldAccess();
        builderFieldAccess.setExpression(ast.newSimpleName(builderName));
        builderFieldAccess.setName(ast.newSimpleName(field.getOriginalFieldName()));
        return builderFieldAccess;
    }
}

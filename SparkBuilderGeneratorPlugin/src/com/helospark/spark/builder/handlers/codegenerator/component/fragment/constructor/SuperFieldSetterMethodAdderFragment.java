package com.helospark.spark.builder.handlers.codegenerator.component.fragment.constructor;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

import com.helospark.spark.builder.handlers.codegenerator.domain.SuperSetterBasedBuilderField;

/**
 * Adds setter calls for superclass fields, like:
 * <pre>
 * super.setField(builder.field);
 * super.setOtherField(builder.otherField);
 * </pre>
 * @author helospark
 */
public class SuperFieldSetterMethodAdderFragment {
    private BuilderFieldAccessCreatorFragment builderFieldAccessCreatorFragment;

    public SuperFieldSetterMethodAdderFragment(BuilderFieldAccessCreatorFragment builderFieldAccessCreatorFragment) {
        this.builderFieldAccessCreatorFragment = builderFieldAccessCreatorFragment;
    }

    public void populateBodyWithSuperSetterCalls(AST ast, String builderName, Block body, List<SuperSetterBasedBuilderField> builderFields) {
        for (SuperSetterBasedBuilderField field : builderFields) {
            FieldAccess builderFieldAccess = builderFieldAccessCreatorFragment.createBuilderFieldAccess(ast, builderName, field);

            SuperMethodInvocation superInvocation = ast.newSuperMethodInvocation();
            superInvocation.setName(ast.newSimpleName(field.getSetterName()));
            superInvocation.arguments().add(builderFieldAccess);

            body.statements().add(ast.newExpressionStatement(superInvocation));
        }
    }

}

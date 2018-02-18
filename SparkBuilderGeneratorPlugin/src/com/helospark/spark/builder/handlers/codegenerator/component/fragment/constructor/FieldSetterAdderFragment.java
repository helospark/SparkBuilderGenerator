package com.helospark.spark.builder.handlers.codegenerator.component.fragment.constructor;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.FieldAccess;

import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;

/**
 * Attaches field copy to the given body. Generated code is something like:
 * <pre>
 * this.firstField = rhsName.firstField;
 * this.secondField = rhsName.secondField;
 * </pre>
 * @author helospark
 */
public class FieldSetterAdderFragment {
    private BuilderFieldAccessCreatorFragment builderFieldAccessCreatorFragment;

    public FieldSetterAdderFragment(BuilderFieldAccessCreatorFragment builderFieldAccessCreatorFragment) {
        this.builderFieldAccessCreatorFragment = builderFieldAccessCreatorFragment;
    }

    public void populateBodyWithFieldSetCalls(AST ast, String rhsName, Block body, List<? extends BuilderField> builderFields) {
        for (BuilderField field : builderFields) {
            Assignment assignment = ast.newAssignment();

            FieldAccess fieldAccess = createThisFieldAccess(ast, field);
            assignment.setLeftHandSide(fieldAccess);

            FieldAccess builderFieldAccess = builderFieldAccessCreatorFragment.createBuilderFieldAccess(ast, rhsName, field);
            assignment.setRightHandSide(builderFieldAccess);

            body.statements().add(ast.newExpressionStatement(assignment));
        }
    }

    private FieldAccess createThisFieldAccess(AST ast, BuilderField field) {
        FieldAccess fieldAccess = ast.newFieldAccess();
        fieldAccess.setExpression(ast.newThisExpression());
        fieldAccess.setName(ast.newSimpleName(field.getOriginalFieldName()));
        return fieldAccess;
    }

}

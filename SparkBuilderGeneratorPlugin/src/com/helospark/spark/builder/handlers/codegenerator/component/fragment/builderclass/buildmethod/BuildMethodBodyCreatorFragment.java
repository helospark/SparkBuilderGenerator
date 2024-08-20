package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.buildmethod;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ReturnStatement;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.IsRecordTypePredicate;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;

/**
 * Fragment to add create the build() method' body.
 * Generated code for classes something like:
 * <pre>
 * {
 *   return new Clazz(this);
 * }
 * </pre>
 * 
 * and for record:
 * <pre>
 * {
 *   return new Clazz(param1, param2);
 * }
 * </pre>
 * @author helospark
 */
public class BuildMethodBodyCreatorFragment {

    public Block createBody(AST ast, AbstractTypeDeclaration originalType, List<BuilderField> builderFields) {
        ClassInstanceCreation newClassInstanceCreation = ast.newClassInstanceCreation();
        newClassInstanceCreation.setType(ast.newSimpleType(ast.newName(originalType.getName().toString())));

        if (IsRecordTypePredicate.isRecordDeclaration(originalType)) {
            for (BuilderField field : builderFields) {
                newClassInstanceCreation.arguments().add(ast.newSimpleName(field.getBuilderFieldName()));
            }
        } else {
            newClassInstanceCreation.arguments().add(ast.newThisExpression());
        }
        ReturnStatement statement = ast.newReturnStatement();
        statement.setExpression(newClassInstanceCreation);

        Block block = ast.newBlock();
        block.statements().add(statement);
        return block;
    }
}

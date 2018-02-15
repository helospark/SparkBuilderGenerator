package com.helospark.spark.builder.handlers.codegenerator.component;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.withmethod.StagedBuilderWithMethodDefiniationCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.empty.NewBuilderAndWithMethodCallCreationFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.JavadocAdder;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderProperties;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;

/**
 * Creates a method to instantiate staged builder, without using the builder() method, like:
 * <pre>
 *  public static INextStage withFirstField(String firstField) {
 *     return new Builder().withFirstField(firstField);
 *  }
 * </pre>
 * @author helospark
 */
public class StagedBuilderCreationWithMethodAdder {
    private StagedBuilderWithMethodDefiniationCreatorFragment stagedBuilderWithMethodDefiniationCreatorFragment;
    private NewBuilderAndWithMethodCallCreationFragment newBuilderAndWithMethodCallCreationFragment;
    private JavadocAdder javadocAdder;

    public StagedBuilderCreationWithMethodAdder(StagedBuilderWithMethodDefiniationCreatorFragment stagedBuilderWithMethodDefiniationCreatorFragment,
            NewBuilderAndWithMethodCallCreationFragment newBuilderAndWithMethodCallCreationFragment, JavadocAdder javadocAdder) {
        this.stagedBuilderWithMethodDefiniationCreatorFragment = stagedBuilderWithMethodDefiniationCreatorFragment;
        this.newBuilderAndWithMethodCallCreationFragment = newBuilderAndWithMethodCallCreationFragment;
        this.javadocAdder = javadocAdder;
    }

    public void addBuilderMethodToCompilationUnit(CompilationUnitModificationDomain modificationDomain, TypeDeclaration builderType,
            StagedBuilderProperties currentStage) {
        AST ast = modificationDomain.getAst();
        ListRewrite listRewrite = modificationDomain.getListRewrite();
        BuilderField firstField = currentStage.getNamedVariableDeclarationField().get(0);

        StagedBuilderProperties nextStage = currentStage.getNextStage().orElse(currentStage);
        MethodDeclaration staticWithMethod = stagedBuilderWithMethodDefiniationCreatorFragment.createNewWithMethod(ast, firstField, nextStage);
        staticWithMethod.modifiers().add(ast.newModifier(ModifierKeyword.STATIC_KEYWORD));

        String parameterName = firstField.getBuilderFieldName();
        String withMethodName = staticWithMethod.getName().toString();
        Block block = newBuilderAndWithMethodCallCreationFragment.createReturnBlock(ast, builderType, withMethodName, parameterName);

        javadocAdder.addJavadocForWithBuilderMethod(ast, builderType.getName().toString(), parameterName, staticWithMethod);

        staticWithMethod.setBody(block);

        listRewrite.insertLast(staticWithMethod, null);
    }

}

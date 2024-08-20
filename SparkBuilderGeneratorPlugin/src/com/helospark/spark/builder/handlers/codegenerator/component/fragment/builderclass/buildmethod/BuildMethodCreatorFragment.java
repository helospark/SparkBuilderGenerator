package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.buildmethod;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;

/**
 * Fragment to create the build() method.
 * Generated code is something like:
 * <pre>
 * public Clazz build() {
 *   return new Clazz(this);
 * }
 * </pre>
 * @author helospark
 */
public class BuildMethodCreatorFragment {
    private BuildMethodDeclarationCreatorFragment buildMethodDeclarationCreatorFragment;
    private BuildMethodBodyCreatorFragment buildMethodBodyCreatorFragment;

    public BuildMethodCreatorFragment(BuildMethodDeclarationCreatorFragment buildMethodDeclarationCreatorFragment,
            BuildMethodBodyCreatorFragment buildMethodBodyCreatorFragment) {
        this.buildMethodDeclarationCreatorFragment = buildMethodDeclarationCreatorFragment;
        this.buildMethodBodyCreatorFragment = buildMethodBodyCreatorFragment;
    }

    public MethodDeclaration addBuildMethodToBuilder(AST ast, AbstractTypeDeclaration originalType, List<BuilderField> builderFields) {
        Block block = buildMethodBodyCreatorFragment.createBody(ast, originalType, builderFields);
        MethodDeclaration method = buildMethodDeclarationCreatorFragment.createMethod(ast, originalType);
        method.setBody(block);
        return method;
    }

}

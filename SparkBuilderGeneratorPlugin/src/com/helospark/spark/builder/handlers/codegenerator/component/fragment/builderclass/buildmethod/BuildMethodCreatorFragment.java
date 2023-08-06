package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.buildmethod;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;

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

    public MethodDeclaration addBuildMethodToBuilder(AST ast, AbstractTypeDeclaration originalType) {
        Block block = buildMethodBodyCreatorFragment.createBody(ast, originalType);
        MethodDeclaration method = buildMethodDeclarationCreatorFragment.createMethod(ast, originalType);
        method.setBody(block);
        return method;
    }

}

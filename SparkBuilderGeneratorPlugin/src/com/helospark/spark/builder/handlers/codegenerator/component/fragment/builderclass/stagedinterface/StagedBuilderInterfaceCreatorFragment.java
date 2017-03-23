package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.stagedinterface;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.buildmethod.BuildMethodDeclarationCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.fragment.buildermethod.StagedBuilderMethodDefiniationCreatorFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderProperties;
import com.helospark.spark.builder.handlers.codegenerator.domain.CompilationUnitModificationDomain;
import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;

public class StagedBuilderInterfaceCreatorFragment {
    private StagedBuilderMethodDefiniationCreatorFragment stagedBuilderMethodDefiniationCreatorFragment;
    private BuildMethodDeclarationCreatorFragment buildMethodDeclarationCreatorFragment;

    public StagedBuilderInterfaceCreatorFragment(StagedBuilderMethodDefiniationCreatorFragment stagedBuilderMethodDefiniationCreatorFragment,
            BuildMethodDeclarationCreatorFragment buildMethodDeclarationCreatorFragment) {
        this.stagedBuilderMethodDefiniationCreatorFragment = stagedBuilderMethodDefiniationCreatorFragment;
        this.buildMethodDeclarationCreatorFragment = buildMethodDeclarationCreatorFragment;
    }

    public TypeDeclaration createInterfaceFor(CompilationUnitModificationDomain modificationDomain,
            StagedBuilderProperties stagedBuilderProperties) {
        String interfaceName = stagedBuilderProperties.getInterfaceName();
        AST ast = modificationDomain.getAst();
        TypeDeclaration originalType = modificationDomain.getOriginalType();

        TypeDeclaration addedInterface = ast.newTypeDeclaration();
        addedInterface.setInterface(true);
        addedInterface.setName(ast.newSimpleName(interfaceName));
        addedInterface.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));

        for (NamedVariableDeclarationField namedVariableDeclarationField : stagedBuilderProperties
                .getNamedVariableDeclarationField()) {
            StagedBuilderProperties nextStage = getNextStage(stagedBuilderProperties);

            MethodDeclaration withMethodDeclaration = stagedBuilderMethodDefiniationCreatorFragment.createNewWithMethod(
                    ast,
                    namedVariableDeclarationField,
                    nextStage);
            addedInterface.bodyDeclarations().add(withMethodDeclaration);
        }
        if (stagedBuilderProperties.isBuildStage()) {
            MethodDeclaration buildMethod = buildMethodDeclarationCreatorFragment.createMethod(ast,
                    originalType);
            addedInterface.bodyDeclarations().add(buildMethod);
        }

        return addedInterface;
    }

    private StagedBuilderProperties getNextStage(StagedBuilderProperties stagedBuilderProperties) {
        StagedBuilderProperties nextStage = stagedBuilderProperties.isBuildStage() ? stagedBuilderProperties
                : stagedBuilderProperties.getNextStage().get();
        return nextStage;
    }

}

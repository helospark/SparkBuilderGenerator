package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.withmethod;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.ADD_NONNULL_ON_RETURN;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;

import com.helospark.spark.builder.handlers.codegenerator.component.helper.BuilderMethodNameBuilder;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.MarkerAnnotationAttacher;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderInterfaceNameProvider;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.StagedBuilderProperties;
import com.helospark.spark.builder.handlers.codegenerator.domain.NamedVariableDeclarationField;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Creates with method for staged builder.
 * <pre>
 * public ISecondStage withFirstField(String firstField);
 * </pre>
 * @author helospark
 */
public class StagedBuilderWithMethodDefiniationCreatorFragment {
    private PreferencesManager preferencesManager;
    private BuilderMethodNameBuilder builderClassMethodNameGeneratorService;
    private MarkerAnnotationAttacher markerAnnotationAttacher;
    private WithMethodParameterCreatorFragment withMethodParameterCreatorFragment;

    public StagedBuilderWithMethodDefiniationCreatorFragment(PreferencesManager preferencesManager,
            BuilderMethodNameBuilder builderClassMethodNameGeneratorService,
            MarkerAnnotationAttacher markerAnnotationAttacher,
            StagedBuilderInterfaceNameProvider stagedBuilderInterfaceNameProvider,
            WithMethodParameterCreatorFragment withMethodParameterCreatorFragment) {
        this.preferencesManager = preferencesManager;
        this.builderClassMethodNameGeneratorService = builderClassMethodNameGeneratorService;
        this.markerAnnotationAttacher = markerAnnotationAttacher;
        this.withMethodParameterCreatorFragment = withMethodParameterCreatorFragment;
    }

    public MethodDeclaration createNewWithMethod(AST ast, NamedVariableDeclarationField namedVariableDeclarationField,
            StagedBuilderProperties nextStage) {
        String fieldName = namedVariableDeclarationField.getBuilderFieldName();
        MethodDeclaration builderMethod = ast.newMethodDeclaration();
        builderMethod.setName(ast.newSimpleName(builderClassMethodNameGeneratorService.build(fieldName)));
        builderMethod.setReturnType2(ast.newSimpleType(ast.newName(nextStage.getInterfaceName())));
        builderMethod.parameters()
                .add(withMethodParameterCreatorFragment.createWithMethodParameter(ast, namedVariableDeclarationField.getFieldDeclaration(), fieldName));

        if (preferencesManager.getPreferenceValue(ADD_NONNULL_ON_RETURN)) {
            markerAnnotationAttacher.attachNonNull(ast, builderMethod);
        }
        builderMethod.modifiers().add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));

        return builderMethod;
    }

}

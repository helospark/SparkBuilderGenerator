package com.helospark.spark.builder.handlers.codegenerator.component.fragment.builderclass.constructor;

import static com.helospark.spark.builder.preferences.PluginPreferenceList.BUILDER_COPY_METHOD_PATTERN;
import static com.helospark.spark.builder.preferences.PluginPreferenceList.CREATE_BUILDER_COPY_METHOD;
import static org.eclipse.jdt.core.dom.Modifier.ModifierKeyword.PUBLIC_KEYWORD;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.helospark.spark.builder.handlers.codegenerator.component.fragment.constructor.FieldSetterAdderFragment;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TemplateResolver;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.TypeDeclarationToVariableNameConverter;
import com.helospark.spark.builder.handlers.codegenerator.domain.BuilderField;
import com.helospark.spark.builder.preferences.PreferencesManager;

/**
 * Creates a private constructor that copies all fields from the given class.
 * <pre>
 * private Builder from(OriginalType original) {
 *   this.field1 = original.field1;
 *   this.field2 = original.field2;
 *   return this;
 * }
 * </pre>
 * @author helospark
 */
public class RegularBuilderCopyMethodAdderFragment {
    private FieldSetterAdderFragment fieldSetterAdderFragment;
    private TypeDeclarationToVariableNameConverter typeDeclarationToVariableNameConverter;
    private PreferencesManager preferencesManager;
    private TemplateResolver templateResolver;

    public RegularBuilderCopyMethodAdderFragment(FieldSetterAdderFragment fieldSetterAdderFragment,
            TypeDeclarationToVariableNameConverter typeDeclarationToVariableNameConverter, PreferencesManager preferencesManager,
            TemplateResolver templateResolver) {
        this.fieldSetterAdderFragment = fieldSetterAdderFragment;
        this.typeDeclarationToVariableNameConverter = typeDeclarationToVariableNameConverter;
        this.preferencesManager = preferencesManager;
        this.templateResolver = templateResolver;
    }

    public void addCopyMethodIfNeeded(AST ast, TypeDeclaration builderType, TypeDeclaration originalType, List<BuilderField> builderFields) {
        if (preferencesManager.getPreferenceValue(CREATE_BUILDER_COPY_METHOD)) {
            createCopyMethod(ast, builderType, originalType, builderFields);
        }
    }

    private void createCopyMethod(AST ast, TypeDeclaration builderType, TypeDeclaration originalType, List<BuilderField> builderFields) {
        String parameterName = typeDeclarationToVariableNameConverter.convert(originalType);
        Block body = ast.newBlock();
        fieldSetterAdderFragment.populateBodyWithFieldSetCalls(ast, parameterName, body, builderFields);
        addReturnThisStatement(ast, body);
        builderType.bodyDeclarations().add(createCopyMethod(ast, builderType, originalType, parameterName, body));
    }

    private void addReturnThisStatement(AST ast, Block body) {
        ReturnStatement builderReturnStatement = ast.newReturnStatement();
        builderReturnStatement.setExpression(ast.newThisExpression());
        body.statements().add(builderReturnStatement);
    }

    private MethodDeclaration createCopyMethod(AST ast, TypeDeclaration builderType, TypeDeclaration originalType, String parameterName,
            Block body) {
        MethodDeclaration copyMethod = ast.newMethodDeclaration();
        copyMethod.setBody(body);
        copyMethod.setReturnType2(createReturnTypeFromBuilder(ast, builderType));
        copyMethod.setName(ast.newSimpleName(builderType.getName().toString()));
        copyMethod.modifiers().add(ast.newModifier(PUBLIC_KEYWORD));
        copyMethod.parameters().add(createParameter(ast, originalType, parameterName));
        copyMethod.setName(createCopyMethodName(ast, builderType, originalType));
        return copyMethod;
    }

    private SimpleName createCopyMethodName(AST ast, TypeDeclaration builderType, TypeDeclaration originalType) {
        Map<String, String> replacements = new HashMap<>();
        replacements.put("BuilderType", builderType.getName().toString());
        replacements.put("builderType", typeDeclarationToVariableNameConverter.convert(builderType));
        replacements.put("Type", originalType.getName().toString());
        replacements.put("type", typeDeclarationToVariableNameConverter.convert(originalType));
        String pattern = preferencesManager.getPreferenceValue(BUILDER_COPY_METHOD_PATTERN);
        String methodName = templateResolver.resolveTemplate(pattern, replacements);
        return ast.newSimpleName(methodName);
    }

    private Type createReturnTypeFromBuilder(AST ast, TypeDeclaration builderType) {
        return ast.newSimpleType(ast.newName(builderType.getName().getIdentifier()));
    }

    private SingleVariableDeclaration createParameter(AST ast, TypeDeclaration originalType, String parameterName) {
        SingleVariableDeclaration methodParameterDeclaration = ast.newSingleVariableDeclaration();
        methodParameterDeclaration.setType(ast.newSimpleType(ast.newName(originalType.getName().toString())));
        methodParameterDeclaration.setName(ast.newSimpleName(parameterName));
        return methodParameterDeclaration;
    }

}

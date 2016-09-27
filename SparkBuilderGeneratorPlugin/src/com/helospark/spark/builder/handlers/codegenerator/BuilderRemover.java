package com.helospark.spark.builder.handlers.codegenerator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

/**
 * Removes the builder if exists.
 * 
 * @author helospark
 */
public class BuilderRemover {

    @SuppressWarnings("unchecked")
    public void removeExistingBuilder(AST ast, ASTRewrite rewriter, CompilationUnit compilationUnit) {
        List<TypeDeclaration> typesList = compilationUnit.types();
        List<BodyDeclaration> otherTypes = ((List<BodyDeclaration>) typesList.get(0).bodyDeclarations())
                .stream()
                .filter(value -> value instanceof TypeDeclaration)
                .collect(Collectors.toList());
        if (otherTypes.size() == 1) {
            TypeDeclaration builderTypeDeclaration = (TypeDeclaration) otherTypes.get(0);
            TypeDeclaration mainType = typesList.get(0);
            MethodDeclaration constructorToRemove = findPrivateConstructor(builderTypeDeclaration, mainType);
            MethodDeclaration builderMethodToRemove = findBuilderMethod(builderTypeDeclaration, mainType);
            rewriter.remove(constructorToRemove, null);
            rewriter.remove(builderMethodToRemove, null);
            rewriter.remove(otherTypes.get(0), null);
        }
    }

    @SuppressWarnings("unchecked")
    private MethodDeclaration findBuilderMethod(TypeDeclaration builderTypeDeclaration, TypeDeclaration mainType) {
        return Arrays.stream(mainType.getMethods())
                .filter(method -> !method.isConstructor())
                .filter(method -> isStatic(method.modifiers()))
                .filter(method -> method.parameters().size() == 0)
                .filter(method -> isReturnTypeSameAsBuilder(builderTypeDeclaration, method.getReturnType2()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No builder method found"));
    }

    private MethodDeclaration findPrivateConstructor(TypeDeclaration builderTypeDeclaration, TypeDeclaration mainType) {
        return Arrays.stream(mainType.getMethods())
                .filter(method -> method.isConstructor())
                .filter(method -> method.parameters().size() == 1)
                .filter(method -> isParameterSameAsBuilder(builderTypeDeclaration, method))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No constructor found for builder"));
    }

    private boolean isStatic(List<Modifier> list) {
        return list.stream()
                .filter(element -> element.getKeyword() == ModifierKeyword.STATIC_KEYWORD)
                .findFirst()
                .map(element -> Boolean.TRUE)
                .orElse(Boolean.FALSE);
    }

    private boolean isParameterSameAsBuilder(TypeDeclaration builderTypeDeclaration, MethodDeclaration method) {
        return ((SingleVariableDeclaration) method.parameters().get(0))
                .getType()
                .toString()
                .equals(getBuilderTypeName(builderTypeDeclaration));
    }

    private boolean isReturnTypeSameAsBuilder(TypeDeclaration builderTypeDeclaration, Type type) {
        return type.toString()
                .equals(getBuilderTypeName(builderTypeDeclaration));
    }

    private String getBuilderTypeName(TypeDeclaration builderTypeDeclaration) {
        return builderTypeDeclaration.getName().toString();
    }

}

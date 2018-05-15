package com.helospark.spark.builder.handlers.codegenerator.component.remover;

import static com.helospark.spark.builder.preferences.StaticPreferences.JSON_DESERIALIZE_CLASS_NAME;

import java.util.List;

import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import com.helospark.spark.builder.preferences.PluginPreferenceList;
import com.helospark.spark.builder.preferences.PreferencesManager;

public class JsonDeserializeRemover implements BuilderRemoverChainItem {
    private PreferencesManager preferencesManager;

    public JsonDeserializeRemover(PreferencesManager preferencesManager) {
        this.preferencesManager = preferencesManager;
    }

    @Override
    public void remove(ASTRewrite rewriter, TypeDeclaration mainType) {
        if (preferencesManager.getPreferenceValue(PluginPreferenceList.ADD_JACKSON_DESERIALIZE_ANNOTATION)) {
            ((List<IExtendedModifier>) mainType.modifiers())
                    .stream()
                    .filter(modifier -> modifier instanceof NormalAnnotation)
                    .map(modifier -> (NormalAnnotation) modifier)
                    .filter(annotation -> annotation.getTypeName().toString().equals(JSON_DESERIALIZE_CLASS_NAME))
                    .filter(annotation -> isBuilderDeserializer(annotation))
                    .findFirst()
                    .ifPresent(annotation -> removeAnnotation(annotation, rewriter, mainType));
        }

    }

    private void removeAnnotation(NormalAnnotation annotation, ASTRewrite rewriter, TypeDeclaration mainType) {
        ListRewrite modifierRewrite = rewriter.getListRewrite(mainType, TypeDeclaration.MODIFIERS2_PROPERTY);
        modifierRewrite.remove(annotation, null);
    }

    private boolean isBuilderDeserializer(NormalAnnotation annotation) {
        List<MemberValuePair> values = annotation.values();
        if (values.size() != 1) {
            return false;
        }
        return values.get(0)
                .getName()
                .toString()
                .equals("builder");
    }

}

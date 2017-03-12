package com.helospark.sparktemplatingplugin.ui.editor.completition.chain.localvariable;

public class InnerBlockRemoverSourceProcessor {
    private static final char CLOSING_BRACE = '}';
    private static final char OPENING_BRACE = '{';

    public String process(String source) {
        StringBuilder result = new StringBuilder();
        boolean isDeleting = false;
        int braceIndex = 0;
        int deletingEndBranceIndex = 0;
        for (int i = source.length() - 1; i >= 0; --i) {
            if (source.charAt(i) == CLOSING_BRACE) {
                if (!isDeleting) {
                    deletingEndBranceIndex = braceIndex;
                    isDeleting = true;
                }
                --braceIndex;
            } else if (source.charAt(i) == OPENING_BRACE) {
                ++braceIndex;
                if (isDeleting && deletingEndBranceIndex == braceIndex) {
                    isDeleting = false;
                    continue;
                }
            }
            if (!isDeleting) {
                result.append(source.charAt(i));
            }
        }
        return result.reverse().toString();
    }
}

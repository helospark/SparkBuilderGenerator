package com.helospark.sparktemplatingplugin.ui.editor.completition.chain.localvariable;

public class NoSourceCleaningSourceProcessor {
    private static final String MULTILINE_COMMENT_START_SEQUENCE = "/*";
    private static final String MULTILINE_COMMENT_END_SEQUENCE = "*/";
    private static final String SINGLINE_COMMENT_START_SEQUENCE = "//";
    private static final char ESCAPE_CHARACTER = '\\';

    public String process(String input) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean isReqularProgram = true;
        boolean isInString = false;
        boolean isInCharacter = false;
        boolean isInSingleLineComment = false;
        boolean isInMultiLineComment = false;
        for (int i = 0; i < input.length(); ++i) {
            if ((isReqularProgram || isInString) && isNonEscapedStartSequence("\"", input, i)) {
                isInString = !isInString;
                isReqularProgram = !isInString;
                continue; // Skip the delimiter
            }
            if ((isReqularProgram || isInCharacter) && isNonEscapedStartSequence("'", input, i)) {
                isInCharacter = !isInCharacter;
                isReqularProgram = !isInCharacter;
                continue; // Skip the delimiter
            }
            if (isReqularProgram && isNonEscapedStartSequence(MULTILINE_COMMENT_START_SEQUENCE, input, i)) {
                isInMultiLineComment = true;
                isReqularProgram = false;
                i += MULTILINE_COMMENT_START_SEQUENCE.length() - 1;
                continue; // Skip the delimiter
            }
            if (isInMultiLineComment && isNonEscapedStartSequence(MULTILINE_COMMENT_END_SEQUENCE, input, i)) {
                isInMultiLineComment = false;
                isReqularProgram = true;
                i += MULTILINE_COMMENT_END_SEQUENCE.length() - 1;
                continue; // Skip the delimiter
            }
            if (isReqularProgram && isNonEscapedStartSequence(SINGLINE_COMMENT_START_SEQUENCE, input, i)) {
                isInSingleLineComment = true;
                isReqularProgram = false;
            }
            if (isInSingleLineComment && input.charAt(i) == '\n') {
                isInSingleLineComment = false;
                isReqularProgram = true;
            }
            if (isReqularProgram) {
                stringBuilder.append(input.charAt(i));
            }
        }
        return stringBuilder.toString();
    }

    private boolean isNonEscapedStartSequence(String sequence, String input, int i) {
        if (i > 0 && input.charAt(i - 1) == ESCAPE_CHARACTER) {
            return false;
        }
        for (int j = 0; j < sequence.length(); ++j) {
            if (input.length() >= i + j) {
                if (input.charAt(i + j) != sequence.charAt(j)) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

}

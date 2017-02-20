package com.helospark.sparktemplatingplugin.ui.editor.rule;

import org.eclipse.jface.text.rules.ICharacterScanner;

public class EscapeSequenceDetector {
    private char startEscapeSequence;

    public EscapeSequenceDetector(char startEscapeSequence) {
        this.startEscapeSequence = startEscapeSequence;
    }

    public boolean isDetected(ICharacterScanner scanner) {
        Character previousCharacter = getPreviousCharacter(scanner);
        return previousCharacter != null && previousCharacter.charValue() == startEscapeSequence;
    }

    private Character getPreviousCharacter(ICharacterScanner scanner) {
        if (scanner.getColumn() >= 2) {
            scanner.unread();
            scanner.unread();
            char previousCharacter = (char) scanner.read();
            scanner.read(); // put the scanner back to original state
            return previousCharacter;
        } else {
            return null;
        }
    }
}

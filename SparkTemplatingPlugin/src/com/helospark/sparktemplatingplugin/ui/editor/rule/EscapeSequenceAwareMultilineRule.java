package com.helospark.sparktemplatingplugin.ui.editor.rule;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;

public class EscapeSequenceAwareMultilineRule extends MultiLineRule {
    private EscapeSequenceDetector escapeSequenceDetector;

    public EscapeSequenceAwareMultilineRule(String startSequence, String endSequence, IToken token, char escapeCharacter, boolean breaksOnEOF, char startEscapeSequence) {
        super(startSequence, endSequence, token, escapeCharacter, breaksOnEOF);
        escapeSequenceDetector = new EscapeSequenceDetector(startEscapeSequence);
    }

    public EscapeSequenceAwareMultilineRule(String startSequence, String endSequence, IToken token, char escapeCharacter, char startEscapeSequence) {
        super(startSequence, endSequence, token, escapeCharacter);
        escapeSequenceDetector = new EscapeSequenceDetector(startEscapeSequence);
    }

    public EscapeSequenceAwareMultilineRule(String startSequence, String endSequence, IToken token, char startEscapeSequence) {
        super(startSequence, endSequence, token);
        escapeSequenceDetector = new EscapeSequenceDetector(startEscapeSequence);
    }

    @Override
    protected boolean sequenceDetected(
            ICharacterScanner scanner,
            char[] sequence,
            boolean eofAllowed) {
        if (escapeSequenceDetector.isDetected(scanner)) {
            return false;
        }

        return super.sequenceDetected(scanner, sequence, eofAllowed);
    }

}

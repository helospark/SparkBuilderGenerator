package com.helospark.sparktemplatingplugin.ui.editor.rule;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;

public class EscapeSequenceAwareEndOfLineRule extends EndOfLineRule {
    private EscapeSequenceDetector escapeSequenceDetector;

    public EscapeSequenceAwareEndOfLineRule(String startSequence, IToken token, char escapeCharacter, boolean escapeContinuesLine, char startEscapeSequence) {
        super(startSequence, token, escapeCharacter, escapeContinuesLine);
        escapeSequenceDetector = new EscapeSequenceDetector(startEscapeSequence);
    }

    public EscapeSequenceAwareEndOfLineRule(String startSequence, IToken token, char escapeCharacter, char startEscapeSequence) {
        super(startSequence, token, escapeCharacter);
        escapeSequenceDetector = new EscapeSequenceDetector(startEscapeSequence);
    }

    public EscapeSequenceAwareEndOfLineRule(String startSequence, IToken token, char startEscapeSequence) {
        super(startSequence, token);
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

package com.helospark.sparktemplatingplugin.ui.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

import com.helospark.sparktemplatingplugin.ui.editor.rule.EscapeSequenceAwareEndOfLineRule;
import com.helospark.sparktemplatingplugin.ui.editor.rule.EscapeSequenceAwareMultilineRule;

public class TemplatingToolPartitionScanner extends RuleBasedPartitionScanner {
    public final static String PROGRAM_SOURCE = "__stt_program_source";

    public TemplatingToolPartitionScanner() {

        IToken programToken = new Token(PROGRAM_SOURCE);

        List<IPredicateRule> rules = new ArrayList<>();

        rules.add(new EscapeSequenceAwareEndOfLineRule("#", programToken, '\\'));
        rules.add(new EscapeSequenceAwareMultilineRule("${", "}", programToken, '\\'));

        setPredicateRules(rules.toArray(new IPredicateRule[0]));
    }
}

package com.helospark.sparktemplatingplugin.ui.editor;

import org.eclipse.jdt.ui.text.IColorManager;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.WhitespaceRule;

public class TemplatingToolTemplateScanner extends RuleBasedScanner {

    public TemplatingToolTemplateScanner(IColorManager manager) {
        IRule[] rules = new IRule[1];
        // Add generic whitespace rule.
        rules[0] = new WhitespaceRule(new TemplatingToolWhitespaceDetector());

        setRules(rules);
    }
}

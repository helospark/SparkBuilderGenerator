package com.helospark.sparktemplatingplugin.ui.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.internal.ui.text.AbstractJavaScanner;
import org.eclipse.jdt.internal.ui.text.CombinedWordRule;
import org.eclipse.jdt.internal.ui.text.ISourceVersionDependent;
import org.eclipse.jdt.internal.ui.text.JavaWhitespaceDetector;
import org.eclipse.jdt.internal.ui.text.JavaWordDetector;
import org.eclipse.jdt.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * Adopted from {@link org.eclipse.jdt.internal.ui.text.java.JavaCodeScanner} as
 * it is a final class this code had to be copied modified slightly.
 * 
 * Modifications to the original: - Added rule for String detection - Added rule
 * for multiline and singleline comment detection
 * 
 * @author helospark
 */
@SuppressWarnings("restriction")
public final class TemplatingToolBeanShellCodeScanner extends AbstractJavaScanner {

    private static String[] fgKeywords = {
            "abstract",
            "break",
            "case", "catch", "class", "const", "continue",
            "default", "do",
            "else", "extends",
            "final", "finally", "for",
            "goto",
            "if", "implements", "import", "instanceof", "interface",
            "native", "new",
            "package", "private", "protected", "public",
            "static", "super", "switch", "synchronized",
            "this", "throw", "throws", "transient", "try",
            "volatile",
            "while" };
    private static String[] fgJava14Keywords = { "assert" };
    private static String[] fgJava15Keywords = { "enum" };

    private static String[] fgTypes = { "void", "boolean", "char", "byte", "short", "strictfp", "int", "long", "float", "double" };

    private static String[] fgConstants = { "false", "null", "true" };
    private static String[] fgTokenProperties = {
            "java_keyword",
            "java_string",
            "java_default",
            "java_keyword_return",
            "java_operator",
            "java_bracket",
            "semanticHighlighting.annotation.color",
            "java_single_line_comment",
            "java_multi_line_comment" };

    private List<ISourceVersionDependent> fVersionDependentRules = new ArrayList<>();

    public TemplatingToolBeanShellCodeScanner(IColorManager manager, IPreferenceStore store) {
        super(manager, store);
        initialize();
    }

    @Override
    protected String[] getTokenProperties() {
        return fgTokenProperties;
    }

    @Override
    protected List<IRule> createRules() {
        List<IRule> rules = new ArrayList<>();

        Token token = getToken("java_string");
        rules.add(new SingleLineRule("'", "'", token, '\\'));
        rules.add(new SingleLineRule("\"", "\"", token, '\\'));

        Token defaultToken = getToken("java_default");
        Token commentToken = getToken("java_single_line_comment");
        Token multilineCommentToken = getToken("java_multi_line_comment");

        rules.add(new WhitespaceRule(new JavaWhitespaceDetector(), defaultToken));
        rules.add(new EndOfLineRule("//", commentToken));
        rules.add(new MultiLineRule("/*", "*/", multilineCommentToken));

        String version = getPreferenceStore().getString("org.eclipse.jdt.core.compiler.source");

        token = getToken("semanticHighlighting.annotation.color");
        AnnotationRule atInterfaceRule = new AnnotationRule(getToken("java_keyword"), token, "1.5", version);
        rules.add(atInterfaceRule);
        this.fVersionDependentRules.add(atInterfaceRule);

        JavaWordDetector wordDetector = new JavaWordDetector();
        CombinedWordRule combinedWordRule = new CombinedWordRule(wordDetector, defaultToken);

        VersionedWordMatcher j14Matcher = new VersionedWordMatcher(defaultToken, "1.4", version);

        token = getToken("java_keyword");
        for (int i = 0; i < fgJava14Keywords.length; ++i) {
            j14Matcher.addWord(fgJava14Keywords[i], token);
        }
        combinedWordRule.addWordMatcher(j14Matcher);
        this.fVersionDependentRules.add(j14Matcher);

        VersionedWordMatcher j15Matcher = new VersionedWordMatcher(defaultToken, "1.5", version);

        token = getToken("java_keyword");
        for (int i = 0; i < fgJava15Keywords.length; ++i) {
            j15Matcher.addWord(fgJava15Keywords[i], token);
        }
        combinedWordRule.addWordMatcher(j15Matcher);
        this.fVersionDependentRules.add(j15Matcher);

        token = getToken("java_operator");
        rules.add(new OperatorRule(token));

        token = getToken("java_bracket");
        rules.add(new BracketRule(token));

        CombinedWordRule.WordMatcher returnWordRule = new CombinedWordRule.WordMatcher();
        token = getToken("java_keyword_return");
        returnWordRule.addWord("return", token);
        combinedWordRule.addWordMatcher(returnWordRule);

        CombinedWordRule.WordMatcher wordRule = new CombinedWordRule.WordMatcher();
        token = getToken("java_keyword");
        for (int i = 0; i < fgKeywords.length; ++i)
            wordRule.addWord(fgKeywords[i], token);
        for (int i = 0; i < fgTypes.length; ++i)
            wordRule.addWord(fgTypes[i], token);
        for (int i = 0; i < fgConstants.length; ++i) {
            wordRule.addWord(fgConstants[i], token);
        }
        combinedWordRule.addWordMatcher(wordRule);

        rules.add(combinedWordRule);

        setDefaultReturnToken(defaultToken);
        return rules;
    }

    @Override
    protected String getBoldKey(String colorKey) {
        if ("semanticHighlighting.annotation.color".equals(colorKey))
            return "semanticHighlighting.annotation.bold";
        return super.getBoldKey(colorKey);
    }

    @Override
    protected String getItalicKey(String colorKey) {
        if ("semanticHighlighting.annotation.color".equals(colorKey))
            return "semanticHighlighting.annotation.italic";
        return super.getItalicKey(colorKey);
    }

    @Override
    protected String getStrikethroughKey(String colorKey) {
        if ("semanticHighlighting.annotation.color".equals(colorKey))
            return "semanticHighlighting.annotation.strikethrough";
        return super.getStrikethroughKey(colorKey);
    }

    @Override
    protected String getUnderlineKey(String colorKey) {
        if ("semanticHighlighting.annotation.color".equals(colorKey))
            return "semanticHighlighting.annotation.underline";
        return super.getUnderlineKey(colorKey);
    }

    @Override
    public boolean affectsBehavior(PropertyChangeEvent event) {
        return event.getProperty().equals("org.eclipse.jdt.core.compiler.source") || super.affectsBehavior(event);
    }

    @Override
    public void adaptToPreferenceChange(PropertyChangeEvent event) {
        if (event.getProperty().equals("org.eclipse.jdt.core.compiler.source")) {
            Object value = event.getNewValue();

            if (value instanceof String) {
                String s = (String) value;

                for (Iterator<ISourceVersionDependent> it = this.fVersionDependentRules.iterator(); it.hasNext();) {
                    ISourceVersionDependent dependent = it.next();
                    dependent.setSourceVersion(s);
                }
            }
        } else if (super.affectsBehavior(event)) {
            super.adaptToPreferenceChange(event);
        }
    }

    private static class AnnotationRule
            implements IRule, ISourceVersionDependent {
        private final IWhitespaceDetector fWhitespaceDetector = new JavaWhitespaceDetector();
        private final IWordDetector fWordDetector = new JavaWordDetector();
        private final IToken fInterfaceToken;
        private final IToken fAtToken;
        private final String fVersion;
        private boolean fIsVersionMatch;

        public AnnotationRule(IToken interfaceToken, Token atToken, String version, String currentVersion) {
            this.fInterfaceToken = interfaceToken;
            this.fAtToken = atToken;
            this.fVersion = version;
            setSourceVersion(currentVersion);
        }

        @Override
        public IToken evaluate(ICharacterScanner scanner) {
            if (!this.fIsVersionMatch) {
                return Token.UNDEFINED;
            }
            ResettableScanner resettable = new ResettableScanner(scanner);
            if (resettable.read() == 64) {
                return readAnnotation(resettable);
            }
            resettable.reset();
            return Token.UNDEFINED;
        }

        private IToken readAnnotation(ResettableScanner scanner) {
            scanner.mark();
            skipWhitespace(scanner);
            if (readInterface(scanner)) {
                return this.fInterfaceToken;
            }
            scanner.reset();
            return this.fAtToken;
        }

        private boolean readInterface(ICharacterScanner scanner) {
            int ch = scanner.read();
            int i = 0;
            while (i < "interface".length() && "interface".charAt(i) == ch) {
                ++i;
                ch = scanner.read();
            }
            if (i < "interface".length()) {
                return false;
            }
            if (this.fWordDetector.isWordPart((char) ch)) {
                return false;
            }
            if (ch != -1) {
                scanner.unread();
            }
            return true;
        }

        private boolean skipWhitespace(ICharacterScanner scanner) {
            if (!this.fWhitespaceDetector.isWhitespace((char) scanner.read()))
                ;
            scanner.unread();
            return true;
        }

        @Override
        public void setSourceVersion(String version) {
            this.fIsVersionMatch = this.fVersion.compareTo(version) <= 0;
        }

        private static final class ResettableScanner
                implements ICharacterScanner {
            private final ICharacterScanner fDelegate;
            private int fReadCount;

            public ResettableScanner(ICharacterScanner scanner) {
                Assert.isNotNull(scanner);
                this.fDelegate = scanner;
                mark();
            }

            @Override
            public int getColumn() {
                return this.fDelegate.getColumn();
            }

            @Override
            public char[][] getLegalLineDelimiters() {
                return this.fDelegate.getLegalLineDelimiters();
            }

            @Override
            public int read() {
                int ch = this.fDelegate.read();
                if (ch != -1)
                    this.fReadCount += 1;
                return ch;
            }

            @Override
            public void unread() {
                if (this.fReadCount > 0)
                    this.fReadCount -= 1;
                this.fDelegate.unread();
            }

            public void mark() {
                this.fReadCount = 0;
            }

            public void reset() {
                while (this.fReadCount > 0) {
                    unread();
                }
                while (this.fReadCount < 0)
                    read();
            }
        }
    }

    private static final class BracketRule
            implements IRule {
        private final char[] JAVA_BRACKETS = { '(', ')', '{', '}', '[', ']' };
        private final IToken fToken;

        public BracketRule(IToken token) {
            this.fToken = token;
        }

        public boolean isBracket(char character) {
            for (int index = 0; index < this.JAVA_BRACKETS.length; ++index) {
                if (this.JAVA_BRACKETS[index] == character)
                    return true;
            }
            return false;
        }

        @Override
        public IToken evaluate(ICharacterScanner scanner) {
            int character = scanner.read();
            if (isBracket((char) character)) {
                do
                    character = scanner.read();
                while (isBracket((char) character));
                scanner.unread();
                return this.fToken;
            }
            scanner.unread();
            return Token.UNDEFINED;
        }
    }

    private static final class OperatorRule
            implements IRule {
        private final char[] JAVA_OPERATORS = { ';', '.', '=', '/', '\\', '+', '-', '*', '<', '>', ':', '?', '!', ',', '|', '&', '^', '%', '~' };
        private final IToken fToken;

        public OperatorRule(IToken token) {
            this.fToken = token;
        }

        public boolean isOperator(char character) {
            for (int index = 0; index < this.JAVA_OPERATORS.length; ++index) {
                if (this.JAVA_OPERATORS[index] == character)
                    return true;
            }
            return false;
        }

        @Override
        public IToken evaluate(ICharacterScanner scanner) {
            int character = scanner.read();
            if (isOperator((char) character)) {
                do
                    character = scanner.read();
                while (isOperator((char) character));
                scanner.unread();
                return this.fToken;
            }
            scanner.unread();
            return Token.UNDEFINED;
        }
    }

    private static class VersionedWordMatcher extends CombinedWordRule.WordMatcher
            implements ISourceVersionDependent {
        private final IToken fDefaultToken;
        private final String fVersion;
        private boolean fIsVersionMatch;

        public VersionedWordMatcher(IToken defaultToken, String version, String currentVersion) {
            this.fDefaultToken = defaultToken;
            this.fVersion = version;
            setSourceVersion(currentVersion);
        }

        @Override
        public void setSourceVersion(String version) {
            this.fIsVersionMatch = this.fVersion.compareTo(version) <= 0;
        }

        @Override
        public IToken evaluate(ICharacterScanner scanner, CombinedWordRule.CharacterBuffer word) {
            IToken token = super.evaluate(scanner, word);

            if (this.fIsVersionMatch || token.isUndefined()) {
                return token;
            }
            return this.fDefaultToken;
        }
    }
}
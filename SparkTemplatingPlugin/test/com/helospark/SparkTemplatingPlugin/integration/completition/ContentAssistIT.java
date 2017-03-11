package com.helospark.SparkTemplatingPlugin.integration.completition;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Arrays;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.helospark.SparkTemplatingPlugin.integration.completition.support.StringBackedTestIDocument;
import com.helospark.sparktemplatingplugin.DiContainer;
import com.helospark.sparktemplatingplugin.ui.editor.completition.TemplatingToolCompletionProcessor;

public class ContentAssistIT {
    private TemplatingToolCompletionProcessor underTest;

    @BeforeTest
    public void setUp() {
        DiContainer.clearDiContainer();
        DiContainer.initializeDiContainer();
        underTest = DiContainer.getDependency(TemplatingToolCompletionProcessor.class);
    }

    @Test(dataProvider = "succesfulContentAssists")
    public void testSuccesfulContentAssistAtEndOfUserTypeString(String userTypedString, String expectedSuggestion) {
        // GIVEN
        ITextViewer viewer = createTextViewer(userTypedString);

        // WHEN
        ICompletionProposal[] result = underTest.computeCompletionProposals(viewer, userTypedString.length());

        // THEN
        boolean resultContainsExpected = containsExpected(result, expectedSuggestion);
        Assert.assertTrue(resultContainsExpected, expectedSuggestion + " is not suggested when user types " + userTypedString);
    }

    @DataProvider(name = "succesfulContentAssists")
    public static Object[][] createSuccessData() {
        return new Object[][] {
                { "", "result" },
                { "resu", "result" },
                { "SttAnnot", "SttAnnotation" },
                { "res", "result" },
                { "result", "result" },
                { "Stt", "SttAnnotation" },
                { "Stt", "SttType" },
                { "Stt", "SttMethod" },
                { "Stt", "SttJavaProject" },
                { "Stt", "SttField" },
                { "result.", "getExposedName()" },
                { "result.", "append(String)" },
                { "result.getExposedName().", "toString()" },
                { "globalConfig.S", "SCRIPT_NAME" },
                { "globalConfig.s", "SCRIPT_NAME" },
                { "codeForm", "codeFormatter" },
                { "codeFormatter.form", "formatCurrentCompilationUnit()" },
                //  { "SttJavaProject.findType(\"package\", \"clazz\")", "getClass()" } // will be done as part of a bug
        };
    }

    @Test(dataProvider = "failedContentAssists")
    public void testFailedContentAssistAtEndOfUserTypeStringShouldNotShowGivenSuggestion(String userTypedString, String notShownSuggestion) {
        // GIVEN
        ITextViewer viewer = createTextViewer(userTypedString);

        // WHEN
        ICompletionProposal[] result = underTest.computeCompletionProposals(viewer, userTypedString.length());

        // THEN
        boolean resultContainsExpected = containsExpected(result, notShownSuggestion);
        Assert.assertFalse(resultContainsExpected, notShownSuggestion + " is suggested when user types " + userTypedString);
    }

    @DataProvider(name = "failedContentAssists")
    public static Object[][] createFailedData() {
        return new Object[][] {
                { "resu", "SttAnnotation" },
                { "ra", "result" },
                { "Stt", "result" },
                { "result.", "result" },
                { "result.", "SttAnnotation" },
                { "", "getClass()" },
                { ".", "getClass()" }
        };
    }

    // TODO: test cases for autosuggest not at the end of line

    private boolean containsExpected(ICompletionProposal[] result, String expectedSuggestion) {
        return Arrays.stream(result)
                .filter(proposal -> proposal.getDisplayString().equals(expectedSuggestion))
                .findFirst()
                .isPresent();
    }

    private ITextViewer createTextViewer(String userTypedString) {
        IDocument document = new StringBackedTestIDocument(userTypedString);
        ITextViewer viewer = mock(ITextViewer.class);
        given(viewer.getDocument()).willReturn(document);
        return viewer;
    }
}

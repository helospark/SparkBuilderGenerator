package com.helospark.spark.builder.handlers.codegenerator.component.helper;

import static java.util.Optional.of;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Optional;

import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.helospark.spark.builder.DiContainer;

public class VariableDeclarationToFieldNameConverterIT {
    private FieldNameToBuilderFieldNameConverter underTest;

    @Mock
    private VariableDeclarationFragment fragment;
    @Mock
    private SimpleName simpleName;
    @Mock
    private PreferenceStoreProvider preferenceStoreProvider;
    @Mock
    private PreferenceStoreWrapper preferenceStoreWrapper;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
        DiContainer.clearDiContainer();

        // Override mock dependencies

        DiContainer.addDependency(preferenceStoreProvider);

        // end of overrides

        DiContainer.initializeDiContainer();
        underTest = DiContainer.getDependency(FieldNameToBuilderFieldNameConverter.class);

        given(preferenceStoreProvider.providePreferenceStore()).willReturn(preferenceStoreWrapper);
    }

    @Test(dataProvider = "inputDataWithReplacements")
    public void testReplacePrefixAndPostfix(String input, String expectedResult) {
        // GIVEN
        given(preferenceStoreWrapper.getString("org.eclipse.jdt.core.codeComplete.fieldPrefixes")).willReturn(of("prefix, a,,"));
        given(preferenceStoreWrapper.getString("org.eclipse.jdt.core.codeComplete.fieldSuffixes")).willReturn(of("Postfix, B  "));
        given(preferenceStoreWrapper.getBoolean("org.helospark.builder.removePrefixAndPostfixFromBuilderNames")).willReturn(true);

        // WHEN
        String result = underTest.convertFieldName(input);

        // THEN
        assertEquals(expectedResult, result);
    }

    @DataProvider(name = "inputDataWithReplacements")
    private Object[][] inputDataWithReplacements() {
        return new Object[][] {
                { "prefixField", "field" },
                { "aField", "field" },
                { "fieldPostfix", "field" },
                { "fieldB", "field" },
                { "prefix", "prefix" },
                { "a", "a" },
                { "B", "B" },
                { "Postfix", "Postfix" },
                { "aFieldB", "field" },
                { "prefixFieldB", "field" },
                { "aFIeldB", "fIeld" },
                { "field", "field" },
                { "notPrefixField", "notPrefixField" },
                { "aaField", "aaField" },
                { "fieldpostfix", "fieldpostfix" }
        };
    }

    @Test(dataProvider = "testDataForNoReplace")
    public void testShouldNotReplaceAnythingWhenPrefixAndPostfixListIsEmpty(String input) {
        // GIVEN
        given(preferenceStoreWrapper.getString("org.eclipse.jdt.core.codeComplete.fieldPrefixes")).willReturn(Optional.empty());
        given(preferenceStoreWrapper.getString("org.eclipse.jdt.core.codeComplete.fieldSuffixes")).willReturn(of(""));
        given(preferenceStoreWrapper.getBoolean("org.helospark.builder.removePrefixAndPostfixFromBuilderNames")).willReturn(true);

        // WHEN
        String result = underTest.convertFieldName(input);

        // THEN
        assertEquals(input, result);
    }

    @Test(dataProvider = "testDataForNoReplace")
    public void testShouldNotReplaceAnythingWhenRemovePrefixAndPostfixPropertyIsDisabled(String input) {
        // GIVEN
        given(preferenceStoreWrapper.getString("org.eclipse.jdt.core.codeComplete.fieldPrefixes")).willReturn(of("prefix, a"));
        given(preferenceStoreWrapper.getString("org.eclipse.jdt.core.codeComplete.fieldSuffixes")).willReturn(of("Postfix, B"));
        given(preferenceStoreWrapper.getBoolean("org.helospark.builder.removePrefixAndPostfixFromBuilderNames")).willReturn(false);

        // WHEN
        String result = underTest.convertFieldName(input);

        // THEN
        assertEquals(input, result);
    }

    @DataProvider(name = "testDataForNoReplace")
    private Object[][] testDataForNoReplace() {
        return new Object[][] {
                { "prefixField" },
                { "aField" },
                { "fieldPostfix" },
                { "fieldB" },
                { "prefix" },
                { "a" }
        };
    }

    @Test(dataProvider = "underscoreTestDataProvider")
    public void testShouldHandleUnderscoreProperly(String input, String expectedOutput) {
        // GIVEN
        given(preferenceStoreWrapper.getString("org.eclipse.jdt.core.codeComplete.fieldPrefixes")).willReturn(of("_,prefix_"));
        given(preferenceStoreWrapper.getString("org.eclipse.jdt.core.codeComplete.fieldSuffixes")).willReturn(of(""));
        given(preferenceStoreWrapper.getBoolean("org.helospark.builder.removePrefixAndPostfixFromBuilderNames")).willReturn(true);

        // WHEN
        String result = underTest.convertFieldName(input);

        // THEN
        assertEquals(expectedOutput, result);
    }

    @DataProvider(name = "underscoreTestDataProvider")
    private Object[][] underscoreTestDataProvider() {
        return new Object[][] {
                { "_prefixField", "prefixField" },
                { "prefix_field", "field" },
                { "_Field", "field" },
                { "_", "_" }
        };
    }
}

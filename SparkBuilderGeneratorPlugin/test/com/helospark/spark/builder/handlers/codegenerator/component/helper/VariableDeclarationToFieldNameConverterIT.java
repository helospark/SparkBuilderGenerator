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
    private VariableDeclarationToFieldNameConverter underTest;

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
        underTest = DiContainer.getDependency(VariableDeclarationToFieldNameConverter.class);

        given(preferenceStoreProvider.providerDefaultPreferenceStore()).willReturn(preferenceStoreWrapper);
    }

    @Test(dataProvider = "inputDataWithReplacements")
    public void testReplacePrefixAndPostfix(String input, String expectedResult) {
        // GIVEN
        given(preferenceStoreWrapper.getString("org.eclipse.jdt.core.codeComplete.fieldPrefixes")).willReturn(of("prefix, a,,"));
        given(preferenceStoreWrapper.getString("org.eclipse.jdt.core.codeComplete.fieldSuffixes")).willReturn(of("Postfix, B  "));
        given(preferenceStoreWrapper.getBoolean("org.helospark.builder.removePrefixAndPostfixFromBuilderNames")).willReturn(true);

        configureFragmentToReturnInput(input);

        // WHEN
        String result = underTest.convertToFieldName(fragment);

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

        configureFragmentToReturnInput(input);

        // WHEN
        String result = underTest.convertToFieldName(fragment);

        // THEN
        assertEquals(input, result);
    }

    @Test(dataProvider = "testDataForNoReplace")
    public void testShouldNotReplaceAnythingWhenRemovePrefixAndPostfixPropertyIsDisabled(String input) {
        // GIVEN
        given(preferenceStoreWrapper.getString("org.eclipse.jdt.core.codeComplete.fieldPrefixes")).willReturn(of("prefix, a"));
        given(preferenceStoreWrapper.getString("org.eclipse.jdt.core.codeComplete.fieldSuffixes")).willReturn(of("Postfix, B"));
        given(preferenceStoreWrapper.getBoolean("org.helospark.builder.removePrefixAndPostfixFromBuilderNames")).willReturn(false);

        configureFragmentToReturnInput(input);

        // WHEN
        String result = underTest.convertToFieldName(fragment);

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

    private void configureFragmentToReturnInput(String input) {
        given(fragment.getName()).willReturn(simpleName);
        given(simpleName.toString()).willReturn(input);
    }
}

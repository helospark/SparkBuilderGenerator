package com.helospark.integrationtest;

import static com.helospark.integrationtest.helper.DefaultPreferenceValueSetterTestHelper.setDefaultTestValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.MockitoAnnotations.initMocks;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.helospark.integrationtest.helper.CompilationUnitHelper;
import com.helospark.spark.builder.DiContainer;
import com.helospark.spark.builder.handlers.CompilationUnitSourceSetter;
import com.helospark.spark.builder.handlers.DialogWrapper;
import com.helospark.spark.builder.handlers.GenerateBuilderHandler;
import com.helospark.spark.builder.handlers.HandlerUtilWrapper;
import com.helospark.spark.builder.handlers.WorkingCopyManagerWrapper;
import com.helospark.spark.builder.preferences.PreferencesManager;

public class BaseTest {
    private ExecutionEvent dummyExecutionEvent = new ExecutionEvent();
    @Mock
    private DialogWrapper dialogWrapper;
    @Mock
    private PreferencesManager preferencesManager;
    @Mock
    private CompilationUnitSourceSetter compilationUnitSourceSetter;
    @Mock
    private HandlerUtilWrapper handlerUtilWrapper;
    @Mock
    private WorkingCopyManagerWrapper workingCopyManagerWrapper;

    private DiContainer diContainer;
    private GenerateBuilderHandler generateBuilderHandler;

    @Before
    public void setUp() {
        initMocks(this);
        setDefaultTestValue(preferencesManager);
        given(handlerUtilWrapper.getActivePartId(any())).willReturn("org.eclipse.jdt.ui.CompilationUnitEditor");

        // Initialize mock dependencies
        DiContainer.addDepenency(dialogWrapper);
        DiContainer.addDepenency(preferencesManager);
        DiContainer.addDepenency(handlerUtilWrapper);
        DiContainer.addDepenency(workingCopyManagerWrapper);

        // Initialize rest of dependencies
        DiContainer.initializeDiContainer();
        generateBuilderHandler = new GenerateBuilderHandler();
    }

    @Test
    public void testWithDefaultValues() throws ExecutionException {
        // GIVEN
        given(workingCopyManagerWrapper.getCurrentCompilationUnit(any())).willReturn(CompilationUnitHelper.loadTestFile("asd"));

        // WHEN
        generateBuilderHandler.execute(dummyExecutionEvent);

        // THEN
    }
}

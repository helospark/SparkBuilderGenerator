package com.helospark.spark.builder.handlers.it;

import static java.util.Optional.of;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import com.helospark.spark.builder.DiContainer;
import com.helospark.spark.builder.handlers.DialogWrapper;
import com.helospark.spark.builder.handlers.HandlerUtilWrapper;
import com.helospark.spark.builder.handlers.WorkingCopyManagerWrapper;
import com.helospark.spark.builder.handlers.codegenerator.CompilationUnitParser;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.PreferenceStoreProvider;
import com.helospark.spark.builder.handlers.codegenerator.component.helper.PreferenceStoreWrapper;

public class BaseBuilderGeneratorIT {
	protected ExecutionEvent dummyExecutionEvent = new ExecutionEvent();

	protected AbstractHandler underTest;

	@Mock
	protected ICompilationUnit iCompilationUnit;
	@Mock
	protected HandlerUtilWrapper handlerUtilWrapper;
	@Mock
	protected WorkingCopyManagerWrapper workingCopyManagerWrapper;
	@Mock
	protected CompilationUnitParser compilationUnitParser;
	@Mock
	protected PreferenceStoreProvider preferenceStoreProvider;
	@Mock
	protected PreferenceStoreWrapper preferenceStore;
	@Mock
	protected DialogWrapper dialogWrapper;
	@Mock
	protected IBuffer iBuffer;
	@Captor
	protected ArgumentCaptor<String> outputCaptor;

	protected void init() throws JavaModelException {
		initMocks(this);
		DiContainer.clearDiContainer();

		// Override mock dependencies

		diContainerOverrides();

		// end of overrides

		DiContainer.initializeDiContainer();

		given(handlerUtilWrapper.getActivePartId(dummyExecutionEvent))
				.willReturn("org.eclipse.jdt.ui.CompilationUnitEditor");
		given(workingCopyManagerWrapper.getCurrentCompilationUnit(dummyExecutionEvent)).willReturn(iCompilationUnit);
		given(preferenceStoreProvider.providePreferenceStore()).willReturn(preferenceStore);
		given(iCompilationUnit.getBuffer()).willReturn(iBuffer);
		setDefaultPreferenceStoreSettings();
		doNothing().when(iBuffer).setContents(outputCaptor.capture());

		DiContainer.initializeDiContainer();
	}

	protected void diContainerOverrides() {
		DiContainer.addDependency(handlerUtilWrapper);
		DiContainer.addDependency(workingCopyManagerWrapper);
		DiContainer.addDependency(compilationUnitParser);
		DiContainer.addDependency(preferenceStoreProvider);
		DiContainer.addDependency(dialogWrapper);
	}

	protected void setInput(String sourceAsString) throws JavaModelException {
		char[] source = sourceAsString.toCharArray();
		CompilationUnit cu = parseAst(source);
		given(compilationUnitParser.parse(iCompilationUnit)).willReturn(cu);
		given(iCompilationUnit.getSource()).willReturn(sourceAsString);
	}

	protected void setDefaultPreferenceStoreSettings() {
		given(preferenceStore.getBoolean("override_previous_builder")).willReturn(true);
		given(preferenceStore.getString("create_builder_method_pattern")).willReturn(of("builder"));
		given(preferenceStore.getString("builder_class_name_pattern")).willReturn(of("Builder"));
		given(preferenceStore.getString("build_method_name")).willReturn(of("build"));
		given(preferenceStore.getString("builders_method_name_pattern")).willReturn(of("with[FieldName]"));
		given(preferenceStore.getBoolean("generate_javadoc_on_builder_method")).willReturn(false);
		given(preferenceStore.getBoolean("generate_javadoc_on_builder_class")).willReturn(false);
		given(preferenceStore.getBoolean("generate_javadoc_on_each_builder_method")).willReturn(false);
		given(preferenceStore.getBoolean("add_nonnull_on_return")).willReturn(false);
		given(preferenceStore.getBoolean("add_nonnull_on_parameter")).willReturn(false);
		given(preferenceStore.getBoolean("add_generated_annotation")).willReturn(false);
		given(preferenceStore.getBoolean("org.helospark.builder.removePrefixAndPostfixFromBuilderNames"))
				.willReturn(false);
	}

	protected CompilationUnit parseAst(char[] source) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(source);
		Map options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_5, options);
		parser.setCompilerOptions(options);
		CompilationUnit result = (CompilationUnit) parser.createAST(null);
		return result;
	}

	protected void assertEqualsJavaContents(String actualValue, String expectedValue) {
		// making sure that the formatting will not matter in the comparition
		// we parse and format them the same way
		String actual = parseAst(actualValue.toCharArray()).toString();
		String expected = parseAst(expectedValue.toCharArray()).toString();
		assertEquals(actual, expected);
	}

	public String readClasspathFile(String fileName) throws IOException, URISyntaxException {
		Path uri = Paths.get(this.getClass().getResource("/" + fileName).toURI());
		return new String(Files.readAllBytes(uri), Charset.forName("UTF-8"));
	}
}

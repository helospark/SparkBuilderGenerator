package com.helospark.sparktemplatingplugin.execute.templater;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.helospark.sparktemplatingplugin.IDocumented;
import com.helospark.sparktemplatingplugin.execute.templater.helper.CompilationUnitCreator;
import com.helospark.sparktemplatingplugin.execute.templater.helper.PackageRootFinder;
import com.helospark.sparktemplatingplugin.execute.templater.provider.CompilationUnitProvider;
import com.helospark.sparktemplatingplugin.execute.templater.provider.CurrentProjectProvider;

public class TemplatingResult implements ScriptExposed, IDocumented {
    public static final String SCRIPT_NAME = "result";
    // Stateless
    private CompilationUnitProvider compilationUnitProvider = new CompilationUnitProvider();
    private CompilationUnitCreator compilationUnitCreator = new CompilationUnitCreator();
    private PackageRootFinder packageRootFinder = new PackageRootFinder();
    private CurrentProjectProvider currentProjectProvider = new CurrentProjectProvider();

    // Stateful
    private StringBuilder buffer = new StringBuilder();
    private ExecutionEvent event;

    public TemplatingResult(CompilationUnitProvider compilationUnitProvider, CompilationUnitCreator compilationUnitCreator, PackageRootFinder packageRootFinder,
            ExecutionEvent event) {
        this.compilationUnitProvider = compilationUnitProvider;
        this.compilationUnitCreator = compilationUnitCreator;
        this.packageRootFinder = packageRootFinder;
        this.event = event;
    }

    public void append(String data) {
        buffer.append(data);
    }

    public void appendToCurrentPosition() {
        try {
            ICompilationUnit iCompilationUnit = compilationUnitProvider.provideCurrentICompiltionUnit(event).getRawCompilationUnit();
            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            IEditorPart activeEditor = page.getActiveEditor();
            if (activeEditor instanceof JavaEditor) {
                int offset = ((TextSelection) ((JavaEditor) activeEditor).getSelectionProvider().getSelection()).getOffset();
                String source = iCompilationUnit.getBuffer().getContents();
                String resultSource = source.substring(0, offset) +
                        buffer.toString() +
                        source.substring(offset + 1);
                iCompilationUnit.getBuffer().setContents(resultSource);
            }
            clearBuffer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void appendToNewFile(IProject iProject, String folder) {
        // IPackageFragmentRoot rootPackage =
        // packageRootFinder.findSrcPackageFragmentRoot(iJavaProject);
        // compilationUnitCreator.createCompilationUnit(rootPackage,
        // "com.helospark.test", "TestClass.java", program);
        try {
            IProject project = currentProjectProvider.provideCurrentProject();

            String[] parts = folder.split("/");

            IFolder src = null;
            for (int i = 0; i < parts.length - 1; ++i) {
                src = project.getFolder(parts[i]);
                if (!src.exists()) {
                    src.create(true, true, new NullProgressMonitor());
                }
            }

            IFile file = src.getFile(parts[parts.length - 1]);
            file.create(new ByteArrayInputStream(buffer.toString().getBytes(Charset.forName("UTF-8"))), IResource.FORCE, new NullProgressMonitor());
            clearBuffer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void appendToMethod(ICompilationUnit iCompilationUnit, String program, CompilationUnit astRoot) throws JavaModelException, BadLocationException {
        AST ast = astRoot.getAST();
        ASTRewrite rewriter = ASTRewrite.create(ast);
        // parse compilation unit

        TypeDeclaration typeDecl = (TypeDeclaration) astRoot.types().get(0);
        MethodDeclaration methodDecl = typeDecl.getMethods()[0];
        Block block = methodDecl.getBody();

        ListRewrite listRewrite = rewriter.getListRewrite(methodDecl, Block.STATEMENTS_PROPERTY);
        Statement placeholder = (Statement) rewriter.createStringPlaceholder(program, ASTNode.EMPTY_STATEMENT);
        listRewrite.insertFirst(placeholder, null);
        TextEdit edits = rewriter.rewriteAST();

        // apply the text edits to the compilation unit
        Document document = new Document(iCompilationUnit.getSource());

        edits.apply(document);

        // this is the code for adding statements
        iCompilationUnit.getBuffer().setContents(document.get());
    }

    public void clearBuffer() {
        buffer.setLength(0);
    }

    @Override
    public String getExposedName() {
        return SCRIPT_NAME;
    }

    @Override
    public String getDocumentation() {
        return "<h1>Result</h1><p>This contains the result, every templated text will automatically"
                + " gets appended to it. You can flush it manually, or it will be automatically flushed"
                + " at the current cursor position in the end of the script.";
    }

}

package io.github.henriquesmoco.workitemfieldhistory.tests;

import static org.junit.Assert.*;
import io.github.henriquesmoco.workitemfieldhistory.views.FieldHistoryView;

import org.eclipse.ui.PlatformUI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FieldHistoryEditorTest {
	
	private FieldHistoryView view; 
	
	@Before
	public void setUp() throws Exception {
		view = (FieldHistoryView) PlatformUI
	        .getWorkbench()
	        .getActiveWorkbenchWindow()
	        .getActivePage()
	        .showView(FieldHistoryView.ID);
	}

	@After
	public void tearDown() throws Exception {
		PlatformUI
        .getWorkbench()
        .getActiveWorkbenchWindow()
        .getActivePage()
        .hideView(view);
	}

	@Test
	public void digitar_numerosNoCampoWorkItemId_atualizaCampo() {
		view.setWorkItemId("123");
		String editorText = view.getWorkItemIdText();
		assertEquals("123", editorText);
	}
	
	@Test
	public void digitar_caracteresInvalidosNoCampoWorkItemId_desabilitaBotaoLoad() {
		view.setWorkItemId("abc");
		assertFalse(view.isShowRevisionsEnabled());
	}

}

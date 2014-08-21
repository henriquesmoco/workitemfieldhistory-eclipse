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
	public void digitar_apenasNumerosNoCampoWorkItemId_habilitaShowRevisions() {
		view.setWorkItemId("123");
		assertTrue(view.isShowRevisionsEnabled());
	}
	
	@Test
	public void digitar_letrasNoCampoWorkItemId_desabilitaShowRevisions() {
		view.setWorkItemId("abc");
		assertFalse(view.isShowRevisionsEnabled());
	}
	
	@Test
	public void mostrarRevisoes_deWorkItemNaoEncontrado_mostraMensagem() throws Exception {
		view.setWorkItemId("123");
		view.showRevisionsClick();
		assertEquals("[Work Item not found]", view.getWorkItemTitle());
	}

}

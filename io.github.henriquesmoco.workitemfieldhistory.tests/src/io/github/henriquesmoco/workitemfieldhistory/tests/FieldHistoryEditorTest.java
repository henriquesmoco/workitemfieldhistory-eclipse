package io.github.henriquesmoco.workitemfieldhistory.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import io.github.henriquesmoco.workitemfieldhistory.views.FieldHistoryView;
import io.github.henriquesmoco.workitemfieldhistory.views.TfsManager;
import io.github.henriquesmoco.workitemfieldhistory.views.WorkItemDTO;

import org.eclipse.ui.PlatformUI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FieldHistoryEditorTest {
	
	private FieldHistoryView view;
	private TfsManager manager;
	
	@Before
	public void setUp() throws Exception {
		view = (FieldHistoryView) PlatformUI
	        .getWorkbench()
	        .getActiveWorkbenchWindow()
	        .getActivePage()
	        .showView(FieldHistoryView.ID);
		manager = mock(TfsManager.class);
		view.setTfsManager(manager);
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
	
	@Test
	public void mostrarRevisoes_deWorkItemEncontrado_mostraTituloDoWorkItem() throws Exception {
		String title = "Alguma Issue";
		WorkItemDTO wi = new WorkItemDTO();
		wi.setTitle(title);
		when(manager.getWorkItem(123)).thenReturn(wi);
		
		view.setWorkItemId("123");
		view.showRevisionsClick();
		
		assertEquals(title, view.getWorkItemTitle());
	}

}

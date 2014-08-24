package io.github.henriquesmoco.workitemfieldhistory.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import io.github.henriquesmoco.workitemfieldhistory.core.RevisionItem;
import io.github.henriquesmoco.workitemfieldhistory.core.TfsManager;
import io.github.henriquesmoco.workitemfieldhistory.core.WorkItemDTO;
import io.github.henriquesmoco.workitemfieldhistory.views.FieldHistoryView;

import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.ui.PlatformUI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.verification.AtLeast;

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
		close(view);
	}

	private void close(FieldHistoryView view) {
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
		WorkItemDTO wi = newWorkItem();
		when(manager.getWorkItem(123)).thenReturn(wi);
		
		view.setWorkItemId("123");
		view.showRevisionsClick();
		
		assertEquals(wi.getTitle(), view.getWorkItemTitle());
	}

	@Test
	public void mostrarRevisoes_deWorkItemSemRevisoes_deixaGridVazio() throws Exception {
		when(manager.getWorkItem(anyInt())).thenReturn(newWorkItem());
		
		view.setWorkItemId("123");
		view.showRevisionsClick();
		
		assertTrue(view.getGridItems().isEmpty());
	}
	
	@Test
	public void mostrarRevisoes_deWorkItemComRevisoes_populaGridAgrupandoPorCampos() throws Exception {
		when(manager.getWorkItem(anyInt())).thenReturn(newWorkItemWithRevisions());
		
		view.setWorkItemId("123");
		view.showRevisionsClick();
		
		List<GridItem> gridItems = view.getGridItems();
		GridItem rootField1 = gridItems.get(0);
		assertEquals(rootField1, gridItems.get(1).getParentItem());		
		GridItem rootField2 = gridItems.get(2);
		assertEquals(rootField2, gridItems.get(3).getParentItem());
		assertEquals(rootField2, gridItems.get(4).getParentItem());
	}
	
	@Test
	public void mostrarRevisoes_deWorkItemComRevisoes_AtualizaFiltroComCamposAlterados() throws Exception {
		when(manager.getWorkItem(anyInt())).thenReturn(newWorkItemWithRevisions());
		
		view.setWorkItemId("123");
		view.showRevisionsClick();
		
		String[] expected = new String[] { "All Fields", "field1", "field2" };
		assertArrayEquals(expected, view.getFilters());	
	}
	
	@Test
	public void filtrarGrid_porTodosOsCampos_mostraTodosCampos() throws Exception {
		when(manager.getWorkItem(anyInt())).thenReturn(newWorkItemWithRevisions());
		
		view.setWorkItemId("123");
		view.showRevisionsClick();
		view.selectFilter("All Fields");
		
		assertEquals(5, view.getGridItems().size());
	}
	
	@Test
	public void filtrarGrid_porUmCampoEspecifico_soMostraRevisoesDesteCampo() throws Exception {
		when(manager.getWorkItem(anyInt())).thenReturn(newWorkItemWithRevisions());
		
		view.setWorkItemId("123");
		view.showRevisionsClick();
		view.selectFilter("field2");
		
		List<GridItem> gridItems = view.getGridItems();
		GridItem rootField = gridItems.get(0);
		assertEquals(3, gridItems.size());
		assertEquals("field2", rootField.getText());
		assertEquals(rootField, gridItems.get(1).getParentItem());
		assertEquals(rootField, gridItems.get(2).getParentItem());
	}
	
	@Test
	public void fecharView_independenteDeConexaoComTfs_deveChamarDisposeNoTfsManager() throws Exception {
		close(view);
		
		verify(manager, atLeastOnce()).dispose();
	}
	
	private WorkItemDTO newWorkItemWithRevisions() {
		WorkItemDTO wi = newWorkItem();
		wi.setRevisions(Arrays.asList(
				newRevision("field1", 1, "new1", "old1"),
				newRevision("field2", 1, "new2", "old2"),
				newRevision("field2", 2, "new3", "old3")
				));
		return wi;
	}
	private WorkItemDTO newWorkItem() {
		WorkItemDTO wi = new WorkItemDTO();
		wi.setTitle("Alguma Issue");
		return wi;
	}
	
	private RevisionItem newRevision(String fieldName, int rev, String newValue, String oldValue) {
		RevisionItem revItem = new RevisionItem();
		revItem.rev = rev;
		revItem.revisedBy = "Developer";
		revItem.revisionDate = LocalDateTime.of(2014, Month.AUGUST, 8, 3, 0);
		revItem.newValue = newValue;
		revItem.oldValue = oldValue;
		revItem.setFieldName(fieldName);
		return revItem;
	}
}

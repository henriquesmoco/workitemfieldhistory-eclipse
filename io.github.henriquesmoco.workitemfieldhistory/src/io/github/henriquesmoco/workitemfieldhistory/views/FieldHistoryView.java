package io.github.henriquesmoco.workitemfieldhistory.views;

import io.github.henriquesmoco.workitemfieldhistory.core.RevisionItem;
import io.github.henriquesmoco.workitemfieldhistory.core.TfsManager;
import io.github.henriquesmoco.workitemfieldhistory.core.TfsManagerImpl;
import io.github.henriquesmoco.workitemfieldhistory.core.WorkItemDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import com.microsoft.tfs.client.common.ui.framework.helper.SWTUtil;


public class FieldHistoryView extends ViewPart {
	public static final String ID = "io.github.henriquesmoco.workitemfieldhistory.views.FieldHistoryView";
	private static final Pattern ONLY_DIGITS = Pattern.compile("\\d+");
	private Text txtWorkItemId;
	private Button btnShowRevisions;
	private Group grpWorkItem;
	private Grid gridRevisions;
	
	private TfsManager tfsManager = new TfsManagerImpl();
	
	@Override
	public void createPartControl(Composite composite) {
		SWTUtil.gridLayout(composite, 3, false, 5, 5);		
		createTextWorItemIdIn(composite);
		createButtonShowRevisionsIn(composite);		
		createGridRevisionsIn(composite);
	}
	
	private void createTextWorItemIdIn(Composite composite) {
		Label lblId = new Label(composite, SWT.LEFT);
		lblId.setText("Work Item ID:");
		txtWorkItemId = new Text(composite, SWT.SINGLE | SWT.BORDER);
		txtWorkItemId.addModifyListener(evt -> {
			String eventText = ((Text)evt.getSource()).getText();
			Optional<String> str = Optional.ofNullable(eventText);
			boolean hasOnlyDigits = ONLY_DIGITS.matcher(str.orElse("")).matches();
			btnShowRevisions.setEnabled(hasOnlyDigits);
		});	
	}

	private void createButtonShowRevisionsIn(Composite composite) {
		btnShowRevisions = new Button(composite, SWT.PUSH);
		btnShowRevisions.setText("Show Revision(s)");
		btnShowRevisions.setEnabled(false);
		btnShowRevisions.addListener(SWT.Selection, evt -> {
			long id = Long.parseLong(txtWorkItemId.getText());
			WorkItemDTO wi = tfsManager.getWorkItem(id);
			updateView(wi);
		});		
	}

	private void createGridRevisionsIn(Composite composite) {
		grpWorkItem = new Group(composite, SWT.NONE);
		grpWorkItem.setText("");
		grpWorkItem.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 0));
		grpWorkItem.setLayout(new FillLayout());
		
		gridRevisions = new Grid(grpWorkItem,SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		gridRevisions.setHeaderVisible(true);		
		createGridColumn("Rev", 100).setTree(true);
	    createGridColumn("Revised By", 100);
	    createGridColumn("Revision Date", 100);
	    createGridColumn("New Value", 100);
	    createGridColumn("Old Value", 100);
	}
	
	private GridColumn createGridColumn(String title, int width) {
	    GridColumn col = new GridColumn(gridRevisions,SWT.NONE);
	    col.setText(title);
	    col.setWidth(width);
	    return col;
	}
	
	private void updateView(WorkItemDTO wi) {
		String wiTitle;
		if (wi == null) {
			wiTitle = "[Work Item not found]";
		} else {
			wiTitle = wi.getTitle();
			updateGridWith(wi.getRevisions());
		}
		grpWorkItem.setText(wiTitle);
	}	

	private void updateGridWith(List<RevisionItem> revisions) {
		Map<String, List<RevisionItem>> groupedRevisions = groupByFieldName(revisions);
		
		groupedRevisions.keySet().stream().sorted().forEachOrdered(key -> {
			GridItem root = new GridItem(gridRevisions, SWT.NONE);
		    root.setText(key);
		    root.setColumnSpan(0, 4);
		    
		    for (RevisionItem revItem : groupedRevisions.get(key)) {
		    	GridItem gridItem = new GridItem(root, SWT.NONE);
				gridItem.setText(String.valueOf(revItem.rev));
				gridItem.setText(1, revItem.revisedBy);
				gridItem.setText(2, revItem.revisionDate.toString());
				gridItem.setText(3, revItem.newValue);
				gridItem.setText(4, revItem.oldValue);	
			}
		});
	}
	
	private Map<String, List<RevisionItem>> groupByFieldName(List<RevisionItem> revs) {
		Map<String, List<RevisionItem>> revsGrouped = 
				revs.stream().collect(Collectors.groupingBy(
						RevisionItem::getFieldName,               
			            Collectors.toList()));
		return revsGrouped;
	}
	
	@Override
	public void setFocus() {
		txtWorkItemId.setFocus();		
	}
	
	//----------------------------
	// Methods used for automation
	//----------------------------
	
	public void setTfsManager(TfsManager manager) {
		tfsManager = manager;
	}
	
	public void setWorkItemId(String text) {
		txtWorkItemId.setText(text);
	}
	
	public String getWorkItemIdText() {
		return txtWorkItemId.getText();
	}

	public boolean isShowRevisionsEnabled() {
		return btnShowRevisions.isEnabled();
	}

	public void showRevisionsClick() {
		btnShowRevisions.notifyListeners(SWT.Selection, null);
	}

	public String getWorkItemTitle() {
		return grpWorkItem.getText();
	}

}

package io.github.henriquesmoco.workitemfieldhistory.views;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Pattern;









import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;









import com.microsoft.tfs.client.common.ui.framework.helper.SWTUtil;
import com.microsoft.tfs.core.clients.workitem.WorkItem;import com.microsoft.tfs.core.clients.workitem.revision.RevisionField;


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
			WorkItem wi = tfsManager.getWorkItem(id);
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
	
	private void updateView(WorkItem wi) {
		String wiTitle;
		if (wi == null) {
			wiTitle = "[Work Item not found]";
		} else {
			wiTitle = wi.getTitle();
			updateGridRevisons(wi);
		}
		grpWorkItem.setText(wiTitle);
	}
	
	private void updateGridRevisons(WorkItem wi) {
		Map<String, GridItem> mapRoots = new TreeMap<>();
		List<RevisionItem> lst = getRevisionsFrom(wi);
		
		for (RevisionItem revItem : lst) {
			GridItem root = mapRoots.get(revItem.fieldName);
			if (root == null) {
				root = new GridItem(gridRevisions,SWT.NONE);
			    root.setText(revItem.fieldName);
			    root.setColumnSpan(0, 4);
			    mapRoots.put(revItem.fieldName, root);
			}
			GridItem gridItem = new GridItem(root,SWT.NONE);
			gridItem.setText(String.valueOf(revItem.rev));
			gridItem.setText(1, revItem.revisedBy);
			gridItem.setText(2, revItem.revisionDate.toString());
			gridItem.setText(3, revItem.newValue);
			gridItem.setText(4, revItem.oldValue);
		}
	}
	
	private List<RevisionItem> getRevisionsFrom(WorkItem wi) {
		List<RevisionItem> lst = new ArrayList<>();
		wi.getRevisions().forEach(item -> {
			int rev = (Integer) item.getField("System.Rev").getValue();
			String changedBy = (String) item.getField("System.ChangedBy").getValue();
			LocalDateTime revDate = toLocalDateTime(item.getRevisionDate());
			for (RevisionField field : item.getFields()) {
				if (shouldIgnore(field)) continue;
				
				RevisionItem revItem = new RevisionItem();
				revItem.rev = rev;
				revItem.revisedBy = changedBy;
				revItem.revisionDate = revDate;
				revItem.fieldName = field.getName();
				revItem.newValue = field.getValue() == null ? "" : field.getValue().toString();
				revItem.oldValue = field.getOriginalValue() == null ? "" : field.getOriginalValue().toString();
				lst.add(revItem);
			};
		});
		return lst;
	}
	
	private LocalDateTime toLocalDateTime(Date date) {
		Instant instant = Instant.ofEpochMilli(date.getTime());
		LocalDateTime localDt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		return localDt;
	}
	
	private boolean shouldIgnore(RevisionField field) {
		Optional<Object> value = Optional.ofNullable(field.getValue());
		Optional<Object> oldValue = Optional.ofNullable(field.getOriginalValue());
		return field.shouldIgnoreForDeltaTable() || 
				value.orElse("").equals(oldValue.orElse(""));
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

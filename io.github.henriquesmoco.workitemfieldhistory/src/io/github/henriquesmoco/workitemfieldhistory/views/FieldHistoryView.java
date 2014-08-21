package io.github.henriquesmoco.workitemfieldhistory.views;

import java.util.Optional;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import com.microsoft.tfs.core.clients.workitem.WorkItem;

public class FieldHistoryView extends ViewPart {
	public static final String ID = "io.github.henriquesmoco.workitemfieldhistory.views.FieldHistoryView";
	private static final Pattern ONLY_DIGITS = Pattern.compile("\\d+");
	private Text txtWorkItemId;
	private Button btnShowRevisions;
	private Group grpWorkItem;
	
	private TfsManager tfsManager;
	
	@Override
	public void createPartControl(Composite composite) {
		Label lblId = new Label(composite, SWT.LEFT);
		lblId.setText("Work Item ID:");
		txtWorkItemId = new Text(composite, SWT.SINGLE | SWT.BORDER);
		txtWorkItemId.addModifyListener(evt -> {
			String eventText = ((Text)evt.getSource()).getText();
			Optional<String> str = Optional.ofNullable(eventText);
			boolean hasOnlyDigits = ONLY_DIGITS.matcher(str.orElse("")).matches();
			btnShowRevisions.setEnabled(hasOnlyDigits);
		});
		
		btnShowRevisions = new Button(composite, SWT.PUSH);
		btnShowRevisions.setText("Show Revision(s)");
		btnShowRevisions.setEnabled(false);
		btnShowRevisions.addListener(SWT.Selection, evt -> {
			long id = Long.parseLong(txtWorkItemId.getText());
			WorkItem wi = tfsManager.getWorkItem(id);
			updateView(wi);
		});
		
		grpWorkItem = new Group(composite, SWT.NONE);
		grpWorkItem.setText("");
		
		
	}
	
	private void updateView(WorkItem wi) {
		String wiTitle;
		if (wi == null) {
			wiTitle = "[Work Item not found]";
		} else {
			wiTitle = wi.getTitle();
		}
		grpWorkItem.setText(wiTitle);
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

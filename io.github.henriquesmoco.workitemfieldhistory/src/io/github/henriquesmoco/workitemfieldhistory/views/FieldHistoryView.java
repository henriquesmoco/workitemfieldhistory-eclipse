package io.github.henriquesmoco.workitemfieldhistory.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class FieldHistoryView extends ViewPart {
	public static final String ID = "io.github.henriquesmoco.workitemfieldhistory.views.FieldHistoryView";
	private Text txtWorkItemId;
	
	@Override
	public void createPartControl(Composite composite) {
		Label lblId = new Label(composite, SWT.LEFT);
		lblId.setText("WorkItem ID:");
		txtWorkItemId = new Text(composite, SWT.SINGLE | SWT.BORDER);		
	}
	
	public void setWorkItemId(String text) {
		txtWorkItemId.setText(text);
	}
	
	public String getWorkItemIdText() {
		return txtWorkItemId.getText();
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	public boolean isShowRevisionsEnabled() {
		return false;
	}

}

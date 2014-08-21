package io.github.henriquesmoco.workitemfieldhistory.views;

import java.util.Optional;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class FieldHistoryView extends ViewPart {
	public static final String ID = "io.github.henriquesmoco.workitemfieldhistory.views.FieldHistoryView";
	private static final Pattern ONLY_DIGITS = Pattern.compile("\\d+");
	private Text txtWorkItemId;
	private Button btnShowRevisions;
	
	@Override
	public void createPartControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
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
		return btnShowRevisions.isEnabled();
	}

}

package io.github.henriquesmoco.workitemfieldhistory.core;

import java.time.LocalDateTime;

public class RevisionItem {
	public long rev;
	public String revisedBy;
	public LocalDateTime revisionDate;
	public String newValue;
	public String oldValue;
	private String fieldName;
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}

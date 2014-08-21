package io.github.henriquesmoco.workitemfieldhistory.views;

import java.time.LocalDateTime;

public class RevisionItem {
	public long rev;
	public String revisedBy;
	public LocalDateTime revisionDate;
	public String newValue;
	public String oldValue;
	public String fieldName;
}

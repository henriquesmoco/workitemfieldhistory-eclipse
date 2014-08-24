package io.github.henriquesmoco.workitemfieldhistory.core;

import java.util.List;

public class WorkItemDTO {
	private int id;
	private String title;
	private String typeName;
	private List<RevisionItem> revisions;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public List<RevisionItem> getRevisions() {
		return revisions;
	}
	public void setRevisions(List<RevisionItem> revisions) {
		this.revisions = revisions;
	}
	public String getDisplayTitle() {
		return String.format("%s - %s", typeName, title);
	}
}

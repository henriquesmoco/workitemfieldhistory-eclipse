package io.github.henriquesmoco.workitemfieldhistory.views;

import java.util.List;

public class WorkItemDTO {
	private int id;
	private String title;
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
	public List<RevisionItem> getRevisions() {
		return revisions;
	}
	public void setRevisions(List<RevisionItem> revisions) {
		this.revisions = revisions;
	}	
}

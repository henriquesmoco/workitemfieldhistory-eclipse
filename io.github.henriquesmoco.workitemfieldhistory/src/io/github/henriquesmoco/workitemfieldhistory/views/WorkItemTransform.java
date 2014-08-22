package io.github.henriquesmoco.workitemfieldhistory.views;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.microsoft.tfs.core.clients.workitem.WorkItem;
import com.microsoft.tfs.core.clients.workitem.revision.RevisionField;

public class WorkItemTransform {

	public WorkItemDTO toDTO(WorkItem wi) {
		if (wi == null) return null;
		
		WorkItemDTO dto = new WorkItemDTO();
		dto.setId(wi.getID());
		dto.setTitle(wi.getTitle());
		dto.setRevisions(getRevisionsFrom(wi));
		return dto;
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
				revItem.newValue = field.getValue() == null ? "" : field.getValue().toString();
				revItem.oldValue = field.getOriginalValue() == null ? "" : field.getOriginalValue().toString();
				revItem.setFieldName(field.getName());
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
	
}

package io.github.henriquesmoco.workitemfieldhistory.views;

import com.microsoft.tfs.core.clients.workitem.WorkItem;

public interface TfsManager {
	WorkItem getWorkItem(long id);
}

package io.github.henriquesmoco.workitemfieldhistory.core;

import org.eclipse.ui.services.IDisposable;


public interface TfsManager extends IDisposable {
	WorkItemDTO getWorkItem(long id);
}

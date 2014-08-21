package io.github.henriquesmoco.workitemfieldhistory.views;

import com.microsoft.tfs.client.common.server.TFSServer;
import com.microsoft.tfs.client.common.ui.TFSCommonUIClientPlugin;
import com.microsoft.tfs.core.clients.workitem.WorkItem;

public class TfsManagerImpl implements TfsManager {
	
	private TFSServer server;
	
	private TFSServer getDefaultServer() {
		TFSServer defaultServer = TFSCommonUIClientPlugin.getDefault()
				.getProductPlugin().getServerManager().getDefaultServer();

		if (defaultServer == null) {
			throw new RuntimeException("Not connected to TFS");
		}
		return defaultServer;
	}
	
	public boolean isConnected() {
		return TFSCommonUIClientPlugin.getDefault().getProductPlugin()
				.getServerManager().containsServer(this.server);
	}

	@Override
	public WorkItem getWorkItem(long id) {
		WorkItem wi = getDefaultServer()
				.getConnection()
				.getWorkItemClient()
				.getWorkItemByID((int) id);
		
		return wi;
	}
}

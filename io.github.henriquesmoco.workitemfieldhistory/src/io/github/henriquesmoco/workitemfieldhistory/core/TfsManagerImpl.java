package io.github.henriquesmoco.workitemfieldhistory.core;

import com.microsoft.tfs.client.common.server.ServerManagerAdapter;
import com.microsoft.tfs.client.common.server.ServerManagerEvent;
import com.microsoft.tfs.client.common.server.ServerManagerListener;
import com.microsoft.tfs.client.common.server.TFSServer;
import com.microsoft.tfs.client.common.ui.TFSCommonUIClientPlugin;
import com.microsoft.tfs.client.common.ui.framework.helper.UIHelpers;
import com.microsoft.tfs.core.clients.workitem.WorkItem;

public class TfsManagerImpl implements TfsManager {	
	private TFSServer server;
	private ServerManagerListener serverManagerListener;
	private WorkItemTransform wiTransform = new WorkItemTransform();
	

	public boolean isConnected() {
		return TFSCommonUIClientPlugin.getDefault().getProductPlugin()
				.getServerManager().containsServer(this.server);
	}

	@Override
	public WorkItemDTO getWorkItem(long id) {
		if (server == null) {
			connect();
		}
		WorkItem wi = server
				.getConnection()
				.getWorkItemClient()
				.getWorkItemByID((int) id);
		
		return wiTransform.toDTO(wi);
	}
	
	private void connect() {
		server = TFSCommonUIClientPlugin.getDefault()
				.getProductPlugin().getServerManager().getDefaultServer();
		
		if (server == null) {
			throw new RuntimeException("Not connected to TFS");
		}
		if (serverManagerListener == null) {
			addServerManagerListener();
		}
	}
	
	private void addServerManagerListener() {
		this.serverManagerListener = new ServerManagerAdapter() {
			public void onServerAdded(final ServerManagerEvent event) {
				UIHelpers.runOnUIThread(true, () -> {
					if (event.getServer() == TfsManagerImpl.this.server) {
						//BaseEditor.this.setDisconnected(false);
					} else {
						if (event.getServer().connectionsEquivalent(TfsManagerImpl.this.server)) {
							TfsManagerImpl.this.server = event.getServer();
							//BaseEditor.this.setDisconnected(false);
						}
					}
				});
			}

			public void onServerRemoved(final ServerManagerEvent event) {
				UIHelpers.runOnUIThread(true, () -> {
					if (event.getServer() == TfsManagerImpl.this.server) {
						TfsManagerImpl.this.server = null;
						//BaseEditor.this.setDisconnected(true);
					}
				});
			}
		};
		TFSCommonUIClientPlugin.getDefault().getProductPlugin()
		.getServerManager().addListener(this.serverManagerListener);
	}

	@Override
	public void dispose() {
		if (this.serverManagerListener != null) {
			TFSCommonUIClientPlugin.getDefault().getProductPlugin()
					.getServerManager()
					.removeListener(this.serverManagerListener);
			this.serverManagerListener = null;
		}
	}	
}

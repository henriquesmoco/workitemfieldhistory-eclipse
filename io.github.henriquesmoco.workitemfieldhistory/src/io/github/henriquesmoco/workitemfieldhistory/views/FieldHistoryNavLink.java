package io.github.henriquesmoco.workitemfieldhistory.views;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.microsoft.tfs.client.common.ui.teamexplorer.TeamExplorerContext;
import com.microsoft.tfs.client.common.ui.teamexplorer.TeamExplorerNavigator;
import com.microsoft.tfs.client.common.ui.teamexplorer.internal.TeamExplorerNavigationItemConfig;
import com.microsoft.tfs.client.common.ui.teamexplorer.link.TeamExplorerBaseNavigationLink;

public class FieldHistoryNavLink extends TeamExplorerBaseNavigationLink {

    @Override
    public void clicked(
        final Shell shell,
        final TeamExplorerContext context,
        final TeamExplorerNavigator navigator,
        final TeamExplorerNavigationItemConfig parentNavigationItem)
    {
    	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			page.showView(FieldHistoryView.ID);
		} catch (PartInitException e) {
			throw new RuntimeException(e);
		}
    }
    
}

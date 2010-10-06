package workflow;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.mwe.core.WorkflowContext;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.lib.WorkflowComponentWithModelSlot;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;

public class ResourceReader extends WorkflowComponentWithModelSlot {
	private String uri;

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}

	@Override
	protected void invokeInternal(WorkflowContext ctx, ProgressMonitor monitor,
			Issues issues) {
		ResourceSet resourceSet = new ResourceSetImpl();
		URI fileURI = URI.createFileURI(uri);
		Resource resource = resourceSet.getResource(fileURI, true);
		ctx.set(getModelSlot(), resource.getContents().get(0));
	}
}

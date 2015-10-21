package com.gesila.test.guard.application.parts;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MStackElement;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.gesila.test.guard.application.Activator;
import com.gesila.test.guard.application.http.RequestType;

/**
 * 
 * @author robin
 *
 */
public class TestOverviewPart {

	@Inject
	private ESelectionService selectionService;

	@Inject
	private EPartService partService;

	@Inject
	private EModelService modelService;

	@Inject
	private MApplication application;

	@PostConstruct
	public void createCompoiste(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		TreeViewer treeViewer = new TreeViewer(parent);
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				selectionService.setSelection(selection.getFirstElement());
				MPartStack partStack = (MPartStack) modelService
						.find("com.gesila.test.guard.application.partstack.detail", application);
				// MPart part =
				// partService.createPart("com.gesila.test.guard.application.partdescriptor.detail");
				Object object = selection.getFirstElement();
				if (object instanceof TestGuardUrlObject) {
					// part.setLabel(((TestGuardUrlObject) object).getName());
					// part.setElementId(object.getClass().toString());
					String id = object.toString();
					List<MStackElement> elements = partStack.getChildren();
					for (MStackElement element : elements) {
						if (id.equals(element.getElementId())) {
							partService.showPart((MPart) element, PartState.VISIBLE);
							return;
						}
					}
					// partStack.getChildren().add(part);
					// partService.showPart(part, PartState.ACTIVATE);

					// part.setLabel((String) selection.getFirstElement());
					// part.setElementId((String) selection.getFirstElement());
					// String id = (String) selection.getFirstElement();
					//
					// List<MStackElement> elements = partStack.getChildren();
					// for (MStackElement element : elements) {
					// if (id.equals(element.getElementId())) {
					// partService.showPart((MPart) element, PartState.VISIBLE);
					// return;
					// }
					// }
					// }

					// MPart mPart =
					// modelService.createModelElement(MPart.class);
					// mPart.setLabel("Test");
					// mPart.setElementId("newid");
					// mPart.setContributionURI(
					// "bundleclass://com.gesila.test.guard.application/com.gesila.test.guard.application.parts.SamplePart");
					// partStack.getChildren().add(part);
					// partService.showPart(part, PartState.ACTIVATE);

				}
			}
		});
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				selectionService.setSelection(selection.getFirstElement());
				MPartStack partStack = (MPartStack) modelService
						.find("com.gesila.test.guard.application.partstack.detail", application);
				MPart part = partService.createPart("com.gesila.test.guard.application.partdescriptor.detail");

				Object object = selection.getFirstElement();
				if (object instanceof TestGuardUrlObject) {
					String id = object.toString();
					List<MStackElement> elements = partStack.getChildren();
					for (MStackElement element : elements) {
						if (id.equals(element.getElementId())) {
							partService.showPart((MPart) element, PartState.VISIBLE);
							return;
						}
					}
					part.setLabel(((TestGuardUrlObject) object).getName());
					part.setElementId(object.toString());
					partStack.getChildren().add(part);
					partService.showPart(part, PartState.ACTIVATE);
				}

			}
		});

		treeViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		treeViewer.setContentProvider(new ITreeContentProvider() {

			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				// TODO Auto-generated method stub

			}

			@Override
			public Object[] getElements(Object inputElement) {

				return ((List) inputElement).toArray();
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				// TODO Auto-generated method stub
				if (parentElement instanceof TestGuardMoudleObject) {
					return ((TestGuardMoudleObject) parentElement).getTestGuardUrls().toArray();
				}
				return new Object[0];
			}

			@Override
			public Object getParent(Object element) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean hasChildren(Object element) {
				return getChildren(element).length > 0;
			}

		});

		treeViewer.setLabelProvider(new ILabelProvider() {

			@Override
			public void removeListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			@Override
			public void addListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public String getText(Object element) {
				return ((TestGuardObject) element).getName();
			}

			@Override
			public Image getImage(Object element) {
				if (element instanceof TestGuardMoudleObject) {
					return Activator.getDefault().getImageRegistry().get("TestGuardModule");
				}
				// TODO Auto-generated method stub
				return Activator.getDefault().getImageRegistry().get("TestGuardUrl");
			}
		});
		String[] models = new String[] { "http://localhost:8080/cloud-nat/v1/stat/channel",
				"http://localhost:8080/cloud-admin/v1/classes/login?name=admin&pass=111111",
				"http://localhost:8080/cloud-admin/v1/classes/channelstat/summary",
				"http://localhost:8080/cloud-admin/v1/classes/channelstat/csv",
				"http://localhost:8080/cloud-support/v1/login?username=13524631949&password=123456",
				"http://localhost:8080/cloud-support/v1/news", "http://localhost:8080/cloud-nat" };
		treeViewer.setInput(createModels());
	}

	private Object createModels() {
		List list = new ArrayList();
		TestGuardMoudleObject testGuardMoudleObject = new TestGuardMoudleObject("Default");

		TestGuardMoudleObject adminTestGuardMoudleObject = new TestGuardMoudleObject("Clound-Admin");
		TestGuardUrlObject loginGuardUrlObject = new TestGuardUrlObject("Login");
		loginGuardUrlObject.setUrl("http://localhost:8080/cloud-admin/v1/classes/login?name=admin&pass=111111");
		loginGuardUrlObject.setRequestType(RequestType.GET);
		adminTestGuardMoudleObject.addTestGuardUrl(loginGuardUrlObject);

		TestGuardUrlObject statChannelSummaryGuardUrlObject = new TestGuardUrlObject("StatChannel Summary");
		statChannelSummaryGuardUrlObject.setUrl("http://localhost:8080/cloud-admin/v1/classes/channelstat/summary");
		statChannelSummaryGuardUrlObject.setRequestType(RequestType.GET);
		adminTestGuardMoudleObject.addTestGuardUrl(statChannelSummaryGuardUrlObject);

		TestGuardUrlObject statChannelCSVGuardUrlObject = new TestGuardUrlObject("StatChannel CSV");
		statChannelCSVGuardUrlObject.setUrl("http://localhost:8080/cloud-admin/v1/classes/channelstat/csv");
		statChannelCSVGuardUrlObject.setRequestType(RequestType.GET);
		adminTestGuardMoudleObject.addTestGuardUrl(statChannelCSVGuardUrlObject);

		TestGuardMoudleObject supportTestGuardMoudleObject = new TestGuardMoudleObject("Clound-Support");
		loginGuardUrlObject = new TestGuardUrlObject("Login");
		loginGuardUrlObject.setRequestType(RequestType.POST);
		loginGuardUrlObject.setUrl("http://localhost:8080/cloud-support/v1/login");
		loginGuardUrlObject.setRequestBody(
				"{\"username\":\"13524631949\",\"password\":\"123456\",\"_method\":\"GET\",\"_ApplicationId\":\"1\",\"_ApplicationKey\":\"1\",\"_ClientVersion\":\"js0.5.4\",\"_InstallationId\":\"46876d56-925b-e32e-5a07-d7823ea0e40a\"}");
		supportTestGuardMoudleObject.addTestGuardUrl(loginGuardUrlObject);

		TestGuardUrlObject newsGuardUrlObject = new TestGuardUrlObject("News");
		newsGuardUrlObject.setRequestType(RequestType.POST);
		newsGuardUrlObject.setUrl("http://localhost:8080/cloud-support/v1/classes/news");
		newsGuardUrlObject.setRequestBody(
				"{\"where\":{\"tagId\":\"1\"},\"keys\":\"title,img,intro,createAt\",\"limit\":8,\"order\":\"-updateAt\",\"_method\":\"GET\",\"_ApplicationId\":\"1\",\"_ApplicationKey\":\"1\",\"_ClientVersion\":\"js0.5.4\",\"_InstallationId\":\"46876d56-925b-e32e-5a07-d7823ea0e40a\",\"_SessionToken\":\"LH-x-jCWgoqcO3ohw-aoalWbeJLKnrFnI5\"}");
		supportTestGuardMoudleObject.addTestGuardUrl(newsGuardUrlObject);

		list.add(testGuardMoudleObject);
		list.add(adminTestGuardMoudleObject);
		list.add(supportTestGuardMoudleObject);

		return list;
	}
}

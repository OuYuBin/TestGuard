package com.gesila.test.guard.application.parts;

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
import org.eclipse.e4.ui.workbench.modeling.ISelectionListener;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

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
				MPart part = partService.createPart("com.gesila.test.guard.application.partdescriptor.detail");
				part.setLabel((String) selection.getFirstElement());
				part.setElementId((String) selection.getFirstElement());
				String id = (String) selection.getFirstElement();

				List<MStackElement> elements = partStack.getChildren();
				for (MStackElement element : elements) {
					if (id.equals(element.getElementId())) {
						partService.showPart((MPart) element, PartState.VISIBLE);
						return;
					}
				}

				// MPart mPart = modelService.createModelElement(MPart.class);
				// mPart.setLabel("Test");
				// mPart.setElementId("newid");
				// mPart.setContributionURI(
				// "bundleclass://com.gesila.test.guard.application/com.gesila.test.guard.application.parts.SamplePart");
				// partStack.getChildren().add(part);
				// partService.showPart(part, PartState.ACTIVATE);

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
				part.setLabel((String) selection.getFirstElement());
				part.setElementId((String) selection.getFirstElement());
				String id = (String) selection.getFirstElement();
				List<MStackElement> elements = partStack.getChildren();
				for (MStackElement element : elements) {
					if (id.equals(element.getElementId())) {
						partService.showPart((MPart) element, PartState.VISIBLE);
						return;
					}
				}
				partStack.getChildren().add(part);
				partService.showPart(part, PartState.ACTIVATE);

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
				// TODO Auto-generated method stub
				return (String[]) inputElement;
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object getParent(Object element) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean hasChildren(Object element) {
				return false;
			}

		});
		String[] models = new String[] { "http://localhost:8080/cloud-admin/v1/classes/login?name=admin&pass=111111",
				"http://localhost:8080/cloud-admin/v1/classes/channelstat/summary", "http://www.163.com", "http://localhost:8080/cloud-nat" };

		treeViewer.setInput(models);
	}
}

/*******************************************************************************
 * Copyright (c) 2010 - 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Lars Vogel <lars.Vogel@gmail.com> - Bug 419770
 *******************************************************************************/
package com.gesila.test.guard.application.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.http.HttpResponse;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.gesila.test.guard.application.http.GesilaHttpClient;
import com.gesila.test.guard.application.http.GesilaHttpClientUtil;
import com.gesila.test.guard.application.http.GesilaReponseStructuredSelection;

public class SamplePart {

	private Text txtInput;

	private Button button;

	private TableViewer tableViewer;

	@Inject
	private MDirtyable dirty;

	private String url;

	@Inject
	private ESelectionService selectionService;

	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout(5, false));

		Label methodLabel = new Label(parent, SWT.NONE);
		methodLabel.setText("Method:");
		methodLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

		Combo combo = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
		String[] methods = { "GET", "POST" };
		combo.setItems(methods);
		combo.select(0);
		combo.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

		Label urlLabel = new Label(parent, SWT.NONE);
		urlLabel.setText("Method:");
		urlLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

		txtInput = new Text(parent, SWT.BORDER);
		txtInput.setMessage("输入url链接地址");
		// txtInput.addModifyListener(new ModifyListener() {
		// @Override
		// public void modifyText(ModifyEvent e) {
		// dirty.setDirty(true);
		// }
		// });
		if (this.url != null) {
			txtInput.setText(url);
		}
		txtInput.setLayoutData(new GridData(GridData.FILL, SWT.CENTER, true, false));
		button = new Button(parent, SWT.BORDER);
		button.setText("确定");
		button.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		button.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				GesilaHttpClient gesilaHttpClient = new GesilaHttpClient(txtInput.getText());
				HttpResponse reponse = (HttpResponse) GesilaHttpClientUtil.execute(gesilaHttpClient);
				System.out.println(reponse.getStatusLine().getStatusCode());
				ISelection selection = new GesilaReponseStructuredSelection(reponse);
				selectionService.setSelection(reponse);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		button.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false));

		Label bodyLabel = new Label(parent, SWT.NONE);
		bodyLabel.setText("Body:");
		bodyLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 5, 1));
		
		Text bodyText = new Text(parent, SWT.BORDER|SWT.WRAP);
		bodyText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));

		// tableViewer = new TableViewer(parent);`
		//
		// tableViewer.add("Sample item 1");
		// tableViewer.add("Sample item 2");
		// tableViewer.add("Sample item 3");
		// tableViewer.add("Sample item 4");
		// tableViewer.add("Sample item 5");
		// tableViewer.getTable().setLayoutData(new
		// GridData(GridData.FILL_BOTH));
	}

	@Inject
	public void setSelection(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) String url) {
		System.out.println(txtInput);
		this.url = url;
		// txtInput.setText(url);
	}

	@Focus
	public void setFocus() {
		// tableViewer.getTable().setFocus();
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}
}
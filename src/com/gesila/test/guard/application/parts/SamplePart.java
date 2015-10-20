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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.utils.URLEncodedUtils;
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

import com.alibaba.fastjson.JSONObject;
import com.gesila.test.guard.application.http.GesilaHttpClient;
import com.gesila.test.guard.application.http.GesilaHttpClientUtil;
import com.gesila.test.guard.application.http.GesilaHttpResponse;
import com.gesila.test.guard.application.http.GesilaReponseStructuredSelection;
import com.gesila.test.guard.application.http.RequestType;

public class SamplePart {

	private Text txtInput;

	private Button button;

	private Text bodyText;

	private Combo combo;

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

		combo = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
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
		button.setText("Send");
		button.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		button.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				GesilaHttpClient gesilaHttpClient = new GesilaHttpClient(txtInput.getText());
				if (bodyText.getText() != null) {
					gesilaHttpClient.setRequestJSON(bodyText.getText());
				}
				gesilaHttpClient.setRequestType(RequestType.valueOf(combo.getText()));
				HttpResponse response = (HttpResponse) GesilaHttpClientUtil.execute(gesilaHttpClient);
				GesilaHttpResponse gesilaHttpResponse = new GesilaHttpResponse(response);
				selectionService.setSelection(gesilaHttpResponse);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		button.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false));

		Label bodyLabel = new Label(parent, SWT.NONE);
		bodyLabel.setText("Body:");
		bodyLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 5, 1));

		bodyText = new Text(parent, SWT.BORDER | SWT.WRAP);
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

	private String getResponseJSON(HttpResponse httpResponse) {
		HttpEntity httpEntity = httpResponse.getEntity();
		InputStream inputStream;
		JSONObject respJsonObject = null;
		try {
			if (httpEntity.isStreaming()) {
				inputStream = httpEntity.getContent();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferReader = new BufferedReader(inputStreamReader);
				StringBuffer stringBuffer = new StringBuffer();
				String str = null;
				while ((str = bufferReader.readLine()) != null) {
					stringBuffer.append(str);
				}
				inputStream.close();
				inputStreamReader.close();
				bufferReader.close();
				return stringBuffer.toString();
			}
			// --判断是否为json字符串
			// char[] responseChars = stringBuffer.toString().toCharArray();
			// char firstChar = responseChars[0];
			// if ('{' == firstChar) {
			// respJsonObject = JSONObject.parseObject(stringBuffer.toString());
			// } else {
			// Map map = new HashMap();
			// map.put("name", stringBuffer.toString());
			// respJsonObject = new JSONObject(map);
			// }

		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

}
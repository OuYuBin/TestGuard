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
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.auth.callback.TextInputCallback;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.utils.URLEncodedUtils;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeColumn;

import com.alibaba.fastjson.JSONArray;
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

	private TestGuardUrlObject testGuardUrlObject;

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

		if (this.testGuardUrlObject != null) {
			// combo.setText(this.testGuardUrlObject.getRequestType().name());
			combo.select(Arrays.binarySearch(methods, this.testGuardUrlObject.getRequestType().name()));
		}

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
		if (this.testGuardUrlObject != null) {
			txtInput.setText(this.testGuardUrlObject.getUrl());
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

		CTabFolder bodyCTabFolder = new CTabFolder(parent, SWT.NONE);
		bodyCTabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));
		bodyCTabFolder.setSimple(true);
		
		CTabItem textTabItem = new CTabItem(bodyCTabFolder, SWT.NONE);
		textTabItem.setText("Text");
		textTabItem.setShowClose(false);

		bodyText = new Text(bodyCTabFolder, SWT.BORDER | SWT.WRAP);
		// bodyText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
		// 5, 1));
		if (this.testGuardUrlObject != null) {
			bodyText.setText(this.testGuardUrlObject.getRequestBody());
		}
		textTabItem.setControl(bodyText);
		
		CTabItem jsonCTabItem=new CTabItem(bodyCTabFolder, SWT.NONE);
		jsonCTabItem.setText("Json");
		jsonCTabItem.setShowClose(false);
		
//		Composite jsonComposite=new Composite(bodyCTabFolder,SWT.BORDER);
//		jsonComposite.setLayout(new GridLayout(1,false));
//		jsonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		TreeViewer treeViewer=new TreeViewer(bodyCTabFolder,SWT.NONE);
		TreeColumn column = new TreeColumn(treeViewer.getTree(), SWT.NONE);
		column.setWidth(200);
		column.setText("Name");

		column = new TreeColumn(treeViewer.getTree(), SWT.NONE);
		column.setWidth(200);
		column.setText("Value");
		treeViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		treeViewer.getTree().setHeaderVisible(true);
		
		treeViewer.setContentProvider(new ITreeContentProvider() {
			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean hasChildren(Object element) {
				// TODO Auto-generated method stub
				if (getChildren(element).length > 0) {
					return true;
				}
				return false;
			}
			
			@Override
			public Object getParent(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Object[] getElements(Object inputElement) {
				// TODO Auto-generated method stub
				return (Object[]) ((List) inputElement).toArray(new Object[0]);
			}
			
			@Override
			public Object[] getChildren(Object parentElement) {
				// TODO Auto-generated method stub
				return ((ResponseObject) parentElement).getReponseObjects().toArray(new Object[0]);
			}
		});
		treeViewer.setLabelProvider(new ITableLabelProvider() {
			
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
			public String getColumnText(Object element, int columnIndex) {
				switch (columnIndex) {
				case 0:
					return ((ResponseObject) element).getName() == null ? "" : ((ResponseObject) element).getName();
				case 1:
					return ((ResponseObject) element).getValue() == null ? "" : ((ResponseObject) element).getValue();
				}
				return null;
			}
			
			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		
		
		String requestBody=this.testGuardUrlObject.getRequestBody();
		JSONObject respJsonObject = null;
		char[] responseChars = requestBody.toCharArray();
		char firstChar = responseChars[0];
		if ('{' == firstChar) {
			respJsonObject = JSONObject.parseObject(requestBody);
		} else {
			Map map = new HashMap();
			map.put("name", requestBody);
			respJsonObject = new JSONObject(map);
		}
		
		List list = new ArrayList();
		createJSONObject(respJsonObject, list);
		treeViewer.setInput(list);
		
		jsonCTabItem.setControl(treeViewer.getControl());
		

		bodyCTabFolder.setSelection(0);

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
	
	private void createJSONObject(JSONObject respJsonObject, List list) {

		Iterator<String> iter = respJsonObject.keySet().iterator();
		while (iter.hasNext()) {
			// --key
			String key = iter.next();
			ResponseObject responseObject = new ResponseObject();
			responseObject.setName(key);
			String value = respJsonObject.getString(key);
			char fchar = 0;
			if (!"".equals(value)) {
				char[] chars = value.toCharArray();
				fchar = chars[0];
			}
			if ('{' == fchar) {
				createJSONObject(JSONObject.parseObject(value), list);
			} else if ('[' == fchar) {
				JSONArray jsonArray = JSONObject.parseArray(value);
				ListIterator<Object> listIter = jsonArray.listIterator();
				ArrayList tmpList = new ArrayList();
				while (listIter.hasNext()) {
					ArrayList arrayList = new ArrayList();
					Object object = listIter.next();
					// --创建虚拟节点对象
					ResponseObject parentReponseObject = new ResponseObject();
					createJSONObject((JSONObject) object, arrayList);
					parentReponseObject.setReponseObjects(arrayList);
					tmpList.add(parentReponseObject);
				}
				responseObject.setReponseObjects(tmpList);
				list.add(responseObject);
			} else {
				responseObject.setValue(value);
				list.add(responseObject);
			}
		}
	}

	@Inject
	public void setSelection(
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) TestGuardUrlObject testGuardUrlObject) {
		System.out.println(txtInput);
		this.testGuardUrlObject = testGuardUrlObject;
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
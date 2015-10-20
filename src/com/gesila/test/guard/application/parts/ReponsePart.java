package com.gesila.test.guard.application.parts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeColumn;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gesila.test.guard.application.http.GesilaHttpResponse;

/**
 * 
 * @author robin
 *
 */
public class ReponsePart {

	private TreeViewer treeViewer;

	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		treeViewer = new TreeViewer(parent);
		treeViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		TreeColumn column = new TreeColumn(treeViewer.getTree(), SWT.NONE);
		column.setWidth(200);
		column.setText("Name");

		column = new TreeColumn(treeViewer.getTree(), SWT.NONE);
		column.setWidth(200);
		column.setText("Value");

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
				return (Object[]) ((List) inputElement).toArray(new Object[0]);
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				return ((ResponseObject) parentElement).getReponseObjects().toArray(new Object[0]);
				// return null;
			}
		});
		treeViewer.setLabelProvider(new ITableLabelProvider() {

			@Override
			public void addListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void removeListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				// TODO Auto-generated method stub
				return null;
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

		});

		// if (httpResponse != null) {
		// Header[] headers = httpResponse.getAllHeaders();
		// treeViewer.setInput(headers);
		// }
	}

	@Inject
	public void setSelection(
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) GesilaHttpResponse gesilaHttpResponse) {
		if (gesilaHttpResponse != null) {
			update(gesilaHttpResponse);
		}

	}

	private void update(GesilaHttpResponse gesilaHttpResponse) {
		HttpResponse httpResponse = gesilaHttpResponse.getHttpResponse();
		String strResponse = gesilaHttpResponse.getStrResponse();
		String response = null;

		if (strResponse != null) {
			response = strResponse;

		} else if (httpResponse != null) {
			Header[] headers = httpResponse.getAllHeaders();
			HttpEntity httpEntity = httpResponse.getEntity();
			response = getResponseJSON(httpResponse);
			gesilaHttpResponse.setStrResponse(response);
		}

		JSONObject respJsonObject = null;
		char[] responseChars = response.toCharArray();
		char firstChar = responseChars[0];
		if ('{' == firstChar) {
			respJsonObject = JSONObject.parseObject(response);
		} else {
			Map map = new HashMap();
			map.put("name", response);
			respJsonObject = new JSONObject(map);
		}
		List list = new ArrayList();
		createJSONObject(respJsonObject, list);
		System.out.println(list);
		if (!list.isEmpty()) {
			treeViewer.setInput(list);
			treeViewer.refresh(true);
			treeViewer.expandToLevel(3);

		}

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

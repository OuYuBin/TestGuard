package com.gesila.test.guard.application.parts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author robin
 *
 */
public class ReponsePart {

	private HttpResponse httpResponse;

	private TreeViewer treeViewer;

	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		treeViewer = new TreeViewer(parent);
		treeViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
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
				return false;
			}

			@Override
			public Object getParent(Object element) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object[] getElements(Object inputElement) {
				// String[] values = new String[((Header[])
				// inputElement).length];
				// int i = 0;
				// for (Header header : (Header[]) inputElement) {
				// values[i] = header.getValue();
				// i++;
				// }
				// return values;
				return (Object[]) inputElement;
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		if (httpResponse != null) {
			Header[] headers = httpResponse.getAllHeaders();
			treeViewer.setInput(headers);
		}
	}

	@Inject
	public void setSelection(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) HttpResponse httpResponse) {
		this.httpResponse = httpResponse;
		update(httpResponse);

	}

	private void update(HttpResponse httpResponse) {
		if (httpResponse != null) {
			Header[] headers = httpResponse.getAllHeaders();
			HttpEntity httpEntity = httpResponse.getEntity();
			JSONObject jsonObject = getResponseJSON(httpResponse);
			if (jsonObject != null) {
				Object[] responses = jsonObject.values().toArray(new Object[0]);
				createModel(responses);
				treeViewer.setInput(responses);
				treeViewer.refresh(true);
			}
		}
	}

	private void createModel(Object[] responses) {
		
	}

	private JSONObject getResponseJSON(HttpResponse httpResponse) {
		HttpEntity httpEntity = httpResponse.getEntity();
		InputStream inputStream;
		try {
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

			JSONObject jsonObject = JSONObject.parseObject(stringBuffer.toString());

			return jsonObject;

		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

}

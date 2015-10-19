package com.gesila.test.guard.application.http;

import org.apache.http.HttpResponse;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * 
 * @author robin
 *
 */
public class GesilaReponseStructuredSelection extends StructuredSelection implements IGesilaReponseStructureSelection {

	public GesilaReponseStructuredSelection(HttpResponse reponse) {
		super(reponse);
	}
}

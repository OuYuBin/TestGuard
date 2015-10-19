package com.gesila.test.guard.application.parts;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author robin
 *
 */
public class ResponseObject {

	List<ResponseObject> reponseObjects = new ArrayList<ResponseObject>();

	public String name;

	public String value;

	public void addResponseObject(ResponseObject responseObject) {
		reponseObjects.add(responseObject);
	}

	public List<ResponseObject> getReponseObjects() {
		return reponseObjects;
	}

	public void setReponseObjects(List<ResponseObject> reponseObjects) {
		this.reponseObjects = reponseObjects;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}

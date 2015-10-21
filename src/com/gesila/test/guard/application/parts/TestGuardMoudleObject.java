package com.gesila.test.guard.application.parts;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author robin
 *
 */
public class TestGuardMoudleObject extends TestGuardObject {

	private List<TestGuardUrlObject> testGuardUrls = new ArrayList<TestGuardUrlObject>();

	public TestGuardMoudleObject(String name) {
		super(name);
	}

	public List<TestGuardUrlObject> getTestGuardUrls() {
		return testGuardUrls;
	}

	/**
	 * 
	 * @param testGuardUrlObject
	 */
	public void addTestGuardUrl(TestGuardUrlObject testGuardUrlObject) {
		testGuardUrls.add(testGuardUrlObject);
	}

}

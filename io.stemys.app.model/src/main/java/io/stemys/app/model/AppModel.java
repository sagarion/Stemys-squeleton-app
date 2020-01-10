/**
 *                   Stemys SA
 * 
 * @author Guillaume Milani
 * @date 01.2019
 * 
 * This file describes a useless POJO which could be used
 * in other classes or services.
 */
package io.stemys.app.model;

public class AppModel {
	/**
	 * Declaring private attributes is a good practice
	 */
	private String attribute;

	/**
	 * Using getters and setters allows to expose private attributes
	 */
	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
}

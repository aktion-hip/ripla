/*******************************************************************************
 * Copyright (c) 2013 RelationWare, Benno Luthiger
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * RelationWare, Benno Luthiger
 ******************************************************************************/

package org.ripla.rap.demo.widgets.data;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The model for the form view.
 * 
 * @author Luthiger
 */
public class FormModel implements PropertyChangeListener {
	private String gender;
	private String familyName;
	private String firstName;
	private String street;
	private String postal;
	private String city;
	private String mail;
	private Integer age;
	private String education;
	private String workarea;

	private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			this);

	/**
	 * FormModel constructor.
	 */
	public FormModel() {
		super();
	}

	public String getGender() {
		return gender;
	}

	public void setGender(final String inGender) {
		propertyChangeSupport.firePropertyChange("gender", gender,
				gender = inGender);
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(final String inFamilyName) {
		propertyChangeSupport.firePropertyChange("familyName", familyName,
				familyName = inFamilyName);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String inFirstName) {
		propertyChangeSupport.firePropertyChange("firstName", firstName,
				firstName = inFirstName);
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(final String inStreet) {
		propertyChangeSupport.firePropertyChange("street", street,
				street = inStreet);
	}

	public String getPostal() {
		return postal;
	}

	public void setPostal(final String inPostal) {
		propertyChangeSupport.firePropertyChange("postal", postal,
				postal = inPostal);
	}

	public String getCity() {
		return city;
	}

	public void setCity(final String inCity) {
		propertyChangeSupport.firePropertyChange("city", city, city = inCity);
	}

	public String getMail() {
		return mail;
	}

	public void setMail(final String inMail) {
		propertyChangeSupport.firePropertyChange("mail", mail, mail = inMail);
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(final Integer inAge) {
		propertyChangeSupport.firePropertyChange("age", age, age = inAge);
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(final String inEducation) {
		propertyChangeSupport.firePropertyChange("education", education,
				education = inEducation);
	}

	public String getWorkarea() {
		return workarea;
	}

	public void setWorkarea(final String inWorkarea) {
		propertyChangeSupport.firePropertyChange("workarea", workarea,
				workarea = inWorkarea);
	}

	public PropertyChangeSupport getPropertyChangeSupport() {
		return propertyChangeSupport;
	}

	public void addPropertyChangeListener(final String inPropertyName,
			final PropertyChangeListener inListener) {
		propertyChangeSupport.addPropertyChangeListener(inPropertyName,
				inListener);
	}

	public void removePropertyChangeListener(
			final PropertyChangeListener inListener) {
		propertyChangeSupport.removePropertyChangeListener(inListener);
	}

	@Override
	public void propertyChange(final PropertyChangeEvent inEvent) {
		// nothing to do?
	}

	/**
	 * @return {@link FormModel} a initialized empty model
	 */
	public static FormModel createEmptyModel() {
		final FormModel out = new FormModel();
		out.setGender("");
		out.setFamilyName("");
		out.setFirstName("");
		out.setStreet("");
		out.setPostal("");
		out.setCity("");
		out.setAge(0);
		out.setMail("@");
		out.setEducation("");
		out.setWorkarea("");
		return out;
	}

}

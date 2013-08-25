/*******************************************************************************
 * Copyright (c) 2012-2013 RelationWare, Benno Luthiger
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * RelationWare, Benno Luthiger
 ******************************************************************************/

package org.ripla.web.demo.widgets.data;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;

/**
 * The bean for the form widgets example.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class FormBean extends PropertysetItem {
	public static final String FN_GENDER = "gender";
	public static final String FN_NAME = "name";
	public static final String FN_FIRSTNAME = "firstname";
	public static final String FN_STREET = "street";
	public static final String FN_POSTAL = "postal";
	public static final String FN_CITY = "city";
	public static final String FN_MAIL = "mail";
	public static final String FN_AGE = "age";
	public static final String FN_EDUCATION = "education";
	public static final String FN_WORKAREA = "workarea";

	public FormBean() {
		super();
		addItemProperty(FN_GENDER, new ObjectProperty<String>(""));
		addItemProperty(FN_NAME, new ObjectProperty<String>(""));
		addItemProperty(FN_FIRSTNAME, new ObjectProperty<String>(""));
		addItemProperty(FN_STREET, new ObjectProperty<String>(""));
		addItemProperty(FN_POSTAL, new ObjectProperty<String>(""));
		addItemProperty(FN_CITY, new ObjectProperty<String>(""));
		addItemProperty(FN_MAIL, new ObjectProperty<String>(""));
		addItemProperty(FN_AGE, new ObjectProperty<String>(""));
		addItemProperty(FN_EDUCATION, new ObjectProperty<String>(""));
		addItemProperty(FN_WORKAREA, new ObjectProperty<String>(""));
	}

	public String getGender() {
		return (String) getItemProperty(FN_GENDER).getValue();
	}

	public String getName() {
		return (String) getItemProperty(FN_NAME).getValue();
	}

	public String getFirstName() {
		return (String) getItemProperty(FN_FIRSTNAME).getValue();
	}

	public String getStreet() {
		return (String) getItemProperty(FN_STREET).getValue();
	}

	public String getPostal() {
		return (String) getItemProperty(FN_POSTAL).getValue();
	}

	public String getCity() {
		return (String) getItemProperty(FN_CITY).getValue();
	}

	public String getMail() {
		return (String) getItemProperty(FN_MAIL).getValue();
	}

	public String getAge() {
		return (String) getItemProperty(FN_AGE).getValue();
	}

	public String getEducation() {
		return (String) getItemProperty(FN_EDUCATION).getValue();
	}

	public String getWorkArea() {
		return (String) getItemProperty(FN_WORKAREA).getValue();
	}

}

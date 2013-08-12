/*******************************************************************************
 * Copyright (c) 2012-2012 RelationWare, Benno Luthiger
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * RelationWare, Benno Luthiger
 ******************************************************************************/
package org.ripla.web.util;

import org.ripla.interfaces.IMessages;
import org.ripla.web.Activator;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;

/**
 * Base class for form creator classes. Subclasses have to implement the method
 * {@link #createTable()}. <br />
 * <p>
 * Helper class to create Vaadin forms. It provides form validation support and
 * meaningful error indicators if form validation fails.
 * </p>
 * Usage:
 * 
 * <pre>
 * public class MyFormCreator extends AbstractFormCreator {
 *     public Component createTable() {
 *         ##create table with input fields
 *         return table;
 *     }
 * }
 * 
 * ## code
 * VerticalLayout layout = new VerticalLayout();
 * final MyFormCreator lForm = new MyFormCreator();
 * layout.addComponent(lForm.createForm());
 * Button lSave = new Button("Save");
 * lSave.addListener(new Button.ClickListener() {
 *     public void buttonClick(ClickEvent inEvent) {
 *         try {
 *             lForm.commit();
 *             ##save
 *         }
 *         catch (InvalidValueException exc) {}
 *     }
 * }
 * layout.addComponent(lSave);
 * </pre>
 * 
 * @author Luthiger
 */
public abstract class AbstractFormCreator {
	private final transient Form form = new Form();
	private final transient IMessages messages = Activator.getMessages();

	protected Form getForm() {
		form.setStyleName("ripla-form"); //$NON-NLS-1$
		return form;
	}

	/**
	 * Delegates the commit to the wrapped form component.
	 */
	public void commit() {
		form.commit();
	}

	/**
	 * Adds a field to the form.
	 * 
	 * @param inFieldID
	 *            String must be unique on the form
	 * @param inField
	 *            {@link Field}
	 * @return {@link Field}
	 */
	protected Field addField(final String inFieldID, final Field inField) {
		form.addField(inFieldID, inField);
		return form.getField(inFieldID);
	}

	/**
	 * Adds a required field to the form.
	 * 
	 * @param inFieldID
	 *            String must be unique on the form
	 * @param inField
	 *            {@link AbstractField}
	 * @return {@link Field}
	 */
	protected Field addFieldRequired(final String inFieldID,
			final AbstractField inField) {
		inField.setRequired(true);
		inField.setImmediate(true);
		return addField(inFieldID, inField);
	}

	/**
	 * Adds a required field to the form.
	 * 
	 * @param inFieldID
	 *            String must be unique on the form
	 * @param inField
	 *            {@link AbstractField}
	 * @param inRequiredFieldLbl
	 *            String the label of the required field, to generate the
	 *            message <code>The field "FieldName" must not be empty!</code>
	 * @return {@link Field}
	 */
	protected Field addFieldRequired(final String inFieldID,
			final AbstractField inField, final String inRequiredFieldLbl) {
		inField.setRequiredError(messages.getFormattedMessage(
				"errmsg.error.not.empty", inRequiredFieldLbl)); //$NON-NLS-1$
		return addFieldRequired(inFieldID, inField);
	}

	/**
	 * Factory method: creates the form.
	 * 
	 * @return {@link Form} the form to add to the layout
	 */
	public Form createForm() {
		final Form outForm = getForm();
		outForm.getLayout().addComponent(createTable());
		return outForm;
	}

	abstract protected Component createTable();

}

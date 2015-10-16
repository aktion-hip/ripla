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
package org.ripla.web.util;

import java.util.Iterator;

import org.ripla.interfaces.IMessages;
import org.ripla.web.Activator;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;

/**
 * Base class for form creator classes. Subclasses have to implement the method
 * {@link #createTable()}. <br />
 * <p>
 * Helper class to create Vaadin forms. It provides field binding and form
 * validation support and meaningful error indicators if form validation fails.
 * </p>
 * Usage:
 * 
 * <pre>
 * public class MyFormCreator extends AbstractFormCreator {
 *     protected MyFormCreator(Item inItem) {
 *         super(inItem);
 *     }
 *     public Component createTable() {
 *         //create table or FormLayout with input fields
 *         return table;
 *     }
 * }
 * 
 * ## code
 * VerticalLayout layout = new VerticalLayout();
 * final MyFormCreator lForm = new MyFormCreator(item);
 * layout.addComponent(lForm.createForm());
 * Button lSave = new Button("Save");
 * lSave.addListener(new Button.ClickListener() {
 *     public void buttonClick(ClickEvent inEvent) {
 *         try {
 *             lForm.commit();
 *             //save item
 *         }
 *         catch (final CommitException exc) {
 *         	   //notification
 *         }
 *     }
 * }
 * layout.addComponent(lSave);
 * </pre>
 * 
 * @author Luthiger
 * @see https://vaadin.com/book/vaadin7/-/page/datamodel.itembinding.html
 */
public abstract class AbstractFormCreator {
	private final transient IMessages messages = Activator.getMessages();
	private final transient FieldGroup binder;

	/**
	 * AbstractFormCreator constructor.
	 * 
	 * @param inItem
	 *            {@link Item} the data source the form fields have to be bound
	 *            to
	 */
	public AbstractFormCreator(final Item inItem) {
		binder = new FieldGroup(inItem);
	}

	/**
	 * Delegates the commit to the wrapped filed binder.
	 * 
	 * @throws CommitException
	 */
	public void commit() throws CommitException {
		try {
			binder.commit();
		} catch (CommitException exc) {
			// in an error case we set the error marker to all invalid fields
			// which are required
			Iterator<Field<?>> fields = binder.getFields().iterator();
			while (fields.hasNext()) {
				Field<?> field = fields.next();
				if (field.isRequired() && !field.isValid()) {
					((AbstractComponent) field).setComponentError(new UserError(""));
				}
				if (field.isRequired()) {
					if (field.isValid()) {
						((AbstractComponent) field).setComponentError(null);
					} else {
						((AbstractComponent) field).setComponentError(new UserError(""));
					}
				}
			}
			throw exc;
		}
	}

	/**
	 * Binds the specified field to the form.
	 * 
	 * @param inFieldID
	 *            String must be unique on the form
	 * @param inField
	 *            {@link Field}
	 * @return {@link Field}
	 */
	protected Field<?> addField(final String inFieldID, final Field<?> inField) {
		binder.bind(inField, inFieldID);
		return inField;
	}

	/**
	 * Binds the specified required field to the form.
	 * 
	 * @param inFieldID
	 *            String must be unique on the form
	 * @param inField
	 *            {@link AbstractField}
	 * @return {@link Field}
	 */
	protected Field<?> addFieldRequired(final String inFieldID, final AbstractField<?> inField) {
		inField.setRequired(true);
		inField.setStyleName("ripla-required-field");
		inField.setImmediate(true);
		return addField(inFieldID, inField);
	}

	/**
	 * Binds the specified required field to the form.
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
	protected Field<?> addFieldRequired(final String inFieldID, final AbstractField<?> inField,
			final String inRequiredFieldLbl) {
		inField.setRequiredError(messages.getFormattedMessage("errmsg.error.not.empty", inRequiredFieldLbl)); //$NON-NLS-1$
		return addFieldRequired(inFieldID, inField);
	}

	/**
	 * Creates and binds a text field to the form.
	 * 
	 * @param inFieldID
	 *            String must be unique on the form
	 * @return {@link Field} the created field
	 */
	protected Field<String> addField(final String inFieldID) {
		final TextField out = new TextField();
		addField(inFieldID, out);
		return out;
	}

	/**
	 * Creates and binds a required field to the form.
	 * 
	 * @param inFieldID
	 *            String must be unique on the form
	 * @return {@link Field} the created field
	 */
	protected Field<String> addFieldRequired(final String inFieldID) {
		final TextField out = new TextField();
		addFieldRequired(inFieldID, out);
		return out;
	}

	/**
	 * Creates and binds a required field to the form.
	 * 
	 * @param inFieldID
	 *            String must be unique on the form
	 * @param inRequiredFieldLbl
	 *            String the label of the required field, to generate the
	 *            message <code>The field "FieldName" must not be empty!</code>
	 * @return {@link Field} the created field
	 */
	protected Field<String> addFieldRequired(final String inFieldID, final String inRequiredFieldLbl) {
		final TextField out = new TextField();
		addFieldRequired(inFieldID, out, inRequiredFieldLbl);
		return out;
	}

	/**
	 * Factory method: creates the form.
	 * 
	 * @return {@link Component} the form component to add to the layout
	 */
	public Component createForm() {
		final Component out = createTable();
		out.setStyleName("ripla-form"); //$NON-NLS-1$
		return out;
	}

	abstract protected Component createTable();

}

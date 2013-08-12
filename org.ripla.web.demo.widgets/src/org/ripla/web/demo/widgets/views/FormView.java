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

package org.ripla.web.demo.widgets.views;

import org.ripla.interfaces.IMessages;
import org.ripla.web.demo.widgets.Activator;
import org.ripla.web.demo.widgets.controllers.FormController;
import org.ripla.web.util.AbstractFormCreator;
import org.ripla.web.util.LabelValueTable;
import org.ripla.web.util.RiplaViewHelper;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * View displaying the form example.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class FormView extends AbstractWidgetsView {
	private static final int FIELD_WITDH = 250;

	/**
	 * FormView constructor.
	 * 
	 * @param inController
	 *            {@link FormController} this view's controller
	 */
	public FormView(final FormController inController) {
		super();

		final IMessages lMessages = Activator.getMessages();
		final VerticalLayout lLayout = initLayout(lMessages,
				"widgets.title.page.form"); //$NON-NLS-1$

		final RegistrationFormCreator lFormCreator = new RegistrationFormCreator();
		lLayout.addComponent(lFormCreator.createForm());

		final PopupContent lPopupContent = new PopupContent();
		final PopupView lPopup = new PopupView(lPopupContent);
		lPopup.setHideOnMouseOut(false);
		lPopup.setPopupVisible(false);
		lLayout.addComponent(lPopup);

		final Button lSave = new Button(
				lMessages.getMessage("widgets.view.button.label.save"));
		lSave.setClickShortcut(KeyCode.ENTER);
		lSave.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(final ClickEvent inEvent) {
				try {
					lFormCreator.commit();
					final String lFeedback = inController.save(
							lFormCreator.getGender(), lFormCreator.getName(),
							lFormCreator.getFirstName(),
							lFormCreator.getStreet(), lFormCreator.getPostal(),
							lFormCreator.getCity(), lFormCreator.getMail(),
							lFormCreator.getAge(), lFormCreator.getEducation(),
							lFormCreator.getWorkArea());
					lPopupContent.setFeedback(lFeedback);
					lPopup.setPopupVisible(true);
				}
				catch (final InvalidValueException exc) { // NOPMD by Luthiger
															// on 07.09.12 00:03
					// intentionally left empty
				}
			}
		});
		lLayout.addComponent(lSave);
	}

	private static class RegistrationFormCreator extends AbstractFormCreator {
		private final LabelValueTable table = new LabelValueTable();
		private final ListSelect gender = new ListSelect();
		private final TextField name = RiplaViewHelper.createTextField("",
				FIELD_WITDH, null);
		private final TextField firstname = RiplaViewHelper.createTextField("",
				FIELD_WITDH, null);
		private final TextField street = RiplaViewHelper.createTextField("",
				FIELD_WITDH, null);
		private final TextField postal = new TextField();
		private final TextField city = new TextField();
		private final TextField mail = RiplaViewHelper.createTextField("",
				FIELD_WITDH, null);
		private final TextField age = RiplaViewHelper.createTextField("", 40,
				null);
		private final ListSelect education = new ListSelect();
		private final ListSelect workarea = new ListSelect();

		@Override
		protected Component createTable() {
			final IMessages lMessages = Activator.getMessages();

			fillSelect(
					gender,
					new String[] {
							lMessages
									.getMessage("widgets.view.form.select.sex.1"),
							lMessages
									.getMessage("widgets.view.form.select.sex.2") });
			fillSelect(
					education,
					new String[] {
							lMessages
									.getMessage("widgets.view.form.select.educ.1"),
							lMessages
									.getMessage("widgets.view.form.select.educ.2"),
							lMessages
									.getMessage("widgets.view.form.select.educ.3"),
							lMessages
									.getMessage("widgets.view.form.select.educ.4") });
			fillSelect(
					workarea,
					new String[] {
							lMessages
									.getMessage("widgets.view.form.select.work.1"),
							lMessages
									.getMessage("widgets.view.form.select.work.2"),
							lMessages
									.getMessage("widgets.view.form.select.work.3"),
							lMessages
									.getMessage("widgets.view.form.select.work.4"),
							lMessages
									.getMessage("widgets.view.form.select.work.5"),
							lMessages
									.getMessage("widgets.view.form.select.work.6") });
			age.setMaxLength(3);

			table.addRow(lMessages.getMessage("widgets.view.form.gender"),
					addField("gender", gender));
			table.addRowEmphasized(
					lMessages.getMessage("widgets.view.form.name"),
					addFieldRequired("familyname", name,
							lMessages.getMessage("widgets.view.form.name")));
			table.addRowEmphasized(
					lMessages.getMessage("widgets.view.form.firstname"),
					addFieldRequired("firstname", firstname,
							lMessages.getMessage("widgets.view.form.firstname")));
			table.addRow(lMessages.getMessage("widgets.view.form.street"),
					addField("street", street));
			table.addRow(lMessages.getMessage("widgets.view.form.city"),
					createPostalCity());
			table.addRowEmphasized(
					lMessages.getMessage("widgets.view.form.mail"),
					addFieldRequired("mail", mail,
							lMessages.getMessage("widgets.view.form.mail")));
			table.addRow(lMessages.getMessage("widgets.view.form.age"),
					addField("age", age));
			table.addRow(lMessages.getMessage("widgets.view.form.education"),
					addField("education", education));
			table.addRow(lMessages.getMessage("widgets.view.form.workarea"),
					addField("workarea", workarea));
			return table;
		}

		private void fillSelect(final ListSelect inSelect,
				final String[] inValues) {
			inSelect.setWidth(FIELD_WITDH, Unit.PIXELS);
			inSelect.setRows(1);
			inSelect.setStyleName("ripla-input"); //$NON-NLS-1$
			for (final String lValue : inValues) {
				inSelect.addItem(lValue);
			}
		}

		private HorizontalLayout createPostalCity() {
			final HorizontalLayout out = new HorizontalLayout();
			out.setStyleName("ripla-input"); //$NON-NLS-1$
			out.setSpacing(true);
			postal.setWidth(54, Unit.PIXELS);
			postal.setMaxLength(6);
			city.setWidth(190, Unit.PIXELS);
			out.addComponent(postal);
			out.addComponent(city);
			return out;
		}

		public String getGender() {
			return (String) gender.getValue();
		}

		public String getName() {
			return name.getValue();
		}

		public String getFirstName() {
			return firstname.getValue();
		}

		public String getStreet() {
			return street.getValue();
		}

		public String getPostal() {
			return postal.getValue();
		}

		public String getCity() {
			return city.getValue();
		}

		public String getMail() {
			return mail.getValue();
		}

		public String getAge() {
			return age.getValue();
		}

		public String getEducation() {
			return (String) education.getValue();
		}

		public String getWorkArea() {
			return (String) workarea.getValue();
		}
	}

	private static class PopupContent implements PopupView.Content {
		private String feedback = "";
		private final VerticalLayout root;

		PopupContent() {
			root = new VerticalLayout();
			root.setSpacing(true);
			root.setSizeUndefined();
		}

		@Override
		public String getMinimizedValueAsHTML() {
			return "";
		}

		@Override
		public Component getPopupComponent() {
			final Label lLayout = new Label(String.format(
					"<p>%s</p>%s",
					Activator.getMessages().getMessage(
							"widgets.view.form.feedback"), feedback),
					ContentMode.HTML);
			lLayout.setWidth(300, Unit.PIXELS);
			root.removeAllComponents();
			root.addComponent(lLayout);
			return root;
		}

		protected void setFeedback(final String inFeedback) {
			feedback = inFeedback;
		}
	}

}

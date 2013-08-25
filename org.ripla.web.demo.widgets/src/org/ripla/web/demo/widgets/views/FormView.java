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
import org.ripla.web.demo.widgets.data.FormBean;
import org.ripla.web.util.AbstractFormCreator;
import org.ripla.web.util.LabelValueTable;
import org.ripla.web.util.RiplaViewHelper;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
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

		final FormBean lFormItem = new FormBean();
		final RegistrationFormCreator lFormCreator = new RegistrationFormCreator(
				lFormItem);
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
					final String lFeedback = inController.save(lFormItem);
					lPopupContent.setFeedback(lFeedback);
					lPopup.setPopupVisible(true);
				}
				catch (final CommitException exc) {
					Notification.show(exc.getCause().getMessage(),
							Type.ERROR_MESSAGE);
				}
			}
		});
		lLayout.addComponent(lSave);
	}

	private static class RegistrationFormCreator extends AbstractFormCreator {
		public RegistrationFormCreator(final Item inItem) {
			super(inItem);
		}

		@Override
		protected Component createTable() {
			final IMessages lMessages = Activator.getMessages();
			final LabelValueTable outTable = new LabelValueTable();

			final ListSelect lGender = fillSelect(
					lMessages.getMessage("widgets.view.form.select.sex.1"),
					lMessages.getMessage("widgets.view.form.select.sex.2"));
			outTable.addRow(lMessages.getMessage("widgets.view.form.gender"),
					addField(FormBean.FN_GENDER, lGender));

			outTable.addRowEmphasized(
					lMessages.getMessage("widgets.view.form.name"),
					addFieldRequired(FormBean.FN_NAME, RiplaViewHelper
							.createTextField("", FIELD_WITDH, null), lMessages
							.getMessage("widgets.view.form.name")));
			outTable.addRowEmphasized(
					lMessages.getMessage("widgets.view.form.firstname"),
					addFieldRequired(FormBean.FN_FIRSTNAME, RiplaViewHelper
							.createTextField("", FIELD_WITDH, null), lMessages
							.getMessage("widgets.view.form.firstname")));
			outTable.addRow(
					lMessages.getMessage("widgets.view.form.street"),
					addField(FormBean.FN_STREET, RiplaViewHelper
							.createTextField("", FIELD_WITDH, null)));
			outTable.addRow(lMessages.getMessage("widgets.view.form.city"),
					createPostalCity());
			outTable.addRowEmphasized(
					lMessages.getMessage("widgets.view.form.mail"),
					addFieldRequired(FormBean.FN_MAIL, RiplaViewHelper
							.createTextField("", FIELD_WITDH, null), lMessages
							.getMessage("widgets.view.form.mail")));

			final TextField lAge = RiplaViewHelper
					.createTextField("", 40, null);
			lAge.setMaxLength(3);
			outTable.addRow(lMessages.getMessage("widgets.view.form.age"),
					addField(FormBean.FN_AGE, lAge));

			final ListSelect lEducation = fillSelect(
					lMessages.getMessage("widgets.view.form.select.educ.1"),
					lMessages.getMessage("widgets.view.form.select.educ.2"),
					lMessages.getMessage("widgets.view.form.select.educ.3"),
					lMessages.getMessage("widgets.view.form.select.educ.4"));
			outTable.addRow(
					lMessages.getMessage("widgets.view.form.education"),
					addField(FormBean.FN_EDUCATION, lEducation));

			final ListSelect lWorkarea = fillSelect(
					lMessages.getMessage("widgets.view.form.select.work.1"),
					lMessages.getMessage("widgets.view.form.select.work.2"),
					lMessages.getMessage("widgets.view.form.select.work.3"),
					lMessages.getMessage("widgets.view.form.select.work.4"),
					lMessages.getMessage("widgets.view.form.select.work.5"),
					lMessages.getMessage("widgets.view.form.select.work.6"));
			outTable.addRow(lMessages.getMessage("widgets.view.form.workarea"),
					addField(FormBean.FN_WORKAREA, lWorkarea));
			return outTable;
		}

		private ListSelect fillSelect(final String... inValues) {
			final ListSelect out = new ListSelect();
			out.setWidth(FIELD_WITDH, Unit.PIXELS);
			out.setRows(1);
			out.setStyleName("ripla-input"); //$NON-NLS-1$
			for (final String lValue : inValues) {
				out.addItem(lValue);
			}
			return out;
		}

		private HorizontalLayout createPostalCity() {
			final HorizontalLayout out = new HorizontalLayout();
			out.setStyleName("ripla-input"); //$NON-NLS-1$
			out.setSpacing(true);

			final TextField lPostal = new TextField();
			lPostal.setWidth(54, Unit.PIXELS);
			lPostal.setMaxLength(6);

			final TextField lCity = new TextField();
			lCity.setWidth(190, Unit.PIXELS);
			out.addComponent(lPostal);
			out.addComponent(lCity);
			return out;
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

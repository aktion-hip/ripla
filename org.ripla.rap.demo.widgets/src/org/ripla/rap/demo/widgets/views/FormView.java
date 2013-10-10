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

package org.ripla.rap.demo.widgets.views;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.ripla.interfaces.IMessages;
import org.ripla.rap.demo.widgets.Activator;
import org.ripla.rap.demo.widgets.controllers.FormController;
import org.ripla.rap.demo.widgets.data.FormModel;
import org.ripla.rap.util.AbstractRiplaForm;
import org.ripla.rap.util.AbstractRiplaForm.RequiredValidator;
import org.ripla.rap.util.AbstractRiplaView;
import org.ripla.rap.util.DataBindingHelper;
import org.ripla.rap.util.DataBindingHelper.WidgetProp;
import org.ripla.rap.util.Popup;
import org.ripla.rap.util.Popup.PopupButtons;

/**
 * View displaying the form example.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class FormView extends AbstractRiplaView {

	/**
	 * FormView constructor.
	 * 
	 * @param inParent
	 * @param inController
	 */
	public FormView(final Composite inParent, final FormController inController) {
		super(inParent);
		final IMessages lMessages = Activator.getMessages();
		createTitle(lMessages.getMessage("widgets.title.page.form"));

		final RegistrationForm lForm = new RegistrationForm(this);
		lForm.create();

		final Button lSave = lForm.createFormProcessButton(lMessages
				.getMessage("widgets.view.button.label.save"));
		lSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent inEvent) {
				if (lForm.checkStatus()) {
					displayFeedback(inController.save(lForm.getGender(),
							lForm.getName(), lForm.getFirstName(),
							lForm.getStreet(), lForm.getPostal(),
							lForm.getCity(), lForm.getMail(), lForm.getAge(),
							lForm.getEducation(), lForm.getWorkArea()));
				} else {
					displayFeedback(lForm.getMessages());
				}
			};
		});
		getShell().setDefaultButton(lSave);
	}

	private void displayFeedback(final String inFeedback) {
		final Popup lPopup = new Popup(getShell(), Popup.DFT_TITLE, inFeedback,
				Popup.DFT_WIDTH, 140);
		lPopup.setButtons(PopupButtons.CANCEL);
		lPopup.open();
	}

	private static class RegistrationForm extends AbstractRiplaForm {
		private static final int WIDTH_FIELD = 300;
		private final static IMessages MESSAGES = Activator.getMessages();
		private static final String[] GENDER = {
				MESSAGES.getMessage("widgets.view.form.select.sex.1"),
				MESSAGES.getMessage("widgets.view.form.select.sex.2") };
		private static final String[] EDUCATION = {
				MESSAGES.getMessage("widgets.view.form.select.educ.1"),
				MESSAGES.getMessage("widgets.view.form.select.educ.2"),
				MESSAGES.getMessage("widgets.view.form.select.educ.3"),
				MESSAGES.getMessage("widgets.view.form.select.educ.4") };
		private static final String[] WORKAREA = {
				MESSAGES.getMessage("widgets.view.form.select.work.1"),
				MESSAGES.getMessage("widgets.view.form.select.work.2"),
				MESSAGES.getMessage("widgets.view.form.select.work.3"),
				MESSAGES.getMessage("widgets.view.form.select.work.4"),
				MESSAGES.getMessage("widgets.view.form.select.work.5"),
				MESSAGES.getMessage("widgets.view.form.select.work.6") };
		private CCombo gender;
		private Text familyName;
		private Text firstName;
		private Text street;
		private Text postal;
		private Text city;
		private Text mail;
		private Text age;
		private CCombo education;
		private CCombo workarea;
		private FormModel model;
		private DataBindingHelper bindContext;

		public RegistrationForm(final Composite inParent) {
			super(inParent, 500);
		}

		protected String getMessages() {
			return bindContext.getStatusMsg();
		}

		protected boolean checkStatus() {
			return bindContext.checkStatus();
		}

		@Override
		protected void createControls() {
			model = FormModel.createEmptyModel();

			setNumColums(3);

			// gender
			createLabel(MESSAGES.getMessage("widgets.view.form.gender"));
			gender = createCombo(GENDER, 2, WIDTH_FIELD);
			gender.setFocus();
			// name
			createLabelRequired(MESSAGES.getMessage("widgets.view.form.name"));
			familyName = createText("", 2, WIDTH_FIELD);
			// first name
			createLabelRequired(MESSAGES
					.getMessage("widgets.view.form.firstname"));
			firstName = createText("", 2, WIDTH_FIELD);
			// street
			createLabel(MESSAGES.getMessage("widgets.view.form.street"));
			street = createText("", 2, WIDTH_FIELD);
			// plz/city
			createLabel(MESSAGES.getMessage("widgets.view.form.city"));
			postal = createText("", 1, 40);
			city = createText("", 1, WIDTH_FIELD - 45);
			// mail
			createLabelRequired(MESSAGES.getMessage("widgets.view.form.mail"));
			mail = createText("", 2, WIDTH_FIELD);
			// age
			createLabel(MESSAGES.getMessage("widgets.view.form.age"));
			age = createText("", 2, WIDTH_FIELD);
			// education
			createLabel(MESSAGES.getMessage("widgets.view.form.education"));
			education = createCombo(EDUCATION, 2, WIDTH_FIELD);
			// workarea
			createLabel(MESSAGES.getMessage("widgets.view.form.workarea"));
			workarea = createCombo(WORKAREA, 2, WIDTH_FIELD);

			bindContext = bindValues();
		}

		private DataBindingHelper bindValues() {
			final DataBindingHelper outCtx = new DataBindingHelper(
					FormModel.class, model);

			outCtx.bindValue(
					outCtx.getWidgetToOserve(WidgetProp.SELECTION, gender),
					outCtx.getModelToObserve("gender"));
			// family name is required
			outCtx.bindValueRequired(outCtx.getWidgetToOserve(WidgetProp.TEXT,
					familyName), outCtx.getModelToObserve("familyName"), outCtx
					.createStrategyBeforeSet(createRequiredValidator(MESSAGES
							.getMessage("widgets.view.form.name"))));
			// first name is required
			outCtx.bindValueRequired(outCtx.getWidgetToOserve(WidgetProp.TEXT,
					firstName), outCtx.getModelToObserve("firstName"), outCtx
					.createStrategyBeforeSet(createRequiredValidator(MESSAGES
							.getMessage("widgets.view.form.firstname"))));

			outCtx.bindValue(outCtx.getWidgetToOserve(WidgetProp.TEXT, street),
					outCtx.getModelToObserve("street"));
			outCtx.bindValue(outCtx.getWidgetToOserve(WidgetProp.TEXT, postal),
					outCtx.getModelToObserve("postal"));
			outCtx.bindValue(outCtx.getWidgetToOserve(WidgetProp.TEXT, city),
					outCtx.getModelToObserve("city"));
			// mail is required
			outCtx.bindValueRequired(outCtx.getWidgetToOserve(WidgetProp.TEXT,
					mail), outCtx.getModelToObserve("mail"), outCtx
					.createStrategyBeforeSet(new MailValidator(MESSAGES
							.getMessage("widgets.view.form.mail"))));

			// age is valid only as number > 0
			final IValidator lValidator = new IValidator() {
				@Override
				public IStatus validate(final Object inValue) {
					try {
						final int lValue = Integer.parseInt((String) inValue);
						if (lValue > 0) {
							return ValidationStatus.ok();
						}
						return ValidationStatus
								.error(MESSAGES
										.getFormattedMessage(
												"widgets.view.form.nr.gr.zero",
												MESSAGES.getMessage("widgets.view.form.age")));
					} catch (final NumberFormatException exc) {
						// intentionally left empty
					}
					return ValidationStatus.error(MESSAGES.getFormattedMessage(
							"widgets_view.form.nr.no",
							MESSAGES.getMessage("widgets.view.form.age")));
				}
			};
			outCtx.bindValueRequired(
					outCtx.getWidgetToOserve(WidgetProp.TEXT, age),
					outCtx.getModelToObserve("age"),
					outCtx.createStrategyAfterGet(lValidator));

			outCtx.bindValue(
					outCtx.getWidgetToOserve(WidgetProp.SELECTION, education),
					outCtx.getModelToObserve("education"));
			outCtx.bindValue(
					outCtx.getWidgetToOserve(WidgetProp.SELECTION, workarea),
					outCtx.getModelToObserve("workarea"));

			return outCtx;
		}

		public String getGender() {
			return gender.getText();
		}

		public String getName() {
			return familyName.getText();
		}

		public String getFirstName() {
			return firstName.getText();
		}

		public String getStreet() {
			return street.getText();
		}

		public String getPostal() {
			return postal.getText();
		}

		public String getCity() {
			return city.getText();
		}

		public String getMail() {
			return mail.getText();
		}

		public String getAge() {
			return age.getText();
		}

		public String getEducation() {
			return education.getText();
		}

		public String getWorkArea() {
			return workarea.getText();
		}
	}

	protected static class MailValidator extends RequiredValidator {
		protected MailValidator(final String inLabel) {
			super(inLabel, new IValidator() {
				@Override
				public IStatus validate(final Object inValue) {
					if (inValue.toString().matches("\\w+@\\w+\\.\\w+")) {
						return ValidationStatus.ok();
					}
					final IMessages lMessages = Activator.getMessages();
					return ValidationStatus
							.error(lMessages.getFormattedMessage(
									"widgets.view.form.mail.no",
									lMessages
											.getMessage("widgets.view.form.mail")));
				}
			});
		}
	}

}

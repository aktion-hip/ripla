/*******************************************************************************
 * Copyright (c) 2012 RelationWare, Benno Luthiger
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * RelationWare, Benno Luthiger
 ******************************************************************************/

package org.ripla.demo.widgets.controllers;

import org.ripla.demo.widgets.Activator;
import org.ripla.demo.widgets.Constants;
import org.ripla.demo.widgets.views.FormView;
import org.ripla.web.annotations.UseCaseController;
import org.ripla.web.controllers.AbstractController;
import org.ripla.web.exceptions.RiplaException;
import org.ripla.web.interfaces.IMessages;

import com.vaadin.ui.Component;

/**
 * Controller for the form demo.
 * 
 * @author Luthiger
 */
@UseCaseController
public class FormController extends AbstractController {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.controllers.AbstractController#needsPermission()
	 */
	@Override
	protected String needsPermission() {
		return Constants.PERMISSION_FORM;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.controllers.AbstractController#runChecked()
	 */
	@Override
	protected Component runChecked() throws RiplaException {
		loadContextMenu(Constants.CONTEXT_MENU_SET_WIDGETS);

		return new FormView(this);
	}

	/**
	 * Callback method to save the user input.
	 * 
	 * @param inGender
	 * @param inName
	 * @param inFirstName
	 * @param inStreet
	 * @param inPostal
	 * @param inCity
	 * @param inMail
	 * @param inAge
	 * @param inEducation
	 * @param inWorkArea
	 * @return String feedback
	 */
	public String save(final String inGender, final String inName, // NOPMD by Luthiger on 06.09.12 23:52
			final String inFirstName, final String inStreet,
			final String inPostal, final String inCity, final String inMail,
			final String inAge, final String inEducation,
			final String inWorkArea) {
		// nothing saved in this demo
		final IMessages lMessages = Activator.getMessages();
		final StringBuilder outFeedback = new StringBuilder();
		outFeedback.append("<ul>");
		appendChecked(outFeedback, inGender,
				lMessages.getMessage("widgets.view.form.gender"));
		appendChecked(outFeedback, inName,
				lMessages.getMessage("widgets.view.form.name"));
		appendChecked(outFeedback, inFirstName,
				lMessages.getMessage("widgets.view.form.firstname"));
		appendChecked(outFeedback, inStreet,
				lMessages.getMessage("widgets.view.form.street"));
		appendChecked(outFeedback, inPostal,
				lMessages.getMessage("widgets.view.form.city"));
		appendChecked(outFeedback, inCity,
				lMessages.getMessage("widgets.view.form.city"));
		appendChecked(outFeedback, inMail,
				lMessages.getMessage("widgets.view.form.mail"));
		appendChecked(outFeedback, inAge,
				lMessages.getMessage("widgets.view.form.age"));
		appendChecked(outFeedback, inEducation,
				lMessages.getMessage("widgets.view.form.education"));
		appendChecked(outFeedback, inWorkArea,
				lMessages.getMessage("widgets.view.form.workarea"));
		outFeedback.append("</ul>");
		return new String(outFeedback);
	}

	private void appendChecked(final StringBuilder inFeedback,
			final String inValue, final String inLabel) {
		if (inValue != null && !inValue.isEmpty()) {
			inFeedback.append("<li><b>").append(inLabel).append("</b>:")
					.append(inValue).append("</li>");
		}
	}

}

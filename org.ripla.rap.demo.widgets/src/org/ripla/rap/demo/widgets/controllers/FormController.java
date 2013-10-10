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

package org.ripla.rap.demo.widgets.controllers;

import org.eclipse.swt.widgets.Composite;
import org.ripla.annotations.UseCaseController;
import org.ripla.exceptions.RiplaException;
import org.ripla.interfaces.IMessages;
import org.ripla.rap.controllers.AbstractController;
import org.ripla.rap.demo.widgets.Activator;
import org.ripla.rap.demo.widgets.Constants;
import org.ripla.rap.demo.widgets.views.FormView;

/**
 * Controller for the RAP form widgets.
 * 
 * @author Luthiger
 */
@UseCaseController
public class FormController extends AbstractController {

	@Override
	protected String needsPermission() {
		return Constants.PERMISSION_FORM;
	}

	@Override
	protected Composite runChecked() throws RiplaException {
		loadContextMenu(Constants.CONTEXT_MENU_SET_WIDGETS);

		return new FormView(getParent(), this);
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
	public String save(
			final String inGender,
			final String inName, // NOPMD by Luthiger on 06.09.12 23:52
			final String inFirstName, final String inStreet,
			final String inPostal, final String inCity, final String inMail,
			final String inAge, final String inEducation,
			final String inWorkArea) {
		// nothing saved in this demo
		final IMessages lMessages = Activator.getMessages();
		final StringBuilder outFeedback = new StringBuilder("<p>");
		outFeedback.append(lMessages.getMessage("widgets.view.form.feedback"))
				.append("</p>");
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

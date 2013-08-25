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

package org.ripla.web.demo.widgets.controllers;

import org.ripla.annotations.UseCaseController;
import org.ripla.exceptions.RiplaException;
import org.ripla.interfaces.IMessages;
import org.ripla.web.controllers.AbstractController;
import org.ripla.web.demo.widgets.Activator;
import org.ripla.web.demo.widgets.Constants;
import org.ripla.web.demo.widgets.data.FormBean;
import org.ripla.web.demo.widgets.views.FormView;

import com.vaadin.ui.Component;

/**
 * Controller for the form demo.
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
	protected Component runChecked() throws RiplaException {
		loadContextMenu(Constants.CONTEXT_MENU_SET_WIDGETS);

		return new FormView(this);
	}

	/**
	 * Callback method to save the user input.
	 * 
	 * @param inFormItem
	 *            {@link FormBean}
	 * @return String feedback
	 */
	public String save(final FormBean inItem) {
		// nothing saved in this demo
		final IMessages lMessages = Activator.getMessages();
		final StringBuilder outFeedback = new StringBuilder();
		outFeedback.append("<ul>");
		appendChecked(outFeedback, inItem.getGender(),
				lMessages.getMessage("widgets.view.form.gender"));
		appendChecked(outFeedback, inItem.getName(),
				lMessages.getMessage("widgets.view.form.name"));
		appendChecked(outFeedback, inItem.getFirstName(),
				lMessages.getMessage("widgets.view.form.firstname"));
		appendChecked(outFeedback, inItem.getStreet(),
				lMessages.getMessage("widgets.view.form.street"));
		appendChecked(outFeedback, inItem.getPostal(),
				lMessages.getMessage("widgets.view.form.city"));
		appendChecked(outFeedback, inItem.getCity(),
				lMessages.getMessage("widgets.view.form.city"));
		appendChecked(outFeedback, inItem.getMail(),
				lMessages.getMessage("widgets.view.form.mail"));
		appendChecked(outFeedback, inItem.getAge(),
				lMessages.getMessage("widgets.view.form.age"));
		appendChecked(outFeedback, inItem.getEducation(),
				lMessages.getMessage("widgets.view.form.education"));
		appendChecked(outFeedback, inItem.getWorkArea(),
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

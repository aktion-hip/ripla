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
package org.ripla.web.internal.views;

import org.ripla.web.Activator;
import org.ripla.web.exceptions.NoControllerFoundException;
import org.ripla.web.interfaces.IMessages;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

/**
 * Default implementation of a view component.<br/>
 * Subclasses may extend.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class DefaultRiplaView extends CustomComponent {

	/**
	 * Creates view component displaying an exceptions message.
	 * 
	 * @param inExc
	 *            Exception
	 */
	public DefaultRiplaView(final Exception inExc) {
		super();

		final IMessages lMessages = Activator.getMessages();
		String lMessage = inExc.getMessage() == null ? inExc.toString() : inExc
				.getMessage();
		if (inExc instanceof NoControllerFoundException) {
			lMessage = lMessages.getMessage("errmsg.error.contactAdmin"); //$NON-NLS-1$
		}
		init(String.format(
				"<span style=\"color:red;\"><strong>%s:</strong> %s</span>", //$NON-NLS-1$
				lMessages.getMessage("label.error"), lMessage)); //$NON-NLS-1$
	}

	/**
	 * Creates view component displaying a simple message.
	 * 
	 * @param inMessage
	 *            String
	 */
	public DefaultRiplaView(final String inMessage) {
		super();

		init(String.format("<span>%s</span>", inMessage)); //$NON-NLS-1$
	}

	private void init(final String inMessage) {
		setSizeFull();
		final Label lLabel = new Label(inMessage, Label.CONTENT_XHTML);
		setCompositionRoot(lLabel);
	}

}
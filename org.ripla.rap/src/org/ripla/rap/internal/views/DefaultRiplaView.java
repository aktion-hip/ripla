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

package org.ripla.rap.internal.views;

import org.eclipse.swt.widgets.Composite;
import org.ripla.exceptions.NoControllerFoundException;
import org.ripla.rap.Activator;
import org.ripla.rap.util.AbstractRiplaView;
import org.ripla.rap.util.LabelHelper;

/**
 * Default implementation of a view component.<br/>
 * Subclasses may extend.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class DefaultRiplaView extends AbstractRiplaView {

	/**
	 * Creates view component displaying an error message.
	 * 
	 * @param inParent
	 *            {@link Composite}
	 * @param inExc
	 */
	public DefaultRiplaView(final Composite inParent, final Exception inExc) {
		super(inParent);

		String lMessage = inExc.getMessage() == null ? inExc.toString() : inExc
				.getMessage();
		if (inExc instanceof NoControllerFoundException) {
			lMessage = Activator.getMessages().getMessage(
					"errmsg.error.contactAdmin");
		}
		LabelHelper.createLabel(this, String.format(
				"<span style=\"color:red;\"><strong>%s:</strong> %s</span>", //$NON-NLS-1$
				Activator.getMessages().getMessage("errmsg.label"), lMessage));
	}

	/**
	 * Creates view component displaying a simple message.
	 * 
	 * @param inParent
	 *            {@link Composite}
	 * @param inMessage
	 *            String
	 */
	public DefaultRiplaView(final Composite inParent, final String inMessage) {
		super(inParent);

		LabelHelper.createLabel(this, inMessage, "ripla-default");
	}

}

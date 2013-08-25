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

package org.ripla.web.util;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

/**
 * Helper class providing various utility methods for label handling.
 * 
 * @author Luthiger
 */
public final class LabelHelper {

	private LabelHelper() {
	}

	/**
	 * xhtml for a label with a specified class attribute
	 */
	public static final String TMPL_LABEL = "<div class=\"%s\">%s</div>"; //$NON-NLS-1$

	public static Label createLabel(final String inLabel, final String inStyle) {
		return new Label(String.format(TMPL_LABEL, inStyle, inLabel),
				ContentMode.HTML);
	}

}

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

import com.vaadin.data.Validator;
import com.vaadin.server.Sizeable;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

/**
 * Constants and templates useful for views in use case providing bundles.
 * 
 * @author Luthiger
 */
public final class RiplaViewHelper {
	/**
	 * xhtml for a title tag with a specified class attribute
	 */
	public static final String TMPL_TITLE = "<div class=\"%s\">%s</div>"; //$NON-NLS-1$
	public static final String TMPL_WARNING = "<div class=\"vif-warning\">%s</div>"; //$NON-NLS-1$

	private RiplaViewHelper() {
	}

	/**
	 * Adds space to the layout:
	 * 
	 * <pre>
	 * &lt;div class="spacer">&lt;/div>
	 * </pre>
	 * 
	 * The amount of space added can be controlled in the application's css: css
	 * selector <code>div.spacer</code>.
	 * 
	 * @return Component
	 */
	public static Component createSpacer() {
		return new Label("<div class=\"spacer\"></div>", ContentMode.HTML); //$NON-NLS-1$
	}

	/**
	 * Creates a layout component with the specified buttons aligned
	 * horizontally.
	 * 
	 * @param inButtons
	 *            Button... the buttons to align
	 * @return {@link HorizontalLayout}
	 */
	public static HorizontalLayout createButtons(final Button... inButtons) {
		final HorizontalLayout outButtons = new HorizontalLayout();
		outButtons.setStyleName("ripla-buttons"); //$NON-NLS-1$
		outButtons.setSpacing(true);
		outButtons.setWidth("100%"); //$NON-NLS-1$
		for (final Button lButton : inButtons) {
			outButtons.addComponent(lButton);
		}
		final Button lLast = inButtons[inButtons.length - 1];
		outButtons.setExpandRatio(lLast, 1);
		outButtons.setComponentAlignment(lLast, Alignment.MIDDLE_LEFT);
		return outButtons;
	}

	/**
	 * Helper method to create a text field.
	 * 
	 * @param inContent
	 *            String the text field's content
	 * @param inWidth
	 *            int the field width (in pixels)
	 * @param inValidator
	 *            {@link Validator} the field's validator or <code>null</code>,
	 *            if no validation is applied
	 * @return {@link TextField}
	 */
	public static TextField createTextField(final String inContent,
			final int inWidth, final Validator inValidator) {
		final TextField out = new TextField(null, inContent);
		if (inValidator != null) {
			out.addValidator(inValidator);
		}
		out.setWidth(inWidth, Unit.PIXELS);
		out.setStyleName("ripla-input"); //$NON-NLS-1$
		return out;
	}

	/**
	 * Convenience method to make a label width undefined.
	 * 
	 * @param inLabel
	 *            {@link Label} to prepare
	 * @return Label with width <code>SIZE_UNDEFINED</code>
	 */
	public static Label makeUndefinedWidth(final Label inLabel) {
		inLabel.setWidth(Sizeable.SIZE_UNDEFINED, Unit.PIXELS);
		return inLabel;
	}

}

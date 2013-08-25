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

import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * <p>
 * The layout for the page footer.
 * </p>
 * <p>
 * Note: The component's styles are:<br />
 * <code>ripla-footer</code> and <code>ripla-footer-text</code>
 * </p>
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public final class FooterHelper extends CustomComponent {
	public static final String DFT_FOOTER_TEXT = "&copy; RelationWare"; //$NON-NLS-1$

	private HorizontalLayout layout;

	/**
	 * VIFFooter constructor
	 * 
	 * @param inFooterText
	 *            String the text to display in the footer
	 */
	private FooterHelper(final String inFooterText) {
		super();

		final HorizontalLayout lLayout = createLayout();
		setCompositionRoot(lLayout);
		populateLayout(lLayout, inFooterText);
	}

	private void populateLayout(final HorizontalLayout inLayout,
			final String inFooterText) {
		final Label lFooterText = LabelHelper.createLabel(inFooterText,
				"ripla-footer-text"); //$NON-NLS-1$
		inLayout.addComponent(lFooterText);
		inLayout.setComponentAlignment(lFooterText, Alignment.TOP_LEFT);
	}

	private HorizontalLayout createLayout() {
		layout = new HorizontalLayout();
		layout.setStyleName("ripla-footer"); //$NON-NLS-1$
		layout.setWidth("100%"); //$NON-NLS-1$
		return layout;
	}

	/**
	 * Sets the footers height.
	 * 
	 * @param inHeight
	 *            int the heigth in pixels
	 */
	public void setHeight(final int inHeight) {
		layout.setHeight(inHeight, Unit.PIXELS);
	}

	/**
	 * Overrides the component's default style name <code>ripla-footer</code>.
	 * 
	 * @param inStyle
	 *            String
	 */
	@Override
	public void setStyleName(final String inStyle) {
		layout.setStyleName(inStyle);
	}

	/**
	 * Factory method, returns a footer component.
	 * 
	 * @param inFooterText
	 *            String The text to be displayed in the footer.
	 * @return {@link FooterHelper}
	 */
	public static FooterHelper createFooter(final String inFooterText) {
		return new FooterHelper(inFooterText);
	}

}

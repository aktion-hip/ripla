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

package org.ripla.rap.util;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.JavaScriptExecutor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Helper class providing various utility methods for label handling.
 * 
 * @author Luthiger
 */
public final class LabelHelper {

	private LabelHelper() {
	}

	/**
	 * Creates a label with the specified css style.
	 * 
	 * @param inParent
	 *            {@link Composite}
	 * @param inLabel
	 *            String the label text
	 * @param inStyle
	 *            String the css style
	 * @return {@link Label}
	 */
	public static Label createLabel(final Composite inParent,
			final String inLabel, final String inStyle) {
		final Label out = new Label(inParent, SWT.NONE);
		out.setData(RWT.CUSTOM_VARIANT, inStyle);
		out.setText(inLabel);
		return out;
	}

	/**
	 * Creates a label with the specified css style.
	 * 
	 * @param inParent
	 *            {@link Composite}
	 * @param inLabel
	 *            String the label text
	 * @param inStyle
	 *            String the css style
	 * @param inHeight
	 *            int height in pixels
	 * @return {@link Label}
	 */
	public static Label createLabel(final Composite inParent,
			final String inLabel, final String inStyle, final int inHeight) {
		return createLabel(inParent, inLabel, inStyle, inHeight, SWT.NONE);
	}

	/**
	 * Creates a label with the specified css style and a font style.
	 * 
	 * @param inParent
	 *            {@link Composite}
	 * @param inLabel
	 *            String the label text
	 * @param inStyle
	 *            String the css style
	 * @param inHeight
	 *            int height in pixels
	 * @param inFontStyle
	 *            int bitwise OR of one or more of the SWT constants NORMAL,
	 *            BOLD and ITALIC
	 * @return {@link Label}
	 */
	public static Label createLabel(final Composite inParent,
			final String inLabel, final String inStyle, final int inHeight,
			final int inFontStyle) {
		final Label out = new Label(inParent, SWT.NONE);
		out.setData(RWT.CUSTOM_VARIANT, inStyle);
		out.setText(inLabel);
		setHeight(out, inHeight, inFontStyle);
		return out;
	}

	private static void setHeight(final Label inLabel, final int inHeight,
			final int inStyle) {
		final FontData lOld = inLabel.getFont().getFontData()[0];
		final FontData lNew = new FontData(lOld.getName(), inHeight,
				inStyle == SWT.NONE ? lOld.getStyle() : inStyle);
		inLabel.setFont(new Font(inLabel.getDisplay(), lNew));
	}

	/**
	 * Create a label displaying html
	 * 
	 * @param inParent
	 *            {@link Composite}
	 * @param inHtml
	 *            String
	 * @return {@link Label}
	 */
	public static Label createLabel(final Composite inParent,
			final String inHtml) {
		final Label out = new Label(inParent, SWT.NONE);
		out.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		out.setText(inHtml);
		return out;
	}

	/**
	 * Makes a label behaving as a link.
	 * 
	 * @param inControl
	 *            {@link Label} the label that should behave as a link
	 * @param inUrl
	 *            String the url to call when the link is clicked
	 */
	@SuppressWarnings("serial")
	public static void makeLink(final Label inControl, final String inUrl) {
		inControl.setCursor(inControl.getDisplay().getSystemCursor(
				SWT.CURSOR_HAND));
		inControl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(final MouseEvent inEvent) {
				final JavaScriptExecutor lExecutor = RWT.getClient()
						.getService(JavaScriptExecutor.class);
				if (lExecutor != null) {
					lExecutor.execute("window.location.href = '" + inUrl + "'");
				}
			}
		});
	}

}

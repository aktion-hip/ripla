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

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * Component to display html formated text in a browser embedded in a parent
 * view.
 * 
 * @author lbenno
 */
public class HtmlDisplay {
	private static final String HTML = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\">"
			+ "<style type=\"text/css\">"
			+ "body { font: 12px Verdana,sans-serif; }"
			+ "</style>"
			+ "</head>" + "<body>%s</body></html>";

	/**
	 * HtmlDisplay constructor.
	 * 
	 * @param inParent
	 *            {@link Composite} the parent component that is embedding the
	 *            browser view
	 * @param inHtml
	 *            String the html to render in the browser view
	 * @param inWidth
	 *            int Pixels widht hint
	 * @param inHeight
	 *            int Pixels height hint
	 */
	public HtmlDisplay(final Composite inParent, final String inHtml,
			final int inWidth, final int inHeight) {
		final Browser browser = new Browser(inParent, SWT.NONE);
		final GridData lLayoutData = GridLayoutHelper.createFillLayoutData();
		lLayoutData.widthHint = inWidth;
		lLayoutData.heightHint = inHeight;
		browser.setLayoutData(lLayoutData);
		browser.setText(String.format(HTML, inHtml));
	}

}

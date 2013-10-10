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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.ripla.rap.Activator;

/**
 * Popup window displaying arbitrary html formated text. Usage:
 * 
 * <pre>
 * final Popup popup = new Popup(getShell(), Popup.DFT_TITLE, &quot;&lt;p&gt;some html&lt;/p&gt;&quot;,
 * 		Popup.DFT_WIDTH, 140);
 * popup.setButtons(PopupButtons.CANCEL);
 * popup.open();
 * </pre>
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class Popup extends Dialog {
	private static final int POPUP_BORDER_HEIGHT = 106;
	private static final String HTML = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\">"
			+ "<style type=\"text/css\">"
			+ "body { font: 12px Verdana,sans-serif; }"
			+ "</style>"
			+ "</head>" + "<body>%s</body></html>";

	public static final String DFT_TITLE = Activator.getMessages().getMessage(
			"popup.dft.feedback");
	public static final int DFT_WIDTH = 300;

	public enum PopupButtons {
		OK, CANCEL, OK_CANCEL;
	}

	private final String title;
	private final String html;
	private Browser browser;
	private Composite dialogArea;
	private final int width;
	private final int height;
	private PopupButtons buttons;

	/**
	 * Popup window constructor.
	 * 
	 * @param inShell
	 *            {@link Shell}
	 * @param inTitle
	 *            String the title displayed on the popup window, e.g.
	 *            Popup.DFT_TITLE (= 'Feedback')
	 * @param inHtml
	 *            String html code, the text displayed on the popup
	 * @param inWidth
	 *            int width hint (Note: the popup window can not be less then,
	 *            e.g. Popup.DFT_WIDTH 285px width)
	 * @param inHeight
	 *            int the height hint (of the dialog area part)
	 */
	public Popup(final Shell inShell, final String inTitle,
			final String inHtml, final int inWidth, final int inHeight) {
		super(inShell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		title = inTitle;
		html = inHtml;
		width = inWidth;
		height = inHeight;
		buttons = PopupButtons.OK_CANCEL;
	}

	/**
	 * Sets the popup window's buttons style.
	 * 
	 * @param inButtons
	 *            {@link PopupButtons}: <code>OK, CANCEL, OK_CANCEL</code>
	 */
	public void setButtons(final PopupButtons inButtons) {
		buttons = inButtons;
	}

	@Override
	protected Control createDialogArea(final Composite inParent) {
		if (title != null) {
			getShell().setText(title);
		}

		dialogArea = (Composite) super.createDialogArea(inParent);
		final GridLayout lLayout = GridLayoutHelper.createGridLayout();
		lLayout.marginWidth = 7;
		dialogArea.setLayout(lLayout);
		final GridData lLayoutData = GridLayoutHelper.createFillLayoutData();
		lLayoutData.heightHint = height;
		lLayoutData.widthHint = width;
		browser = new Browser(dialogArea, SWT.NONE);
		browser.setLayoutData(lLayoutData);
		browser.setText(String.format(HTML, html));
		return dialogArea;
	}

	@Override
	protected void createButtonsForButtonBar(final Composite inParent) {
		switch (buttons) {
		case OK:
			createButton(inParent, IDialogConstants.OK_ID,
					IDialogConstants.get().OK_LABEL, true);
			break;
		case CANCEL:
			createButton(inParent, IDialogConstants.CANCEL_ID,
					IDialogConstants.get().CANCEL_LABEL, true);
			break;
		case OK_CANCEL:
		default:
			super.createButtonsForButtonBar(inParent);
			break;
		}
	}

	@Override
	protected Control createContents(final Composite inParent) {
		final Control out = super.createContents(inParent);
		getShell().addControlListener(new ControlListener() {
			@Override
			public void controlResized(final ControlEvent inEvent) {
				final Rectangle lSize = ((Control) inEvent.widget).getBounds();
				final GridData lLayoutData = (GridData) browser.getLayoutData();
				lLayoutData.heightHint = lSize.height - POPUP_BORDER_HEIGHT;
				lLayoutData.widthHint = lSize.width;
				browser.setLayoutData(lLayoutData);
				dialogArea.layout();
			}

			@Override
			public void controlMoved(final ControlEvent inE) {
				// do nothing
			}
		});
		return out;
	}

}

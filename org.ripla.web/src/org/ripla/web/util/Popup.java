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

import org.ripla.web.Activator;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Helper class to display a view component in a popup window.<br />
 * Usage:
 * 
 * <pre>
 * Popup.displayPopup(&quot;Look at this!&quot;, &quot;myComponent&quot;, 300, 600);
 * </pre>
 * 
 * @author Luthiger
 */
public final class Popup {

	private Popup() {
	}

	/**
	 * Displays the specified component in a popup window.
	 * 
	 * @param inTitle
	 *            String the popup's title
	 * @param inLayout
	 *            {@link Layout} the view component to display
	 * @param inWidth
	 *            int the window width
	 * @param inHeight
	 *            int the window height
	 */
	public static void displayPopup(final String inTitle,
			final Layout inLayout, final int inWidth, final int inHeight) {
		final PopupWindow lPopup = new PopupWindow(inTitle, inLayout, inWidth,
				inHeight);
		UI.getCurrent().addWindow(lPopup);
		lPopup.setPosition(50, 50);
	}

	// ---

	@SuppressWarnings("serial")
	public static class PopupWindow extends Window {

		/**
		 * Private PopupWindow constructor.
		 */
		PopupWindow(final String inTitle, final Layout inLayout,
				final int inWidth, final int inHeight) {
			super(inTitle);
			setWidth(inWidth, Unit.PIXELS);
			setHeight(inHeight, Unit.PIXELS);

			final VerticalLayout lLayout = new VerticalLayout();
			setContent(lLayout);
			lLayout.setStyleName("ripla-lookup"); //$NON-NLS-1$
			lLayout.setMargin(true);
			lLayout.setSpacing(true);
			lLayout.setSizeFull();
			lLayout.addComponent(inLayout);

			final Button lClose = new Button(Activator.getMessages()
					.getMessage("lookup.window.button.close"), //$NON-NLS-1$
					new Button.ClickListener() {
						@Override
						public void buttonClick(final ClickEvent inEvent) {
							UI.getCurrent().removeWindow(PopupWindow.this);
						}
					});
			lClose.setClickShortcut(KeyCode.ESCAPE);
			lClose.setImmediate(true);
			lClose.setStyleName("ripla-lookup-close"); //$NON-NLS-1$
			lLayout.addComponent(lClose);
		};

		protected void setPosition(final int inPositionX, final int inPositionY) {
			setPositionX(inPositionX);
			setPositionY(inPositionY);
		}
	}

}

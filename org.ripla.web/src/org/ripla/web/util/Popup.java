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

import org.ripla.web.Activator;
import org.ripla.web.internal.services.ApplicationData;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Layout;
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
		if (lPopup.getParent() == null) {
			ApplicationData.getWindow().addWindow(lPopup.getWindow());
		}
		lPopup.setPosition(50, 50);
	}

	// ---

	@SuppressWarnings("serial")
	public static class PopupWindow extends VerticalLayout {
		private final transient Window popupWindow; // NOPMD by Luthiger

		/**
		 * Private PopupWindow constructor.
		 */
		PopupWindow(final String inTitle, final Layout inLayout,
				final int inWidth, final int inHeight) {
			super();

			popupWindow = new Window(inTitle);
			popupWindow.setWidth(inWidth, Sizeable.UNITS_PIXELS);
			popupWindow.setHeight(inHeight, Sizeable.UNITS_PIXELS);

			final VerticalLayout lLayout = (VerticalLayout) popupWindow
					.getContent();
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
							(popupWindow.getParent()).removeWindow(popupWindow);
						}
					});
			lClose.setClickShortcut(KeyCode.ESCAPE);
			lClose.setImmediate(true);
			lClose.setStyleName("ripla-lookup-close"); //$NON-NLS-1$
			lLayout.addComponent(lClose);
		};

		/**
		 * @return {@link Window}
		 */
		@Override
		public Window getWindow() {
			return popupWindow;
		}

		protected void setPosition(final int inPositionX, final int inPositionY) {
			popupWindow.setPositionX(inPositionX);
			popupWindow.setPositionX(inPositionY);
		}

		/**
		 * Sets the dialog's visibility.
		 * 
		 * @param inVisible
		 *            boolean <code>true</code> makes the existing dialog window
		 *            visible, <code>false</code> makes the visible window
		 *            invisible
		 */
		@Override
		public void setVisible(final boolean inVisible) {
			popupWindow.setVisible(inVisible);
		}
	}

}

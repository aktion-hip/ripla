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

import java.util.Collection;

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
 * Popup.displayPopup(&quot;Look at this!&quot;, &lt;myComponent>, 300, 600);
 * Popup.newPopup(&quot;Look at this!&quot;, &lt;myComponent>, 300, 600).setClosable(false).build();
 * </pre>
 * 
 * @author Luthiger
 */
public final class Popup {

	private Popup() {
		// prevent instantiation
	}

	/**
	 * Helper method to create a popup using a builder.
	 * 
	 * @param inTitle
	 *            String the popup's title
	 * @param inLayout
	 *            {@link Layout} the view component to display
	 * @param inWidth
	 *            int the window width
	 * @param inHeight
	 *            int the window height
	 * @return {@link PopupBuilder} the builder for the popup window
	 */
	public static PopupBuilder newPopup(final String inTitle,
			final Layout inLayout, final int inWidth, final int inHeight) {
		return new Popup.PopupBuilder(inTitle, inLayout, inWidth, inHeight);
	}

	/**
	 * Convenience method to display the specified component in a popup window.
	 * 
	 * @param inTitle
	 *            String the popup's title
	 * @param inLayout
	 *            {@link Layout} the view component to display
	 * @param inWidth
	 *            int the window width
	 * @param inHeight
	 *            int the window height
	 * @return {@link PopupWindow} the created popup window
	 */
	public static PopupWindow displayPopup(final String inTitle,
			final Layout inLayout, final int inWidth, final int inHeight) {
		return newPopup(inTitle, inLayout, inWidth, inHeight).setClosable(true)
				.build();
	}

	/**
	 * Convenience method: removes all existing popup windows.
	 */
	public static void removePopups() {
		final UI lCurrent = UI.getCurrent();
		final Collection<Window> lSubWindows = lCurrent.getWindows();
		for (final Window lSubWindow : lSubWindows) {
			if (lSubWindow instanceof PopupWindow) {
				lCurrent.removeWindow(lSubWindow);
			}
		}
	}

	// ---

	public static class PopupBuilder {

		private transient final String title;
		private transient final Layout layout;
		private transient final int width;
		private transient final int height;
		private transient UI parent;
		private transient boolean closable;
		private transient int positionX = 50;
		private transient int positionY = 50;

		// prevent public instantiation
		private PopupBuilder(final String inTitle, final Layout inLayout,
				final int inWidth, final int inHeight) {
			title = inTitle;
			layout = inLayout;
			width = inWidth;
			height = inHeight;
		}

		/**
		 * @param inParent
		 *            {@link UI} the parent window
		 * @return {@link PopupBuilder}
		 */
		public PopupBuilder setParent(final UI inParent) {
			parent = inParent;
			return this;
		}

		/**
		 * @param inCloseable
		 *            boolean <code>true</code> to set the popup closeable
		 * @return {@link PopupBuilder}
		 */
		public PopupBuilder setClosable(final boolean inCloseable) {
			closable = inCloseable;
			return this;
		}

		/**
		 * @param inPositionX
		 *            int the windows x position
		 * @param inPositionY
		 *            int the windows y position
		 * @return {@link PopupBuilder}
		 */
		public PopupBuilder setPosition(final int inPositionX,
				final int inPositionY) {
			positionX = inPositionX;
			positionY = inPositionY;
			return this;
		}

		/**
		 * Builds the popup window.
		 * 
		 * @return {@link PopupWindow} the created popup window
		 */
		public PopupWindow build() {
			final PopupWindow outPopup = new PopupWindow(title, layout, width,
					height, closable);
			final UI lParent = parent == null ? UI.getCurrent() : parent;
			lParent.addWindow(outPopup);
			outPopup.setPosition(positionX, positionY);
			return outPopup;
		}

	}

	@SuppressWarnings("serial")
	public static class PopupWindow extends Window {

		/**
		 * Private PopupWindow constructor.
		 */
		PopupWindow(final String inTitle, final Layout inLayout,
				final int inWidth, final int inHeight, final boolean inCloseable) {
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

			setClosable(inCloseable);
			if (inCloseable) {
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
			}
		};

		protected void setPosition(final int inPositionX, final int inPositionY) {
			setPositionX(inPositionX);
			setPositionY(inPositionY);
		}
	}

}

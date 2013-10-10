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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolTip;

/**
 * Helper class to display tool tip notifications (i.e. non modal feedback).
 * 
 * @author Luthiger
 */
public final class NotificationHelper {
	private static final int BOX_HEIGHT = 80;
	private static final int BOX_WIDTH = 277;

	private enum Location {
		BOTTOM_RIGHT(1), CENTER(2);

		private int div;

		Location(final int inDiv) {
			div = inDiv;
		}

		protected Point getLocation(final Rectangle inBounds) {
			final int lX = inBounds.x + inBounds.width - BOX_WIDTH;
			final int lY = inBounds.y + inBounds.height - BOX_HEIGHT;
			return new Point(lX / div, lY / div);
		}
	}

	private static String message;
	private static Composite parent;
	private Location location;

	/**
	 * NotificationHelper constructor for displaying the specified message in a
	 * tray popup.
	 * 
	 * @param inMessage
	 *            String the notification message
	 * @param inParent
	 *            {@link Composite}
	 */
	public NotificationHelper(final String inMessage, final Composite inParent) {
		message = inMessage;
		parent = inParent;
		location = Location.BOTTOM_RIGHT;
	}

	protected void setLocation(final Location inLocation) {
		location = inLocation;
	}

	/**
	 * Displays the notification popup.
	 */
	public void showNotification() {
		final ToolTip lTip = new ToolTip(parent.getShell(), SWT.BALLOON
				| SWT.ICON_INFORMATION);
		lTip.setMessage(message);
		lTip.setLocation(location.getLocation(parent.getShell().getBounds()));
		lTip.setVisible(true);
	}

	/**
	 * Factory method for a tray notification.
	 * 
	 * @param inMessage
	 *            String the notification message
	 * @param inParent
	 *            {@link Composite}
	 */
	public static void showNotification(final String inMessage,
			final Composite inParent) {
		final NotificationHelper lNotification = new NotificationHelper(
				inMessage, inParent);
		lNotification.showNotification();
	}

	/**
	 * Factory method for a notification displayed in the center of the window.
	 * 
	 * @param inMessage
	 *            String the notification message
	 * @param inParent
	 *            {@link Composite}
	 */
	public static void showNotificationCenter(final String inMessage,
			final Composite inParent) {
		final NotificationHelper lNotification = new NotificationHelper(
				inMessage, inParent);
		lNotification.setLocation(Location.CENTER);
		lNotification.showNotification();

	}

}

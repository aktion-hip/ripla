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

package org.ripla.web.internal.services;

import org.osgi.service.event.Event;
import org.ripla.web.Constants;
import org.ripla.web.exceptions.NoControllerFoundException;
import org.ripla.web.interfaces.IBodyComponent;
import org.ripla.web.interfaces.IPluggable;
import org.ripla.web.internal.views.DefaultRiplaView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The application's event handler class.
 * 
 * @author Luthiger
 */
public final class RiplaEventHandler {
	private static final Logger LOG = LoggerFactory
			.getLogger(RiplaEventHandler.class);

	private transient IBodyComponent bodyComponent;

	/**
	 * @param inBody
	 *            {@link IBodyComponent}
	 */
	public void setBodyComponent(final IBodyComponent inBody) {
		bodyComponent = inBody;
	}

	/**
	 * Handle the event.
	 * 
	 * @param inEvent
	 *            {@link Event}
	 */
	@SuppressWarnings("unchecked")
	public void handleEvent(final Event inEvent) {
		// handle main view
		final Object lNext = inEvent
				.getProperty(Constants.EVENT_PROPERTY_NEXT_CONTROLLER);
		if (lNext != null) {
			LOG.debug("next task={}.", lNext); //$NON-NLS-1$
			try {
				bodyComponent.setContentView(bodyComponent
						.getContentComponent(lNext.toString()));
			}
			catch (final NoControllerFoundException exc) {
				handleNoTaskFound(exc, bodyComponent);
			}
		}

		// handle context menu
		final Object lContextMenuSet = inEvent
				.getProperty(Constants.EVENT_PROPERTY_CONTEXT_MENU_ID);
		if (lContextMenuSet != null) {
			LOG.debug("Event: displaying context menu={}", lContextMenuSet); //$NON-NLS-1$
			bodyComponent
					.setContextMenu(
							lContextMenuSet.toString(),
							(Class<? extends IPluggable>) inEvent
									.getProperty(Constants.EVENT_PROPERTY_CONTROLLER_ID));
		}

		// handle notifications
		final Object lNotification = inEvent
				.getProperty(Constants.EVENT_PROPERTY_NOTIFICATION_MSG);
		final Object lNotificationType = inEvent
				.getProperty(Constants.EVENT_PROPERTY_NOTIFICATION_TYPE);
		if (lNotification != null) {
			LOG.debug("Event: show notification \"{}\"", lNotification); //$NON-NLS-1$
			bodyComponent.showNotification((String) lNotification,
					((Integer) lNotificationType).intValue());
		}

		// refresh
		final Object lRefresh = inEvent
				.getProperty(Constants.EVENT_PROPERTY_REFRESH);
		if (lRefresh != null) {
			LOG.debug("Event: refresh body");
			bodyComponent.refreshBody();
		}

		// close
		final Object lClose = inEvent
				.getProperty(Constants.EVENT_PROPERTY_CLOSE);
		if (lClose != null) {
			LOG.debug("Event: close application");
			bodyComponent.close();
		}
	}

	private void handleNoTaskFound(final NoControllerFoundException inExc,
			final IBodyComponent inBody) {
		LOG.error("Configuration error:", inExc); //$NON-NLS-1$
		inBody.setContentView(new DefaultRiplaView(inExc));
	}

}

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
package org.ripla.rap.internal.services;

import java.util.Map;

import org.ripla.exceptions.NoControllerFoundException;
import org.ripla.interfaces.IRiplaEventDispatcher;
import org.ripla.rap.Constants;
import org.ripla.rap.app.RiplaApplication;
import org.ripla.rap.interfaces.IBodyComponent;
import org.ripla.rap.interfaces.IPluggable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The event dispatcher implementation.
 * 
 * @author Luthiger
 */
public class RiplaEventDispatcher implements IRiplaEventDispatcher {
	private static final Logger LOG = LoggerFactory
			.getLogger(RiplaEventDispatcher.class);

	private IBodyComponent bodyComponent;

	/**
	 * @param inRiplaBase
	 */
	public void setBodyComponent(final IBodyComponent inBody) {
		bodyComponent = inBody;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dispatch(final Event inType,
			final Map<String, Object> inProperties) {
		switch (inType) {
		case LOAD_CONTROLLER:
			final Object lNext = inProperties
					.get(Constants.EVENT_PROPERTY_NEXT_CONTROLLER);
			LOG.debug("next task={}.", lNext); //$NON-NLS-1$
			try {
				bodyComponent.setContentView(lNext.toString());
			} catch (final NoControllerFoundException exc) {
				handleNoTaskFound(exc, bodyComponent);
			}
			break;

		case LOAD_CONTEXT_MENU:
			final Object lContextMenuSet = inProperties
					.get(Constants.EVENT_PROPERTY_CONTEXT_MENU_ID);
			LOG.debug("Event: displaying context menu={}", lContextMenuSet); //$NON-NLS-1$
			try {
				bodyComponent.setContextMenu(lContextMenuSet.toString(),
						((Class<? extends IPluggable>) inProperties
								.get(Constants.EVENT_PROPERTY_CONTROLLER_ID)));
			} catch (final NoControllerFoundException exc) {
				handleNoTaskFound(exc, bodyComponent);
			}
			break;

		case REFRESH:
			LOG.debug("Event: refresh body");
			bodyComponent.refreshBody();
			break;

		case REFRESH_SKIN:
			final Object lSkinID = inProperties
					.get(Constants.EVENT_PROPERTY_SKIN_ID);
			LOG.debug("Event: change skin to {}", lSkinID);
			SkinRegistry.INSTANCE.changeSkin((String) lSkinID);
			break;

		case REFRESH_UI:
			LOG.debug("Event: refresh UI");
			bodyComponent.refreshBody();
			break;

		case LOGOUT:
			LOG.debug("Event: logout");
			final Object lDelay = inProperties
					.get(Constants.EVENT_PROPERTY_LOGOUT_DELAY);
			if (lDelay == null) {
				RiplaApplication.restart();
			} else {
				RiplaApplication.restart((Integer) lDelay);
			}
			break;
		}
	}

	private void handleNoTaskFound(final NoControllerFoundException inExc,
			final IBodyComponent inBody) {
		inBody.setContentView(inExc);
	}

}

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

package org.ripla.web.internal.services;

import java.util.Map;

import org.ripla.exceptions.NoControllerFoundException;
import org.ripla.interfaces.IRiplaEventDispatcher;
import org.ripla.web.Constants;
import org.ripla.web.interfaces.IBodyComponent;
import org.ripla.web.interfaces.IPluggable;
import org.ripla.web.internal.views.DefaultRiplaView;
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

	private transient IBodyComponent bodyComponent;

	/**
	 * @param inBody
	 *            {@link IBodyComponent}
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
				bodyComponent.setContentView(bodyComponent
						.getContentComponent(lNext.toString()));
			}
			catch (final NoControllerFoundException exc) {
				handleNoTaskFound(exc, bodyComponent);
			}
			break;

		case LOAD_CONTEXT_MENU:
			final Object lContextMenuSet = inProperties
					.get(Constants.EVENT_PROPERTY_CONTEXT_MENU_ID);
			LOG.debug("Event: displaying context menu={}", lContextMenuSet); //$NON-NLS-1$
			bodyComponent.setContextMenu(lContextMenuSet.toString(),
					((Class<? extends IPluggable>) inProperties
							.get(Constants.EVENT_PROPERTY_CONTROLLER_ID)));
			break;

		case REFRESH:
			LOG.debug("Event: refresh body");
			bodyComponent.refreshBody();
			break;

		case CLOSE:
			LOG.debug("Event: close application");
			bodyComponent.close();
			break;

		default:
			break;
		}

	}

	private void handleNoTaskFound(final NoControllerFoundException inExc,
			final IBodyComponent inBody) {
		LOG.error("Configuration error:", inExc); //$NON-NLS-1$
		inBody.setContentView(new DefaultRiplaView(inExc));
	}

}

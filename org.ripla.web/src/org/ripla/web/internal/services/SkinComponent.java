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

import org.ripla.services.ISkinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The service component consuming the <code>ISkinService</code>s.
 * 
 * @author Luthiger
 */
public class SkinComponent {
	private static final Logger LOG = LoggerFactory
			.getLogger(SkinComponent.class);

	/**
	 * Registers the specified skin.
	 * 
	 * @param inSkin
	 *            {@link ISkinService}
	 */
	public void registerSkin(final ISkinService inSkin) {
		LOG.debug("Registered skin '{}'.", inSkin.getSkinID());
		SkinRegistry.INSTANCE.registerSkin(inSkin);
	}

	/**
	 * Unregisters the specified skin.
	 * 
	 * @param inSkin
	 *            {@link ISkinService}
	 */
	public void unregisterSkin(final ISkinService inSkin) {
		LOG.debug("Unregistered skin '{}'.", inSkin.getSkinID());
		SkinRegistry.INSTANCE.unregisterSkin(inSkin);
	}

}

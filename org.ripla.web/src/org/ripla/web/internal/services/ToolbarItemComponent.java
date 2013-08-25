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

import org.ripla.web.services.IToolbarItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The service component consuming the <code>IToolbarItem</code> instances.
 * 
 * @author Luthiger
 */
public class ToolbarItemComponent {
	private static final Logger LOG = LoggerFactory
			.getLogger(ToolbarItemComponent.class);

	public void registerToolbarItem(final IToolbarItem inItem) {
		LOG.debug("Registered the toolbar item '{}'.", inItem);
		ToolbarItemRegistry.INSTANCE.registerToolbarItem(inItem);
	}

	public void unregisterToolbarItem(final IToolbarItem inItem) {
		LOG.debug("Unregistered the toolbar item '{}'.", inItem);
		ToolbarItemRegistry.INSTANCE.unregisterToolbarItem(inItem);
	}

}

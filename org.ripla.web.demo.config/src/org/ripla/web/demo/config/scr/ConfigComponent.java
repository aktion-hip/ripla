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

package org.ripla.web.demo.config.scr;

import org.osgi.service.cm.ConfigurationAdmin;
import org.ripla.services.ISkinService;
import org.ripla.web.demo.config.data.SkinConfigRegistry;

/**
 * The consumer of the <code>ISkin</code> service.
 * 
 * @author Luthiger
 */
public class ConfigComponent {

	public void registerSkin(final ISkinService inSkin) {
		SkinConfigRegistry.INSTANCE.register(inSkin);
	}

	public void unregisterSkin(final ISkinService inSkin) {
		SkinConfigRegistry.INSTANCE.unregister(inSkin);
	}

	public void setConfigAdmin(final ConfigurationAdmin inConfigAdmin) {
		SkinConfigRegistry.INSTANCE.setConfigAdmin(inConfigAdmin);
	}

	public void unsetConfigAdmin(final ConfigurationAdmin inConfigAdmin) {
		SkinConfigRegistry.INSTANCE.setConfigAdmin(null);
	}

}

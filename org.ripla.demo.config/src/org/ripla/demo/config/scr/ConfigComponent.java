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

package org.ripla.demo.config.scr;

import org.ripla.demo.config.data.SkinConfigRegistry;
import org.ripla.web.services.ISkin;

/**
 * The consumer of the <code>ISkin</code> service.
 * 
 * @author Luthiger
 */
public class ConfigComponent {

	public void registerSkin(final ISkin inSkin) {
		SkinConfigRegistry.INSTANCE.register(inSkin);
	}

	public void unregisterSkin(final ISkin inSkin) {
		SkinConfigRegistry.INSTANCE.unregister(inSkin);
	}

}

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

package org.ripla.web.demo.config.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.ripla.services.ISkinService;

/**
 * The configuration bundle's skin registry.
 * 
 * @author Luthiger
 */
public enum SkinConfigRegistry {
	INSTANCE;

	private final Collection<ISkinService> skins = new ArrayList<ISkinService>();

	/**
	 * @param inSkin
	 */
	public void register(final ISkinService inSkin) {
		skins.add(inSkin);
	}

	/**
	 * @param inSkin
	 */
	public void unregister(final ISkinService inSkin) {
		skins.remove(inSkin);
	}

	public List<SkinBean> getSkins() {
		final List<SkinBean> out = new ArrayList<SkinBean>(skins.size());
		for (final ISkinService lSkin : skins) {
			out.add(new SkinBean(lSkin.getSkinID(), lSkin.getSkinName())); // NOPMD
		}
		Collections.sort(out);
		return out;
	}

}

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

package org.ripla.demo.config.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.ripla.web.services.ISkin;

/**
 * The configuration bundle's skin registry.
 * 
 * @author Luthiger
 */
public enum SkinConfigRegistry {
	INSTANCE;

	private Collection<ISkin> skins = new ArrayList<ISkin>();

	/**
	 * @param inSkin
	 */
	public void register(ISkin inSkin) {
		skins.add(inSkin);
	}

	/**
	 * @param inSkin
	 */
	public void unregister(ISkin inSkin) {
		skins.remove(inSkin);
	}
	
	public List<SkinBean> getSkins() {
		List<SkinBean> out = new ArrayList<SkinBean>(skins.size());
		for (ISkin lSkin : skins) {
			out.add(new SkinBean(lSkin.getSkinID(), lSkin.getSkinName()));
		}
		Collections.sort(out);
		return out;
	}
	
}

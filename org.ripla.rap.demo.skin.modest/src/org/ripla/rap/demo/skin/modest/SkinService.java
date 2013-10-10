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
package org.ripla.rap.demo.skin.modest;

import org.ripla.rap.services.ISkin;
import org.ripla.rap.services.ISkinService;
import org.ripla.rap.util.AbstractSkinService;

/**
 * The service to create a modest skin.
 * 
 * @author Luthiger
 */
public class SkinService extends AbstractSkinService {
	public static final String SKIN_ID = "org.ripla.rap.demo.skin.modest";

	@Override
	public String getSkinID() {
		return SKIN_ID;
	}

	@Override
	public String getSkinName() {
		return "Modest Demo Skin (for RAP)";
	}

	@Override
	public ISkin createSkin() {
		return new Skin();
	}

	@Override
	protected String getStyleSheetLocation() {
		return "theme/modest.css";
	}

	@Override
	protected Class<? extends ISkinService> getSkinBundleClass() {
		return getClass();
	}

}

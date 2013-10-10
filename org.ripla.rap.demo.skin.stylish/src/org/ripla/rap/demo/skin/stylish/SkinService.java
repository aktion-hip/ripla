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
package org.ripla.rap.demo.skin.stylish;

import org.ripla.rap.services.ISkinService;
import org.ripla.rap.util.AbstractSkinService;
import org.ripla.services.ISkin;

/**
 * The service to create a stylish skin.
 * 
 * @author Luthiger
 */
public class SkinService extends AbstractSkinService {
	public static final String SKIN_ID = "org.ripla.rap.demo.skin.stylish";

	@Override
	public String getSkinID() {
		return SKIN_ID;
	}

	@Override
	public String getSkinName() {
		return "Stylish Demo Skin (for RAP)";
	}

	@Override
	public ISkin createSkin() {
		return new Skin();
	}

	@Override
	protected String getStyleSheetLocation() {
		return "theme/stylish.css";
	}

	@Override
	protected Class<? extends ISkinService> getSkinBundleClass() {
		return getClass();
	}

}

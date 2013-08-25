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
package org.ripla.web.demo.skin.stylish;

import org.ripla.services.ISkin;
import org.ripla.services.ISkinService;

/**
 * The service to create a stylish skin.
 * 
 * @author Luthiger
 */
public class SkinService implements ISkinService {
	public static final String SKIN_ID = "org.ripla.web.demo.skin.stylish";

	@Override
	public String getSkinID() {
		return SKIN_ID;
	}

	@Override
	public String getSkinName() {
		return "Stylish Demo Skin (Runo)";
	}

	@Override
	public ISkin createSkin() {
		return new Skin();
	}

}

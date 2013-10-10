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
package org.ripla.rap.demo.config.controller;

import org.eclipse.swt.widgets.Composite;
import org.ripla.annotations.UseCaseController;
import org.ripla.exceptions.RiplaException;
import org.ripla.rap.controllers.AbstractController;
import org.ripla.rap.demo.config.Constants;
import org.ripla.rap.demo.config.data.SkinConfigRegistry;
import org.ripla.rap.demo.config.views.SkinConfigurationView;
import org.ripla.util.PreferencesHelper;

/**
 * The controller for the use case to select the application's skin.
 * 
 * @author Luthiger
 */
@UseCaseController
public class SkinSelectController extends AbstractController {

	@Override
	protected String needsPermission() {
		return Constants.PERMISSION_SELECT_SKIN;
	}

	@Override
	protected Composite runChecked() throws RiplaException {
		emptyContextMenu();

		return new SkinConfigurationView(getParent(), this, getPreference(
				PreferencesHelper.KEY_SKIN, ""));
	}

	/**
	 * Callback method, saves the changed skin selection.
	 * 
	 * @param inSkinId
	 *            String the selected skin's ID
	 */
	public void save(final String inSkinId) {
		SkinConfigRegistry.INSTANCE.changeSkin(inSkinId);
		logout(1000);
	}

}

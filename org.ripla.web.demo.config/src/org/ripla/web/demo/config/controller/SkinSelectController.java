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

package org.ripla.web.demo.config.controller;

import org.ripla.annotations.UseCaseController;
import org.ripla.exceptions.RiplaException;
import org.ripla.util.PreferencesHelper;
import org.ripla.web.controllers.AbstractController;
import org.ripla.web.demo.config.Constants;
import org.ripla.web.demo.config.data.SkinBean;
import org.ripla.web.demo.config.views.SkinConfigurationView;

import com.vaadin.ui.Component;

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
	protected Component runChecked() throws RiplaException {
		emptyContextMenu();

		return new SkinConfigurationView(this);
	}

	/**
	 * Callback method, saves the changed skin selection.
	 * 
	 * @param inSkin
	 *            {@link SkinBean}
	 * @param inApplication
	 */
	public void save(final SkinBean inSkin) { // , final Application
												// inApplication
		savePreferences(PreferencesHelper.KEY_SKIN, inSkin.getID());
		// TODO
		// inApplication.setTheme(inSkin.getID());
	}

}

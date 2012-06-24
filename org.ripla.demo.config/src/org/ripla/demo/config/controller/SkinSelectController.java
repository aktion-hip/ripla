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

package org.ripla.demo.config.controller;

import org.ripla.demo.config.Constants;
import org.ripla.demo.config.data.SkinBean;
import org.ripla.demo.config.views.SkinConfigurationView;
import org.ripla.web.annotations.UseCaseController;
import org.ripla.web.controllers.AbstractController;
import org.ripla.web.exceptions.RiplaException;
import org.ripla.web.util.PreferencesHelper;

import com.vaadin.Application;
import com.vaadin.ui.Component;

/**
 * The controller for the use case to select the application's skin.
 * 
 * @author Luthiger
 */
@UseCaseController
public class SkinSelectController extends AbstractController {
	
	/* (non-Javadoc)
	 * @see org.ripla.web.controllers.AbstractController#needsPermission()
	 */
	@Override
	protected String needsPermission() {
		return Constants.PERMISSION_SELECT_SKIN;
	}

	/* (non-Javadoc)
	 * @see org.ripla.web.controllers.AbstractController#runChecked()
	 */
	@Override
	protected Component runChecked() throws RiplaException {
		emptyContextMenu();
		
		return new SkinConfigurationView(this);
	}

	/**
	 * Callback method, saves the changed skin selection.
	 * 
	 * @param inSkin {@link SkinBean}
	 * @param inApplication
	 */
	public void save(SkinBean inSkin, Application inApplication) {
		savePreferences(PreferencesHelper.KEY_SKIN, inSkin.getID());
		inApplication.setTheme(inSkin.getID());
	}

}

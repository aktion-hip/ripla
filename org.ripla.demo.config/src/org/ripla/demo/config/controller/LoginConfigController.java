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
import org.ripla.demo.config.views.LoginConfigView;
import org.ripla.web.annotations.UseCaseController;
import org.ripla.web.controllers.AbstractController;
import org.ripla.web.exceptions.RiplaException;

import com.vaadin.ui.Component;

/**
 * The controller for the login configuration.
 * 
 * @author Luthiger
 */
@UseCaseController
public class LoginConfigController extends AbstractController {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.controllers.AbstractController#needsPermission()
	 */
	@Override
	protected String needsPermission() {
		return Constants.PERMISSION_LOGIN_CONFIG;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.controllers.AbstractController#runChecked()
	 */
	@Override
	protected Component runChecked() throws RiplaException {
		emptyContextMenu();

		final String lLoginConfig = getPreference(
				org.ripla.demo.Constants.KEY_LOGIN, Boolean.FALSE.toString());
		return new LoginConfigView(
				Boolean.parseBoolean(lLoginConfig),
				this,
				getUserAdminRole(org.ripla.demo.Constants.ADMIN_GROUP_NAME) != null);
	}

	/**
	 * Callback method
	 * 
	 * @param inLoginConfig
	 */
	public void saveChange(final boolean inLoginConfig) {
		savePreferences(org.ripla.demo.Constants.KEY_LOGIN,
				Boolean.toString(inLoginConfig));
		closeApp();
	}

}

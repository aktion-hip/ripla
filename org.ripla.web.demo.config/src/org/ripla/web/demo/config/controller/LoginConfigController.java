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
import org.ripla.web.controllers.AbstractController;
import org.ripla.web.demo.config.Constants;
import org.ripla.web.demo.config.views.LoginConfigView;

import com.vaadin.ui.Component;

/**
 * The controller for the login configuration.
 * 
 * @author Luthiger
 */
@UseCaseController
public class LoginConfigController extends AbstractController {

	@Override
	protected String needsPermission() {
		return Constants.PERMISSION_LOGIN_CONFIG;
	}

	@Override
	protected Component runChecked() throws RiplaException {
		emptyContextMenu();

		final String lLoginConfig = getPreference(
				org.ripla.web.demo.Constants.KEY_LOGIN,
				Boolean.FALSE.toString());
		return new LoginConfigView(
				Boolean.parseBoolean(lLoginConfig),
				this,
				getUserAdminRole(org.ripla.web.demo.Constants.ADMIN_GROUP_NAME) != null);
	}

	/**
	 * Callback method
	 * 
	 * @param inLoginConfig
	 */
	public void saveChange(final Boolean inLoginConfig) {
		savePreferences(
				org.ripla.web.demo.Constants.KEY_LOGIN,
				inLoginConfig == null ? Boolean.FALSE.toString() : Boolean
						.toString(inLoginConfig));
		closeApp();
	}

}

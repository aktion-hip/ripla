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
import org.ripla.rap.demo.config.views.LoginConfigView;

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
	protected Composite runChecked() throws RiplaException {
		emptyContextMenu();

		final String lLoginConfig = getPreference(
				org.ripla.rap.demo.exp.Constants.KEY_LOGIN,
				Boolean.FALSE.toString());
		return new LoginConfigView(
				getParent(),
				Boolean.parseBoolean(lLoginConfig),
				this,
				getUserAdminRole(org.ripla.rap.demo.exp.Constants.ADMIN_GROUP_NAME) != null);
	}

	/**
	 * Callback method
	 * 
	 * @param inLoginConfig
	 *            boolean
	 */
	public void saveChange(final boolean inLoginConfig) {
		savePreferences(org.ripla.rap.demo.exp.Constants.KEY_LOGIN,
				Boolean.toString(inLoginConfig));
		logout(0);
	}

}

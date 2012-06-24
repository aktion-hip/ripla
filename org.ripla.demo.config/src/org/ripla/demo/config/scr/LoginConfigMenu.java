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

package org.ripla.demo.config.scr;

import java.util.Collections;
import java.util.List;

import org.ripla.demo.config.Activator;
import org.ripla.demo.config.Constants;
import org.ripla.demo.config.controller.LoginConfigController;
import org.ripla.web.interfaces.IMenuItem;
import org.ripla.web.interfaces.IMessages;
import org.ripla.web.menu.ExtendibleMenuMarker.Position;
import org.ripla.web.menu.ExtendibleMenuMarker.PositionType;
import org.ripla.web.services.IExtendibleMenuContribution;
import org.ripla.web.util.UseCaseHelper;

/**
 * Menu item calling the controller to configur the login menu.<br />
 * This is an OSGi provider for the <code>IExtendibleMenuContribution</code> service, 
 * i.e. this class implements a contribution of an extendible menu.
 * 
 * @author Luthiger
 */
public class LoginConfigMenu implements IExtendibleMenuContribution {
	private static final IMessages MESSAGES = Activator.getMessages();
	
	public String getExtendibleMenuID() {
		return Constants.EXTENDIBLE_MENU_ID;
	}
	public String getLabel() {
		return MESSAGES.getMessage("config.menu.login.config"); //$NON-NLS-1$
	}
	public String getControllerName() {
		return UseCaseHelper.createFullyQualifiedControllerName(LoginConfigController.class);
	}
	public Position getPosition() {
		return new Position(PositionType.APPEND, Constants.EXTENDIBLE_MENU_POSITION_START);
	}
	public String getPermission() {
		return Constants.PERMISSION_LOGIN_CONFIG;
	}
	public List<IMenuItem> getSubMenu() {
		return Collections.emptyList();
	}

}

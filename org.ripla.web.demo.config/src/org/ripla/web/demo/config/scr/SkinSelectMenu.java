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

package org.ripla.web.demo.config.scr;

import java.util.Collections;
import java.util.List;

import org.ripla.interfaces.IMenuItem;
import org.ripla.interfaces.IMessages;
import org.ripla.services.IExtendibleMenuContribution;
import org.ripla.util.ExtendibleMenuMarker;
import org.ripla.web.demo.config.Activator;
import org.ripla.web.demo.config.Constants;
import org.ripla.web.demo.config.controller.SkinSelectController;
import org.ripla.web.util.UseCaseHelper;

/**
 * Menu item calling the controller to select a skin.<br />
 * This is an OSGi provider for the <code>IExtendibleMenuContribution</code>
 * service, i.e. this class implements a contribution of an extendible menu.
 * 
 * @author Luthiger
 */
public class SkinSelectMenu implements IExtendibleMenuContribution {
	private static final IMessages MESSAGES = Activator.getMessages();

	public String getExtendibleMenuID() {
		return Constants.EXTENDIBLE_MENU_ID;
	}

	public String getLabel() {
		return MESSAGES.getMessage("config.menu.select.skin"); //$NON-NLS-1$
	}

	public String getControllerName() {
		return UseCaseHelper
				.createFullyQualifiedControllerName(SkinSelectController.class);
	}

	public ExtendibleMenuMarker.Position getPosition() {
		return new ExtendibleMenuMarker.Position(
				ExtendibleMenuMarker.PositionType.APPEND,
				Constants.EXTENDIBLE_MENU_POSITION_START);
	}

	public String getPermission() {
		return Constants.PERMISSION_SELECT_SKIN;
	}

	public List<IMenuItem> getSubMenu() {
		return Collections.emptyList();
	}

}

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
package org.ripla.rap.demo.config.scr;

import java.util.Collections;
import java.util.List;

import org.ripla.interfaces.IMenuItem;
import org.ripla.interfaces.IMessages;
import org.ripla.rap.demo.config.Activator;
import org.ripla.rap.demo.config.Constants;
import org.ripla.rap.demo.config.controller.SkinSelectController;
import org.ripla.rap.util.UseCaseHelper;
import org.ripla.services.IExtendibleMenuContribution;
import org.ripla.util.ExtendibleMenuMarker.Position;
import org.ripla.util.ExtendibleMenuMarker.PositionType;

/**
 * Menu item calling the controller to select a skin.<br />
 * This is an OSGi provider for the <code>IExtendibleMenuContribution</code>
 * service, i.e. this class implements a contribution of an extendible menu.
 * 
 * @author Luthiger
 */
public class SkinSelectMenu implements IExtendibleMenuContribution {
	private static final IMessages MESSAGES = Activator.getMessages();

	@Override
	public String getExtendibleMenuID() {
		return Constants.EXTENDIBLE_MENU_ID;
	}

	@Override
	public String getLabel() {
		return MESSAGES.getMessage("config.menu.select.skin"); //$NON-NLS-1$
	}

	@Override
	public String getControllerName() {
		return UseCaseHelper
				.createFullyQualifiedControllerName(SkinSelectController.class);
	}

	@Override
	public Position getPosition() {
		return new Position(PositionType.APPEND,
				Constants.EXTENDIBLE_MENU_POSITION_START);
	}

	@Override
	public String getPermission() {
		return Constants.PERMISSION_SELECT_SKIN;
	}

	@Override
	public List<IMenuItem> getSubMenu() {
		return Collections.emptyList();
	}

}

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

import org.ripla.interfaces.IControllerSet;
import org.ripla.interfaces.IMenuItem;
import org.ripla.util.ExtendibleMenuMarker;
import org.ripla.web.demo.config.Activator;
import org.ripla.web.demo.config.Constants;
import org.ripla.web.demo.config.controller.SkinSelectController;
import org.ripla.web.interfaces.IMenuSet;
import org.ripla.web.menu.AbstractExtendibleMenu;
import org.ripla.web.services.IUseCase;
import org.ripla.web.util.UseCaseHelper;

/**
 * This bundle's provider of the <code>IUseCase</code> service.
 * 
 * @author Luthiger
 */
public class UseCaseComponent implements IUseCase {

	@Override
	public IMenuItem getMenu() {
		return new ExtendibleMenu();
	}

	@Override
	public Package getControllerClasses() {
		return SkinSelectController.class.getPackage();
	}

	@Override
	public IControllerSet getControllerSet() {
		return UseCaseHelper.EMPTY_CONTROLLER_SET;
	}

	@Override
	public IMenuSet[] getContextMenus() {
		return UseCaseHelper.EMPTY_CONTEXT_MENU_SET;
	}

	// ---

	public static class ExtendibleMenu extends AbstractExtendibleMenu {

		@Override
		public String getMenuID() {
			return Constants.EXTENDIBLE_MENU_ID;
		}

		@Override
		public ExtendibleMenuMarker[] getMarkers() {
			return new ExtendibleMenuMarker[] {
					new ExtendibleMenuMarker(
							Constants.EXTENDIBLE_MENU_POSITION_START),
					new ExtendibleMenuMarker(
							Constants.EXTENDIBLE_MENU_POSITION_ADDITIONS),
					new ExtendibleMenuMarker(
							Constants.EXTENDIBLE_MENU_POSITION_END) };
		}

		@Override
		public int getPosition() {
			return 50;
		}

		@Override
		public String getLabel() {
			return Activator.getMessages().getMessage("component.menu.title"); //$NON-NLS-1$
		}
	}

}

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

import org.ripla.demo.config.Activator;
import org.ripla.demo.config.Constants;
import org.ripla.demo.config.controller.SkinSelectController;
import org.ripla.web.interfaces.IControllerSet;
import org.ripla.web.interfaces.IMenuItem;
import org.ripla.web.interfaces.IMenuSet;
import org.ripla.web.menu.AbstractExtendibleMenu;
import org.ripla.web.menu.ExtendibleMenuMarker;
import org.ripla.web.services.IUseCase;
import org.ripla.web.util.UseCaseHelper;

/**
 * This bundle's provider of the <code>IUseCase</code> service.
 * 
 * @author Luthiger
 */
public class UseCaseComponent implements IUseCase {

	/* (non-Javadoc)
	 * @see org.ripla.web.services.IUseCase#getMenu()
	 */
	@Override
	public IMenuItem getMenu() {
		return new ExtendibleMenu();
	}

	/* (non-Javadoc)
	 * @see org.ripla.web.services.IUseCase#getControllerClasses()
	 */
	@Override
	public Package getControllerClasses() {
		return SkinSelectController.class.getPackage();
	}

	/* (non-Javadoc)
	 * @see org.ripla.web.services.IUseCase#getControllerSet()
	 */
	@Override
	public IControllerSet getControllerSet() {
		return UseCaseHelper.EMPTY_CONTROLLER_SET;
	}

	/* (non-Javadoc)
	 * @see org.ripla.web.services.IUseCase#getContextMenus()
	 */
	@Override
	public IMenuSet[] getContextMenus() {
		return UseCaseHelper.EMPTY_CONTEXT_MENU_SET;
	}
	
// ---
	
	public static class ExtendibleMenu extends AbstractExtendibleMenu {

		/* (non-Javadoc)
		 * @see org.ripla.web.interfaces.IMenuExtendible#getMenuID()
		 */
		@Override
		public String getMenuID() {
			return Constants.EXTENDIBLE_MENU_ID;
		}
	
		/* (non-Javadoc)
		 * @see org.ripla.web.interfaces.IMenuExtendible#getMarkers()
		 */
		@Override
		public ExtendibleMenuMarker[] getMarkers() {
			return new ExtendibleMenuMarker[] {
					new ExtendibleMenuMarker(Constants.EXTENDIBLE_MENU_POSITION_START),
					new ExtendibleMenuMarker(Constants.EXTENDIBLE_MENU_POSITION_ADDITIONS),
					new ExtendibleMenuMarker(Constants.EXTENDIBLE_MENU_POSITION_END)
			};
		}
	
		/* (non-Javadoc)
		 * @see org.ripla.web.interfaces.IMenuItem#getPosition()
		 */
		@Override
		public int getPosition() {
			return 50;
		}
	
		/* (non-Javadoc)
		 * @see org.ripla.web.interfaces.IMenuElement#getLabel()
		 */
		@Override
		public String getLabel() {
			return Activator.getMessages().getMessage("component.menu.title"); //$NON-NLS-1$
		}
	}

}

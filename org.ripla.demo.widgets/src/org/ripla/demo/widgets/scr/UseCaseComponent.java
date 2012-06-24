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

package org.ripla.demo.widgets.scr;

import org.ripla.demo.widgets.Activator;
import org.ripla.demo.widgets.Constants;
import org.ripla.demo.widgets.controllers.ButtonWidgetsController;
import org.ripla.demo.widgets.controllers.FormController;
import org.ripla.demo.widgets.controllers.InputWidgetsController;
import org.ripla.demo.widgets.controllers.SelectionWidgetsController;
import org.ripla.demo.widgets.controllers.TableWidgetsController;
import org.ripla.demo.widgets.controllers.TreeWidgetsController;
import org.ripla.web.interfaces.IControllerSet;
import org.ripla.web.interfaces.IMenuItem;
import org.ripla.web.interfaces.IMenuSet;
import org.ripla.web.interfaces.IMessages;
import org.ripla.web.menu.RiplaMenuComposite;
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
		return createMenu();
	}

	/* (non-Javadoc)
	 * @see org.ripla.web.services.IUseCase#getControllerClasses()
	 */
	@Override
	public Package getControllerClasses() {
		return InputWidgetsController.class.getPackage();
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
		return new IMenuSet[] {ContextMenuHelper.createContextMenuSet()};
	}
	
// ---	

	/**
	 * @return IMenuItem
	 */
	private IMenuItem createMenu() {
		IMessages lMessages = Activator.getMessages();
		RiplaMenuComposite outMenu = new RiplaMenuComposite(lMessages.getMessage("component.menu.title"), 10); //$NON-NLS-1$
		outMenu.setControllerName(UseCaseHelper.createFullyQualifiedControllerName(InputWidgetsController.class));
		outMenu.setPermission(Constants.PERMISSION_INPUT_WIDGETS);
		
		RiplaMenuComposite lSubMenu = new RiplaMenuComposite(lMessages.getMessage("widgets.menu.button"), 10); //$NON-NLS-1$
		lSubMenu.setControllerName(UseCaseHelper.createFullyQualifiedControllerName(ButtonWidgetsController.class));
		outMenu.add(lSubMenu);
		
		lSubMenu = new RiplaMenuComposite(lMessages.getMessage("widgets.menu.selection"), 20); //$NON-NLS-1$
		lSubMenu.setControllerName(UseCaseHelper.createFullyQualifiedControllerName(SelectionWidgetsController.class));
		outMenu.add(lSubMenu);
		
		lSubMenu = new RiplaMenuComposite(lMessages.getMessage("widgets.menu.form"), 30); //$NON-NLS-1$
		lSubMenu.setControllerName(UseCaseHelper.createFullyQualifiedControllerName(FormController.class));
		outMenu.add(lSubMenu);
		
		lSubMenu = new RiplaMenuComposite(lMessages.getMessage("widgets.menu.table"), 40); //$NON-NLS-1$
		lSubMenu.setControllerName(UseCaseHelper.createFullyQualifiedControllerName(TableWidgetsController.class));
		outMenu.add(lSubMenu);
		
		lSubMenu = new RiplaMenuComposite(lMessages.getMessage("widgets.menu.tree"), 50); //$NON-NLS-1$
		lSubMenu.setControllerName(UseCaseHelper.createFullyQualifiedControllerName(TreeWidgetsController.class));
		outMenu.add(lSubMenu);
		
		return outMenu;
	}
	

}

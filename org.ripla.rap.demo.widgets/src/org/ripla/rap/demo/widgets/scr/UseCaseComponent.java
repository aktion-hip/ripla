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
package org.ripla.rap.demo.widgets.scr;

import org.ripla.interfaces.IControllerSet;
import org.ripla.interfaces.IMenuItem;
import org.ripla.interfaces.IMessages;
import org.ripla.menu.RiplaMenuComposite;
import org.ripla.rap.demo.widgets.Activator;
import org.ripla.rap.demo.widgets.Constants;
import org.ripla.rap.demo.widgets.controllers.ButtonWidgetsController;
import org.ripla.rap.demo.widgets.controllers.FormController;
import org.ripla.rap.demo.widgets.controllers.InputWidgetsController;
import org.ripla.rap.demo.widgets.controllers.SelectionWidgetsController;
import org.ripla.rap.demo.widgets.controllers.TableWidgetsController;
import org.ripla.rap.demo.widgets.controllers.TreeWidgetsController;
import org.ripla.rap.interfaces.IMenuSet;
import org.ripla.rap.services.IUseCase;
import org.ripla.rap.util.UseCaseHelper;

/**
 * This bundle's provider of the <code>IUseCase</code> service.
 * 
 * @author Luthiger
 */
public class UseCaseComponent implements IUseCase {

	@Override
	public Package getControllerClasses() {
		return InputWidgetsController.class.getPackage();
	}

	@Override
	public IMenuSet[] getContextMenus() {
		return new IMenuSet[] { ContextMenuHelper.createContextMenuSet() };
	}

	@Override
	public IMenuItem getMenu() {
		return createMenu();
	}

	@Override
	public IControllerSet getControllerSet() {
		return UseCaseHelper.EMPTY_CONTROLLER_SET;
	}

	// ---

	/**
	 * @return IMenuItem
	 */
	private IMenuItem createMenu() {
		final IMessages lMessages = Activator.getMessages();
		final RiplaMenuComposite outMenu = new RiplaMenuComposite(
				lMessages.getMessage("component.menu.title"), 10); //$NON-NLS-1$
		outMenu.setControllerName(UseCaseHelper
				.createFullyQualifiedControllerName(InputWidgetsController.class));
		outMenu.setPermission(Constants.PERMISSION_INPUT_WIDGETS);

		RiplaMenuComposite lSubMenu = new RiplaMenuComposite(
				lMessages.getMessage("widgets.menu.input"), 10); //$NON-NLS-1$
		lSubMenu.setControllerName(UseCaseHelper
				.createFullyQualifiedControllerName(InputWidgetsController.class));
		outMenu.add(lSubMenu);

		lSubMenu = new RiplaMenuComposite(
				lMessages.getMessage("widgets.menu.button"), 20); //$NON-NLS-1$
		lSubMenu.setControllerName(UseCaseHelper
				.createFullyQualifiedControllerName(ButtonWidgetsController.class));
		outMenu.add(lSubMenu);

		lSubMenu = new RiplaMenuComposite(
				lMessages.getMessage("widgets.menu.selection"), 30); //$NON-NLS-1$
		lSubMenu.setControllerName(UseCaseHelper
				.createFullyQualifiedControllerName(SelectionWidgetsController.class));
		outMenu.add(lSubMenu);

		lSubMenu = new RiplaMenuComposite(
				lMessages.getMessage("widgets.menu.form"), 40); //$NON-NLS-1$
		lSubMenu.setControllerName(UseCaseHelper
				.createFullyQualifiedControllerName(FormController.class));
		outMenu.add(lSubMenu);

		lSubMenu = new RiplaMenuComposite(
				lMessages.getMessage("widgets.menu.table"), 50); //$NON-NLS-1$
		lSubMenu.setControllerName(UseCaseHelper
				.createFullyQualifiedControllerName(TableWidgetsController.class));
		outMenu.add(lSubMenu);

		lSubMenu = new RiplaMenuComposite(
				lMessages.getMessage("widgets.menu.tree"), 60); //$NON-NLS-1$
		lSubMenu.setControllerName(UseCaseHelper
				.createFullyQualifiedControllerName(TreeWidgetsController.class));
		outMenu.add(lSubMenu);

		return outMenu;
	}

}

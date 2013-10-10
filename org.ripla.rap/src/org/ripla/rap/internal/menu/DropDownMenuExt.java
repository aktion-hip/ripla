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

package org.ripla.rap.internal.menu;

import java.util.List;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.osgi.service.useradmin.Authorization;
import org.ripla.interfaces.IMenuCommand;
import org.ripla.interfaces.IMenuItem;
import org.ripla.rap.internal.menu.ExtendibleMenuFactory.IExtMenuItem;
import org.ripla.services.IExtendibleMenuContribution;

/**
 * A composite to display a drop down menu for an extendible menu.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class DropDownMenuExt extends DropDownMenu {

	/**
	 * @param inParent
	 * @param inItemCreator
	 */
	public DropDownMenuExt(final Composite inParent,
			final IMenuItemsCreator inItemCreator) {
		super(inParent, inItemCreator);
	}

	public static IMenuItemsCreator getItemsCreator(
			final IMenuItem inMenuConfig,
			final List<IExtMenuItem> inContributions,
			final SelectionListener inListener,
			final Authorization inAuthorization) {
		return new MenuItemsCreatorExt(inMenuConfig, inContributions,
				inListener, inAuthorization);
	}

	// ---

	protected static class MenuItemsCreatorExt extends MenuItemsCreator
			implements IMenuItemsCreator {
		private final IMenuItem menuConfig;
		private final List<IExtMenuItem> contributions;
		private final Authorization authorization;

		protected MenuItemsCreatorExt(final IMenuItem inMenuConfig,
				final List<IExtMenuItem> inContributions,
				final SelectionListener inListener,
				final Authorization inAuthorization) {
			super(inMenuConfig, inListener, inAuthorization);
			menuConfig = inMenuConfig;
			contributions = inContributions;
			authorization = inAuthorization;
		}

		@Override
		public IMenuCommand create(final Menu inMenu) {
			IMenuCommand outCmd = null;
			for (final IExtMenuItem lItemConfig : contributions) {
				if (lItemConfig.isMarker()) {
					continue;
				}

				final IExtendibleMenuContribution lContribution = lItemConfig
						.getContribution();
				if (!MenuFactory.checkPermissions(
						lContribution.getPermission(), authorization)) {
					continue;
				}

				final List<IMenuItem> lSubMenuItems = lContribution
						.getSubMenu();
				final int lStyle = lSubMenuItems.isEmpty() ? SWT.PUSH
						: SWT.CASCADE;
				final MenuItem lItem = new MenuItem(inMenu, lStyle | SWT.LEFT);
				lItem.setText(lContribution.getLabel());
				lItem.setData(RWT.CUSTOM_VARIANT, "ripla-menu-item");
				lItem.addSelectionListener(getListener());
				lItem.setData(MenuFactory.KEY_MENU_ACTION,
						createMenuCommand(lContribution.getControllerName()));
				if (outCmd == null) {
					outCmd = createMenuCommand(lContribution
							.getControllerName());
				}

				// create nested sub menu
				if (!lSubMenuItems.isEmpty()) {
					final Menu lSubMenu = new Menu(inMenu);
					lItem.setMenu(lSubMenu);
					for (final IMenuItem lSubItem : lSubMenuItems) {
						createMenuItem(lSubMenu, lSubItem, authorization);
					}
				}
			}
			return outCmd;
		}

		@Override
		public IMenuItem getMenuConfig() {
			return menuConfig;
		}
	}

}

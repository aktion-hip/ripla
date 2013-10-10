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
import java.util.Map;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.osgi.service.useradmin.Authorization;
import org.ripla.interfaces.IMenuCommand;
import org.ripla.interfaces.IMenuItem;

/**
 * Helper class for creating the main menu's pulldown menu entries.
 * 
 * @author Luthiger
 */
public class MenuFactory implements Comparable<MenuFactory> {
	public static final String KEY_MENU_ACTION = "ripla.menu.action";

	private final transient IMenuItem menu;

	/**
	 * MenuFactory constructor
	 * 
	 * @param inMenu
	 *            {@link IVIFMenuItem} the main menu item to process.
	 */
	public MenuFactory(final IMenuItem inMenu) {
		menu = inMenu;
	}

	/**
	 * Creates the menu for the use case and adds it to the specified menu bar.
	 * 
	 * @param inMenuBar
	 *            {@link Composite} the composite that will house the menus
	 * @param inIcon
	 *            {@link Image} the resource for the icon to indicate a submenu,
	 *            may be <code>null</code>
	 * @param inListener
	 *            {@link SelectionListener} the listener for the RAP menu item
	 *            selection
	 * @param inAuthorization
	 *            {@link Authorization} the authorization object
	 * @return {@link DropDownMenu} the created menu or <code>null</code>, if
	 *         the user doesn't have sufficient permissions
	 */
	public DropDownMenu createMenu(final Composite inMenuBar,
			final Image inIcon, final SelectionListener inListener,
			final Authorization inAuthorization) {
		if (checkPermissions(menu.getPermission(), inAuthorization)) {
			return new DropDownMenu(inMenuBar, DropDownMenu.getItemsCreator(
					menu, inListener, inAuthorization));
		}
		return null;
	}

	/**
	 * Check permission.
	 * 
	 * @param inMenuPermission
	 *            String the permission, may be an empty string
	 * @param inAuthorization
	 *            {@link Authorization} the parameter object containing the
	 *            permissions the user has
	 * @return boolean <code>true</code> if the user has sufficient permissions
	 *         for the command handled by the menu item
	 */
	public static boolean checkPermissions(final String inMenuPermission,
			final Authorization inAuthorization) {
		if (inAuthorization == null) {
			return true;
		}
		if (inMenuPermission.length() > 0
				&& !inAuthorization.hasRole(inMenuPermission)) {
			return false;
		}
		return true;
	}

	protected void addCommand(final Map<Integer, IMenuCommand> inMap,
			final MenuItem inItem, final IMenuItem inMenuItem) {
		addCommand(inMap, inItem,
				createMenuCommand(inMenuItem.getControllerName()));
	}

	protected void addCommand(final Map<Integer, IMenuCommand> inMap,
			final MenuItem inItem, final IMenuCommand inCommand) {
		if (inCommand != null) {
			inMap.put(inItem.getID(), inCommand);
		}
	}

	protected IMenuCommand createMenuCommand(final String inControllerName) {
		return new IMenuCommand() {
			@Override
			public String getControllerName() {
				return inControllerName;
			}
		};
	}

	/**
	 * Recurse structure to create the menu's pulldown entries.
	 * 
	 * @param inSubItems
	 * @param inParent
	 * @param inMap
	 * @param inAuthorization
	 * @param inCommand
	 */
	protected void createSubMenu(final List<IMenuItem> inSubItems,
			final Menu inParent, final Map<Integer, IMenuCommand> inMap,
			final SelectionListener inCommand,
			final Authorization inAuthorization) {
		for (final IMenuItem lItem : inSubItems) {
			if (!checkPermissions(lItem.getPermission(), inAuthorization)) {
				continue;
			}

			final List<IMenuItem> lSubMenuItems = lItem.getSubMenu();
			final int lStyle = lSubMenuItems.isEmpty() ? SWT.PUSH : SWT.CASCADE;
			final MenuItem lMenuItem = new MenuItem(inParent, lStyle | SWT.LEFT);
			lMenuItem.setText(lItem.getLabel());
			lMenuItem.addSelectionListener(inCommand);
			lMenuItem.setData(RWT.CUSTOM_VARIANT, "ripla-menu-item");
			addCommand(inMap, lMenuItem, lItem);

			if (!lSubMenuItems.isEmpty()) {
				final Menu lSubMenu = new Menu(inParent);
				lMenuItem.setMenu(lSubMenu);
				createSubMenu(lSubMenuItems, lSubMenu, inMap, inCommand,
						inAuthorization);
			}
		}
	}

	/**
	 * This menu's position compared to other menu entry's position. Higher
	 * position values are displayed more to the right.
	 * 
	 * @return int the position
	 */
	public int getPosition() {
		return menu.getPosition();
	}

	@Override
	public int compareTo(final MenuFactory inCompare) {
		return getPosition() - inCompare.getPosition();
	}

	/**
	 * @return String the symbolic name of the bundle providing this menu
	 */
	public String getProviderSymbolicName() {
		return menu.getControllerName().split("/")[0]; //$NON-NLS-1$
	}

}

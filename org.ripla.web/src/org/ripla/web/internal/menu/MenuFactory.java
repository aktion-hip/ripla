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
package org.ripla.web.internal.menu;

import java.util.List;
import java.util.Map;

import org.osgi.service.useradmin.Authorization;
import org.ripla.web.interfaces.IMenuCommand;
import org.ripla.web.interfaces.IMenuItem;

import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

/**
 * Helper class for creating the main menu's pulldown menu entries.
 * 
 * @author Luthiger
 */
public class MenuFactory implements Comparable<MenuFactory> {
	
	private IMenuItem menu;

	/**
	 * Constructor
	 * 
	 * @param inMenu {@link IVIFMenuItem} the main menu item to process.
	 */
	public MenuFactory(IMenuItem inMenu) {
		menu = inMenu;
	}

	/**
	 * Creates the menu for the use case and adds it to the specified menu bar.
	 * 
	 * @param inMenuBar {@link MenuBar}
	 * @param inMap Map<Integer, IMenuCommand> the map to register the menu's command
	 * @param inCommand {@link Command} the Vaadin menu bar command
	 * @param inAuthorization {@link Authorization} the authorization object 
	 * @return MenuItem the Vaadin menu item
	 */
	public MenuItem createMenu(MenuBar inMenuBar, Map<Integer, IMenuCommand> inMap, Command inCommand, Authorization inAuthorization) {
		if (!checkPermissions(menu.getPermission(), inAuthorization)) return null;
		
		MenuItem outItem = inMenuBar.addItem(menu.getLabel(), null, inCommand);
		outItem.setStyleName("ripla-menu-item"); //$NON-NLS-1$
		addCommand(inMap, outItem, menu);
		
		List<IMenuItem> lSubMenu = menu.getSubMenu();
		if (!lSubMenu.isEmpty()) {
			createSubMenu(lSubMenu, outItem, inMap, inCommand, inAuthorization);
		}
		
		return outItem;
	}

	/**
	 * Check permission.
	 * 
	 * @param inMenuPermission String the permission, may be an empty string
	 * @param inAuthorization {@link Authorization} the parameter object containing the permissions the user has
	 * @return boolean <code>true</code> if the user has sufficient permissions for the command handled by the menu item
	 */
	protected boolean checkPermissions(String inMenuPermission, Authorization inAuthorization) {
		if (inAuthorization == null) return true;
		if (inMenuPermission.length() > 0 && !inAuthorization.hasRole(inMenuPermission)) return false;
		return true;
	}

	protected void addCommand(Map<Integer, IMenuCommand> inMap, MenuItem inItem, IMenuItem inMenuItem) {
		addCommand(inMap, inItem, createMenuCommand(inMenuItem.getControllerName()));
	}
	
	protected void addCommand(Map<Integer, IMenuCommand> inMap, MenuItem inItem, IMenuCommand inCommand) {
		if (inCommand != null) {			
			inMap.put(inItem.getId(), inCommand);
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
	 * @param inItem
	 * @param inMap
	 * @param inAuthorization 
	 * @param inComman
	 */
	protected void createSubMenu(List<IMenuItem> inSubItems, MenuItem inItem, Map<Integer, IMenuCommand> inMap, Command inCommand, Authorization inAuthorization) {
		for (IMenuItem lItem : inSubItems) {
			if (!checkPermissions(lItem.getPermission(), inAuthorization)) {
				continue;
			}
			MenuItem lMenuItem = inItem.addItem(lItem.getLabel(), null, inCommand);
			lMenuItem.setStyleName("ripla-menu-item");
			addCommand(inMap, lMenuItem, lItem);
			List<IMenuItem> lSubMenu = lItem.getSubMenu();
			if (!lSubMenu.isEmpty()) {
				createSubMenu(lSubMenu, lMenuItem, inMap, inCommand, inAuthorization);
			}
		}
	}
	
	/**
	 * This menu's position compared to other menu entry's position.
	 * Higher position values are displayed more to the right. 
	 * 
	 * @return int the position
	 */
	public int getPosition() {
		return menu.getPosition();
	}

	@Override
	public int compareTo(MenuFactory inCompare) {
		return getPosition() - inCompare.getPosition();
	}
	
	/**
	 * @return String the symbolic name of the bundle providing this menu
	 */
	public String getProviderSymbolicName() {
		return menu.getControllerName().split("/")[0]; //$NON-NLS-1$
	}

}

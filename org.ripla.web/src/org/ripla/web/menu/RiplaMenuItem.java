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
package org.ripla.web.menu;

import java.util.Collections;
import java.util.List;

import org.ripla.web.interfaces.IMenuCommand;
import org.ripla.web.interfaces.IMenuItem;

/**
 * A menu item implementing the <code>IMenuItem</code> interface.<br />
 * This item implementation doesn't contain sub menus.
 *
 * @author Luthiger
 * @see IMenuItem
 */
public class RiplaMenuItem implements IMenuItem {
	private String label;
	private int position;
	private String controllerName;
	private String permission;

	/**
	 * Constructor
	 * 
	 * @param inLabel String the label displayed on the menu
	 */
	public RiplaMenuItem(String inLabel) {
		this(inLabel, 0);
	}
	
	/**
	 * Constructor
	 * 
	 * @param inLabel String the label displayed on the menu
	 * @param inPosition int the menu's position
	 */
	public RiplaMenuItem(String inLabel, int inPosition) {
		label = inLabel;
		position = inPosition;
	}
	
	/**
	 * @return String caption in Vaadin menu
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return int position in Vaadin menu bar.
	 */
	public int getPosition() {
		return position;
	}
	
	/**
	 * Sets the fully qualified name of the controller to be executed when the menu item is clicked.<br />
	 * Use <pre>UseCaseHelper.createFullyQualifiedControllerName(MyController.class)</pre> for a consistent naming.
	 * 
	 * @param inControllerName String
	 */
	public void setControllerName(String inControllerName) {
		controllerName = inControllerName;
	}
	
	@Override
	public String getControllerName() {
		return controllerName;
	}

	/*
	 * (non-Javadoc)
	 * @see org.ripla.web.interfaces.IMenuCommand#getMenuCommand()
	 */
	public IMenuCommand getMenuCommand() {
		return new IMenuCommand() {
			@Override
			public String getControllerName() {
				return controllerName;
			}
		};
	}
	
	/**
	 * Sets the permission the user needs for that the menu item becomes visible (and selectable).
	 * 
	 * @param inPermission String
	 */
	public void setPermission(String inPermission) {
		permission = inPermission;
	}
	
	@Override
	public String getPermission() {
		return permission == null ? "" : permission; //$NON-NLS-1$
	}

	@Override
	public List<IMenuItem> getSubMenu() {
		return Collections.emptyList();
	}

}

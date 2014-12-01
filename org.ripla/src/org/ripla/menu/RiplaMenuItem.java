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
package org.ripla.menu;

import java.util.Collections;
import java.util.List;

import org.ripla.interfaces.IMenuCommand;
import org.ripla.interfaces.IMenuItem;

/**
 * A menu item implementing the <code>IMenuItem</code> interface.<br />
 * This item implementation doesn't contain sub menus.
 * 
 * @author Luthiger
 * @see IMenuItem
 */
public class RiplaMenuItem implements IMenuItem {
	private transient String label;
	private transient int position;
	private String controllerName;
	private String permission;
	private String menuTag;

	/**
	 * Constructor
	 * 
	 * @param inLabel
	 *            String the label displayed on the menu
	 */
	public RiplaMenuItem(final String inLabel) {
		this(inLabel, 0);
	}

	/**
	 * Constructor
	 * 
	 * @param inLabel
	 *            String the label displayed on the menu
	 * @param inPosition
	 *            int the menu's position
	 */
	public RiplaMenuItem(final String inLabel, final int inPosition) {
		label = inLabel;
		position = inPosition;
	}

	/**
	 * @return String caption in menu
	 */
	@Override
	public String getLabel() {
		return label;
	}

	/**
	 * @return int position in menu bar.
	 */
	@Override
	public int getPosition() {
		return position;
	}

	@Override
	public String getTag() {
		return menuTag;
	}

	/**
	 * Sets the tag for menu filtering.
	 * 
	 * @param inTag
	 *            String the menu's tag
	 */
	public void setTag(String inTag) {
		menuTag = inTag;
	}

	/**
	 * Sets the fully qualified name of the controller to be executed when the
	 * menu item is clicked.<br />
	 * Use
	 * 
	 * <pre>
	 * UseCaseHelper.createFullyQualifiedControllerName(MyController.class)
	 * </pre>
	 * 
	 * for a consistent naming.
	 * 
	 * @param inControllerName
	 *            String
	 */
	public void setControllerName(final String inControllerName) {
		controllerName = inControllerName;
	}

	@Override
	public String getControllerName() {
		return controllerName;
	}

	/**
	 * @return {@link IMenuCommand}
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
	 * Sets the permission the user needs for that the menu item becomes visible
	 * (and selectable).
	 * 
	 * @param inPermission
	 *            String
	 */
	public void setPermission(final String inPermission) {
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

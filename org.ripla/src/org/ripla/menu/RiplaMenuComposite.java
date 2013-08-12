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

import java.util.ArrayList;
import java.util.List;

import org.ripla.interfaces.IMenuItem;

/**
 * A menu item implementation containing sub menus.
 * 
 * @author Luthiger
 */
public class RiplaMenuComposite extends RiplaMenuItem implements IMenuItem {
	private final transient List<IMenuItem> subMenu = new ArrayList<IMenuItem>();

	/**
	 * RiplaMenuComposite constructor.
	 * 
	 * @param inLabel
	 *            String the label displayed on the menu
	 */
	public RiplaMenuComposite(final String inLabel) {
		super(inLabel);
	}

	/**
	 * RiplaMenuComposite constructor.
	 * 
	 * @param inLabel
	 *            String the label displayed on the menu
	 * @param inPosition
	 *            int the menu's position
	 */
	public RiplaMenuComposite(final String inLabel, final int inPosition) {
		super(inLabel, inPosition);
	}

	/**
	 * Adding the sub menu.
	 * 
	 * @param inSubMenu
	 *            {@link RiplaMenuComposite}
	 */
	public void add(final RiplaMenuComposite inSubMenu) {
		subMenu.add(inSubMenu);
	}

	/**
	 * Remove a menu item.
	 * 
	 * @param inSubMenu
	 *            {@link RiplaMenuItem}
	 */
	public void remove(final RiplaMenuItem inSubMenu) {
		subMenu.remove(inSubMenu);
	}

	/**
	 * Returns the sub menu, i.e. a list of <code>IMenuItem</code>s.
	 * 
	 * @return List of {@link IMenuItem}
	 */
	@Override
	public List<IMenuItem> getSubMenu() {
		return subMenu;
	}

}

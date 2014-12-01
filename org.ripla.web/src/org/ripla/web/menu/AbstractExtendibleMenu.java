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

package org.ripla.web.menu;

import java.util.Collections;
import java.util.List;

import org.ripla.interfaces.IMenuExtendible;
import org.ripla.interfaces.IMenuItem;

/**
 * Base class for the implementations of <code>IMenuExtendible</code> provided
 * by bundles.
 * 
 * @author Luthiger
 */
public abstract class AbstractExtendibleMenu implements IMenuExtendible {
	private String menuTag;

	/**
	 * This implementation returns an empty list (i.e.
	 * <code>Collections.emptyList()</code>). The sub menu is made by menu
	 * contributions in an extendible way.
	 * 
	 * @return List&lt;IMenuItem>
	 */
	@Override
	public List<IMenuItem> getSubMenu() {
		return Collections.emptyList();
	}

	@Override
	public String getControllerName() { // NOPMD by Luthiger on 10.09.12 00:02
		return null;
	}

	@Override
	public String getPermission() { // NOPMD by Luthiger on 10.09.12 00:02
		return ""; //$NON-NLS-1$
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

}

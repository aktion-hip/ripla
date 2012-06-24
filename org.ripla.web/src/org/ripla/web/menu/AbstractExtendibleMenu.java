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

import org.ripla.web.interfaces.IMenuExtendible;
import org.ripla.web.interfaces.IMenuItem;

/**
 * Base class for the implementations of <code>IMenuExtendible</code> provided by bundles.
 * 
 * @author Luthiger
 */
public abstract class AbstractExtendibleMenu implements IMenuExtendible {
	
	/**
	 * This implementation returns an empty list (i.e. <code>Collections.emptyList()</code>).
	 * The sub menu is made by menu contributions in an extendible way.
	 * 
	 * @return List&lt;IMenuItem>
	 */
	@Override
	public List<IMenuItem> getSubMenu() {
		return Collections.emptyList();
	}

	/* (non-Javadoc)
	 * @see org.ripla.web.interfaces.IMenuElement#getControllerName()
	 */
	@Override
	public String getControllerName() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.ripla.web.interfaces.IMenuElement#getPermission()
	 */
	@Override
	public String getPermission() {
		return ""; //$NON-NLS-1$
	}

}

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

package org.ripla.web.interfaces;

import org.ripla.web.menu.ExtendibleMenuMarker;

/**
 * Interface for Ripla menu variants that are extendible.<br />
 * Bundles can provide menu contributions to extendible menus.
 * 
 * @author Luthiger
 */
public interface IMenuExtendible extends IMenuItem {
	
	/**
	 * @return String the extendible menu's ID. 
	 * Must be unique for that bundles that want to make contributions to this menu can look it up.
	 */
	String getMenuID();
		
	/**
	 * @return {@link ExtendibleMenuMarker}[] the array of markers this menu defines to which a contribution
	 * can be attached.
	 */
	ExtendibleMenuMarker[] getMarkers();

}

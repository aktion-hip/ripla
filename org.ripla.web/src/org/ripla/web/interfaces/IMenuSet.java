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

/**
 * Interface for a specific context menu, i.e. a context menu identified by a set ID and made up of a set of menu items.
 * 
 * @author Luthiger
 */
public interface IMenuSet {
	
	/**
	 * @return String the menu set's ID. Must be unique within a bundle.
	 */
	String getSetID();

	/**
	 * @return {@link IContextMenuItem}[] the set of context menu configurations that make up the specific context menu.
	 */
	IContextMenuItem[] getContextMenuItems();
	
}

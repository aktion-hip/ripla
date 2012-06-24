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

import java.util.List;

/**
 * Interface for elements in the Ripla menu.<br />
 * This interface defines a composite, i.e. the menu element can contain sub menu elements of the same structure.
 * 
 * @author Luthiger
 */
public interface IMenuElement {
	
	/**
	 * @return String caption in Vaadin menu
	 */
	String getLabel();
	
	/**
	 * Returns the fully qualified name of the controller to be executed when the menu item is clicked.<br />
	 * Use <pre>UseCaseHelper.createFullyQualifiedControllerName(MyController.class)</pre> for a consistent naming.
	 * 
	 * @return String the controller's fully qualified name, i.e. <code>org.ripla.mybundle/mycontroller</code>, may be <code>null</code>.
	 * @see UseCaseHelper#createFullyQualifiedTaskName(Class)
	 */
	String getControllerName();

	/**
	 * @return List<IMenuItem> the menu item's sub menu. May be <code>Collections.emptyList()</code>.
	 */
	List<IMenuItem> getSubMenu();
	
	/**
	 * Returns the permission the user (i.e. role) needs to have for that he sees the contribution visible and selectable.
	 * 
	 * @return String the menu permission or an empty string for no permission needed
	 */
	String getPermission();

}

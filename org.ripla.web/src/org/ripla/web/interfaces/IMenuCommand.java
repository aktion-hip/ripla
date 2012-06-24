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
 * Interface for a command triggered when a menu item is clicked.
 *
 * @author Luthiger
 */
public interface IMenuCommand {	
	
	/**
	 * Returns the controller's fully qualified name.
	 * 
	 * @return String the controller's fully qualified name, i.e. <code>org.ripla.mybundle/mycontroller</code>
	 */
	String getControllerName();

}

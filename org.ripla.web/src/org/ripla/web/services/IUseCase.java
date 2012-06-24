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

package org.ripla.web.services;

import org.ripla.web.interfaces.IControllerSet;
import org.ripla.web.interfaces.IMenuItem;
import org.ripla.web.interfaces.IMenuSet;

/**
 * Interface for Ripla use cases.
 * 
 * @author Luthiger
 */
public interface IUseCase {

	/**
	 * @return {@link IMenuItem} the contribution to the menu
	 */
	IMenuItem getMenu();
	
	/**
	 * <p>This method tells the application which package within the bundle contributes this bundle's 
	 * controller classes to the application.</p>
	 * <p>For that the controller classes within the specified package can be looked up and registered,
	 * they have to implement the <code>IPluggable</code> interface. 
	 * In addition, they have to be annotated by <code>@UseCaseController</code>.</p> 
	 * <p>This method can be used in combination with <code>IUseCase.getControllerSet()</code>.</p>
	 * 
	 * @return {@link Package} the package within the bundle that provides the controller classes
	 */	
	Package getControllerClasses();
	
	/**
	 * <p>This method contributes this bundle's set of controllers to the application.</p>
	 * <p>This method can be used in combination with <code>IUseCase.getControllerClasses()</code>.</p>
	 * <p>Note: The controller classes provided by this method are 'hard wired', therefore, they don't have to be
	 * annotated by <code>@UseCaseController</code>.</p> 
	 * 
	 * @return {@link IControllerSet} the set of controllers defined in the bundle
	 */
	IControllerSet getControllerSet();
	
	/**
	 * @return the set of context menu items defined in the bundle
	 */
	IMenuSet[] getContextMenus();
	
}

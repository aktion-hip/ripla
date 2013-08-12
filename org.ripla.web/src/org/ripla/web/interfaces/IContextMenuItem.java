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
package org.ripla.web.interfaces;

import org.osgi.service.useradmin.Authorization;
import org.osgi.service.useradmin.User;
import org.ripla.util.ParameterObject;

/**
 * Interface for an item in the context menu.<br />
 * The context menu is the menu displayed in the sidebar panel.
 * 
 * @author Luthiger
 */
public interface IContextMenuItem {

	/**
	 * @return Class&lt;? extends IPluggable> the controller that has to be
	 *         called when the user clicks the context menu item.
	 */
	Class<? extends IPluggable> getControllerClass();

	/**
	 * @return String the message id for the localized message to display in the
	 *         context menu.
	 */
	String getTitleMsg();

	/**
	 * @return String The permission a user needs to have for that the task is
	 *         displayed in the context menu. Empty string of no permission
	 *         needed.
	 */
	String getMenuPermission();

	/**
	 * Check the conditions to display the context menu item.
	 * 
	 * @param inUser
	 *            {@link User} the user instance, might be evaluated to check
	 *            the conditions
	 * @param inAuthorization
	 *            {@link Authorization} the authorization instance, will be
	 *            evaluate to check the conditions
	 * @param inParameters
	 *            {@link ParameterObject} the generic parameter object with
	 *            parameters that could be evaluated to check the conditions
	 * @return boolean <code>true</code> if the conditions allow to
	 *         display/enable the context menu item, <code>false</code> if not
	 */
	boolean checkConditions(User inUser, Authorization inAuthorization,
			ParameterObject inParameters);

}

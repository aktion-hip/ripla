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
package org.ripla.services;

/**
 * <p>
 * A permission entry is a definition of an action group in the OSGi user admin
 * wording.
 * </p>
 * <p>
 * Bundles can implement this interface, thus signaling that they want to
 * protect the functionality they provide. The service consumer has to create an
 * action group instance and add the specified groups as member of this new
 * group instance.
 * </p>
 * 
 * @author Luthiger
 */
public interface IPermissionEntry {

	/**
	 * @return String the name of the permission (i.e. action group)
	 */
	String getPermissionName();

	/**
	 * @return String additional description of the permission
	 */
	String getPermissionDescription();

	/**
	 * @return String[] set of required members of the group created with this
	 *         permission name. If an action needs this permission, a user must
	 *         have one of the specified roles (i.e. has to be member of all of
	 *         those groups) for that he is authorized for this action.
	 */
	String[] getRequieredMemberNames();

	/**
	 * @return String[] set of basic members of the group created with this
	 *         permission name. If an action needs this permission, a user must
	 *         have one of the specified roles (i.e. has to be member of one of
	 *         those groups) for that he is authorized for this action.
	 */
	String[] getMemberNames();

}

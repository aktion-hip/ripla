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

import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;
import org.ripla.web.exceptions.LoginException;

/**
 * Interface for classes to authenticate users using the OSGi UserAmin.
 * 
 * @author Luthiger
 */
public interface IAuthenticator {
	
	/**
	 * Checks the user's credentials and returns the authenticated user from the store.
	 * 
	 * @param inName String the user name
	 * @param inPassword String the user's password
	 * @param inUserAdmin {@link UserAdmin} the user admin instance to be used to check the user's credentials
	 * @return {@link User} or <code>null</code>, if no user with the specified combination of user name and credentials could be found
	 * @throws LoginException
	 */
	User authenticate(String inName, String inPassword, UserAdmin inUserAdmin) throws LoginException;

}

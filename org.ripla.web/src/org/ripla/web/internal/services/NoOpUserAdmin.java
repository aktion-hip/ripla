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

package org.ripla.web.internal.services;

import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.useradmin.Authorization;
import org.osgi.service.useradmin.Role;
import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;

/**
 * A user admin instance doin nothing.
 * 
 * @author Luthiger
 */
public class NoOpUserAdmin implements UserAdmin {

	/* (non-Javadoc)
	 * @see org.osgi.service.useradmin.UserAdmin#createRole(java.lang.String, int)
	 */
	@Override
	public Role createRole(String inName, int inType) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.useradmin.UserAdmin#removeRole(java.lang.String)
	 */
	@Override
	public boolean removeRole(String inName) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.useradmin.UserAdmin#getRole(java.lang.String)
	 */
	@Override
	public Role getRole(String inName) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.useradmin.UserAdmin#getRoles(java.lang.String)
	 */
	@Override
	public Role[] getRoles(String inFilter) throws InvalidSyntaxException {
		return inFilter == null ? new Role[] {} : null;
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.useradmin.UserAdmin#getUser(java.lang.String, java.lang.String)
	 */
	@Override
	public User getUser(String inKey, String inValue) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.useradmin.UserAdmin#getAuthorization(org.osgi.service.useradmin.User)
	 */
	@Override
	public Authorization getAuthorization(User inUser) {
		return new NoOpAuthorization();
	}
	
// ---
	
	private static class NoOpAuthorization implements Authorization {
		/* (non-Javadoc)
		 * @see org.osgi.service.useradmin.Authorization#getName()
		 */
		@Override
		public String getName() {
			return null;
		}
		/* (non-Javadoc)
		 * @see org.osgi.service.useradmin.Authorization#hasRole(java.lang.String)
		 */
		@Override
		public boolean hasRole(String inName) {
			return false;
		}	
		/* (non-Javadoc)
		 * @see org.osgi.service.useradmin.Authorization#getRoles()
		 */
		@Override
		public String[] getRoles() {
			return null;
		}
	}

}

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
package org.ripla.rap.internal.services;

import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.useradmin.Authorization;
import org.osgi.service.useradmin.Role;
import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;

/**
 * A user admin instance doing nothing.
 * 
 * @author Luthiger
 */
public class NoOpUserAdmin implements UserAdmin {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.service.useradmin.UserAdmin#createRole(java.lang.String,
	 * int)
	 */
	@Override
	public Role createRole(final String inName, final int inType) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.service.useradmin.UserAdmin#removeRole(java.lang.String)
	 */
	@Override
	public boolean removeRole(final String inName) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.service.useradmin.UserAdmin#getRole(java.lang.String)
	 */
	@Override
	public Role getRole(final String inName) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.service.useradmin.UserAdmin#getRoles(java.lang.String)
	 */
	@Override
	public Role[] getRoles(final String inFilter) throws InvalidSyntaxException {
		return inFilter == null ? new Role[] {} : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.service.useradmin.UserAdmin#getUser(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public User getUser(final String inKey, final String inValue) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.service.useradmin.UserAdmin#getAuthorization(org.osgi.service
	 * .useradmin.User)
	 */
	@Override
	public Authorization getAuthorization(final User inUser) {
		return new NoOpAuthorization();
	}

	// ---

	private static class NoOpAuthorization implements Authorization {
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.osgi.service.useradmin.Authorization#getName()
		 */
		@Override
		public String getName() {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.osgi.service.useradmin.Authorization#hasRole(java.lang.String)
		 */
		@Override
		public boolean hasRole(final String inName) {
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.osgi.service.useradmin.Authorization#getRoles()
		 */
		@Override
		public String[] getRoles() {
			return new String[] {};
		}
	}

}

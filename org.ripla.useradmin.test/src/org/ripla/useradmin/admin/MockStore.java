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

package org.ripla.useradmin.admin;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.useradmin.Group;
import org.osgi.service.useradmin.Role;
import org.osgi.service.useradmin.UserAdmin;
import org.ripla.useradmin.interfaces.IUserAdminStore;

/**
 * @author Luthiger
 */
public class MockStore implements IUserAdminStore { // NOPMD by Luthiger on 08.09.12 23:44

	private final transient UserAdmin userAdmin;

	/**
	 * @param inTestRiplaUserAdmin
	 */
	public MockStore(final UserAdmin inUserAdmin) {
		userAdmin = inUserAdmin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.useradmin.interfaces.IUserAdminStore#initialize()
	 */
	@Override
	public void initialize() throws BackingStoreException {
		((RiplaUserAdmin) userAdmin).createRole(Role.USER_ANYONE, Role.ROLE,
				false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ripla.useradmin.interfaces.IUserAdminStore#addRole(org.osgi.service
	 * .useradmin.Role)
	 */
	@Override
	public void addRole(final Role inRole) throws BackingStoreException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ripla.useradmin.interfaces.IUserAdminStore#removeRole(org.osgi.service
	 * .useradmin.Role)
	 */
	@Override
	public void removeRole(final Role inRole) throws BackingStoreException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ripla.useradmin.interfaces.IUserAdminStore#addMember(org.osgi.service
	 * .useradmin.Group, org.osgi.service.useradmin.Role)
	 */
	@Override
	public void addMember(final Group inGroup, final Role inRole)
			throws BackingStoreException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ripla.useradmin.interfaces.IUserAdminStore#addRequiredMember(org.
	 * osgi.service.useradmin.Group, org.osgi.service.useradmin.Role)
	 */
	@Override
	public void addRequiredMember(final Group inGroup, final Role inRole)
			throws BackingStoreException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ripla.useradmin.interfaces.IUserAdminStore#removeMember(org.osgi.
	 * service.useradmin.Group, org.osgi.service.useradmin.Role)
	 */
	@Override
	public void removeMember(final Group inGroup, final Role inRole)
			throws BackingStoreException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ripla.useradmin.interfaces.IUserAdminStore#addCredential(org.osgi
	 * .service.useradmin.Role, java.lang.String, java.lang.Object)
	 */
	@Override
	public void addCredential(final Role inRole, final String inKey,
			final Object inValue) throws BackingStoreException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ripla.useradmin.interfaces.IUserAdminStore#removeCredential(org.osgi
	 * .service.useradmin.Role, java.lang.String)
	 */
	@Override
	public void removeCredential(final Role inRole, final String inKey)
			throws BackingStoreException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ripla.useradmin.interfaces.IUserAdminStore#clearCredentials(org.osgi
	 * .service.useradmin.Role)
	 */
	@Override
	public void clearCredentials(final Role inRole)
			throws BackingStoreException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ripla.useradmin.interfaces.IUserAdminStore#addProperty(org.osgi.service
	 * .useradmin.Role, java.lang.String, java.lang.Object)
	 */
	@Override
	public void addProperty(final Role inRole, final String inKey,
			final Object inValue) throws BackingStoreException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ripla.useradmin.interfaces.IUserAdminStore#removeProperty(org.osgi
	 * .service.useradmin.Role, java.lang.String)
	 */
	@Override
	public void removeProperty(final Role inRole, final String inKey)
			throws BackingStoreException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ripla.useradmin.interfaces.IUserAdminStore#clearProperties(org.osgi
	 * .service.useradmin.Role)
	 */
	@Override
	public void clearProperties(final Role inRole) throws BackingStoreException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.useradmin.interfaces.IUserAdminStore#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}

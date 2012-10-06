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

import org.osgi.service.useradmin.Group;
import org.osgi.service.useradmin.Role;
import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;
import org.ripla.useradmin.interfaces.IUserAdminStore;
import org.ripla.useradmin.internal.UserAdminEventProducer;

/**
 * Mock object for user admin.
 * 
 * @author Luthiger
 */
public class TestRiplaUserAdmin extends RiplaUserAdmin {

	private final transient MockEventProducer eventProducer;

	public TestRiplaUserAdmin() throws Exception { // NOPMD by Luthiger
		super();
		eventProducer = new MockEventProducer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.useradmin.admin.RiplaUserAdmin#createUserAdminStore()
	 */
	@Override
	protected IUserAdminStore createUserAdminStore() {
		return new MockStore(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.useradmin.admin.RiplaUserAdmin#getEventProducer()
	 */
	@Override
	public UserAdminEventProducer getEventProducer() {
		return eventProducer;
	}

	/**
	 * Convenience method: creates the user admin instance.
	 * 
	 * @return {@link UserAdmin}
	 * @throws Exception
	 */
	public static UserAdmin createUserAdmin() throws Exception { // NOPMD
		final TestRiplaUserAdmin outUserAdmin = new TestRiplaUserAdmin();
		outUserAdmin.setPreferences(new MockPreferencesService());
		return outUserAdmin;
	}

	/**
	 * Convenience method, creates a user with the specified name.
	 * 
	 * @param inUserName
	 *            String
	 * @return {@link User}
	 * @throws Exception
	 */
	public static User createUser(final String inUserName) throws Exception { // NOPMD
		return (User) createUserAdmin().createRole(inUserName, Role.USER);
	}

	/**
	 * Convenience method, creates a group with the specified name.
	 * 
	 * @param inGroupName
	 *            String
	 * @return {@link User}
	 * @throws Exception
	 */
	public static Group createGroup(final String inGroupName) throws Exception { // NOPMD
		return (Group) createUserAdmin().createRole(inGroupName, Role.GROUP);
	}

}

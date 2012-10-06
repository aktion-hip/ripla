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

package org.ripla.useradmin.admin; // NOPMD by Luthiger on 09.09.12 00:12

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.osgi.service.useradmin.Authorization;
import org.osgi.service.useradmin.Group;
import org.osgi.service.useradmin.Role;
import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;

/**
 * @author Luthiger
 */
public class RiplaUserAdminTest {
	private static final String PROPERTY_KEY_NAME = "test.user.name";

	private transient UserAdmin userAdmin;

	@Before
	public void setUp() throws Exception {
		userAdmin = new TestRiplaUserAdmin();
		((RiplaUserAdmin) userAdmin)
				.setPreferences(new MockPreferencesService());
	}

	/**
	 * Test method for
	 * {@link org.ripla.useradmin.admin.RiplaUserAdmin#createRole(java.lang.String, int)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateRole() throws Exception { // NOPMD
		final String lUserName = "test.user"; // NOPMD
		final String lGroupName = "test.group"; // NOPMD

		User lTestUser = (User) userAdmin.createRole(lUserName, Role.USER);
		assertNotNull("Must not be null", lTestUser); // NOPMD
		assertEquals("Wrong name", lUserName, lTestUser.getName()); // NOPMD

		lTestUser = (User) userAdmin.createRole(lUserName, Role.USER);
		assertNull("Must be null", lTestUser); // NOPMD

		Group lTestGroup = (Group) userAdmin.createRole(lGroupName, Role.GROUP);
		assertNotNull("Must not be null", lTestGroup);
		assertEquals("Wrong name", lGroupName, lTestGroup.getName());

		lTestGroup = (Group) userAdmin.createRole(lGroupName, Role.GROUP);
		assertNull("Must be null", lTestGroup);

		lTestUser = (User) userAdmin.createRole(lUserName, Role.ROLE);
	}

	/**
	 * Test method for
	 * {@link org.ripla.useradmin.admin.RiplaUserAdmin#removeRole(java.lang.String)}
	 * .
	 */
	@Test
	public void testRemoveRole() {
		final String lUserName = "test.user"; // NOPMD
		final String lGroupName = "test.group"; // NOPMD

		final User lTestUser = (User) userAdmin
				.createRole(lUserName, Role.USER);
		final Group lTestGroup = (Group) userAdmin.createRole(lGroupName,
				Role.GROUP);
		lTestGroup.addMember(lTestUser);

		final Role[] lMembers = lTestGroup.getMembers();
		assertEquals("Wrong length", 1, lMembers.length);
		assertEquals("Wrong name", lUserName, lMembers[0].getName());

		assertTrue("Could not remove role", userAdmin.removeRole(lUserName));
		assertNull("Groups should be empty", lTestGroup.getMembers());

		assertTrue("Could not remove role",
				userAdmin.removeRole(Role.USER_ANYONE));

		assertFalse("Must not remove role", userAdmin.removeRole("foo.bar"));
		assertFalse("Must not remove role", userAdmin.removeRole(lUserName));
	}

	/**
	 * Test method for
	 * {@link org.ripla.useradmin.admin.RiplaUserAdmin#getRole(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetRole() {
		final String lUserName = "test.user"; // NOPMD
		final String lGroupName = "test.group"; // NOPMD

		userAdmin.createRole(lUserName, Role.USER);
		userAdmin.createRole(lGroupName, Role.GROUP);

		final User lTestUser = (User) userAdmin.getRole(lUserName);
		assertNotNull("Must not be null", lTestUser);
		assertEquals("Wrong name", lUserName, lTestUser.getName());

		final Group lTestGroup = (Group) userAdmin.getRole(lGroupName);
		assertNotNull("Must not be null", lTestGroup);
		assertEquals("Wrong name", lGroupName, lTestGroup.getName());

		final Role lAnonymous = userAdmin.getRole(Role.USER_ANYONE);
		assertEquals("Wrong name", Role.USER_ANYONE, lAnonymous.getName());

		assertNull("Must be null", userAdmin.getRole("foo.bar"));
	}

	/**
	 * Test method for
	 * {@link org.ripla.useradmin.admin.RiplaUserAdmin#getRoles(java.lang.String)}
	 * .
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetRoles() throws Exception { // NOPMD by Luthiger on
		final String lUserName = "test.user"; // NOPMD
		final String lGroupName = "test.group"; // NOPMD

		Role lRole = userAdmin.createRole(lUserName, Role.USER);
		lRole.getProperties().put(PROPERTY_KEY_NAME, lUserName);

		lRole = userAdmin.createRole(lGroupName, Role.GROUP);
		lRole.getProperties().put(PROPERTY_KEY_NAME, lGroupName);

		final Role[] lRoles = userAdmin.getRoles(null);
		assertEquals("Wrong length", 3, lRoles.length);
		assertContains(lRoles, lUserName);
		assertContains(lRoles, lGroupName);
		assertContains(lRoles, Role.USER_ANYONE);

		// we can't test filtering without OSGi bundle context
		// String lFilter = String.format("(%s=%s)", PROPERTY_KEY_NAME,
		// lUserName);
		// lRoles = userAdmin.getRoles(lFilter);
	}

	private void assertContains(final Role[] inRoles, final String inRoleName) {
		for (final Role lRole : inRoles) {
			if (inRoleName.equals(lRole.getName())) {
				return;
			}
		}
		fail();
	}

	/**
	 * Test method for
	 * {@link org.ripla.useradmin.admin.RiplaUserAdmin#getUser(java.lang.String, java.lang.String)}
	 * .
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetUser() {
		final String lUserName = "test.user"; // NOPMD

		Role lRole = userAdmin.createRole(lUserName, Role.USER);
		lRole.getProperties().put(PROPERTY_KEY_NAME, lUserName);

		final User lUser = userAdmin.getUser(PROPERTY_KEY_NAME, lUserName);
		assertNotNull("Must not be null", lUser);
		assertEquals("Wrong name", lUserName, lUser.getName());

		assertNull("Must be null", userAdmin.getUser(PROPERTY_KEY_NAME, "foo"));

		lRole = userAdmin.createRole("foo", Role.USER);
		lRole.getProperties().put(PROPERTY_KEY_NAME, lUserName);

		assertNull("Must be null",
				userAdmin.getUser(PROPERTY_KEY_NAME, lUserName));
	}

	/**
	 * Test method for
	 * {@link org.ripla.useradmin.admin.RiplaUserAdmin#getAuthorization(org.osgi.service.useradmin.User)}
	 * .
	 */
	@Test
	public void testGetAuthorization() {
		final String lUserName = "test.user"; // NOPMD

		final User lRole = (User) userAdmin.createRole(lUserName, Role.USER);

		Authorization lAuthorization = userAdmin.getAuthorization(lRole);
		assertNotNull("Must not be null", lAuthorization);
		assertEquals("Wrong name", lUserName, lAuthorization.getName());

		lAuthorization = userAdmin.getAuthorization(null);
		assertNotNull("Must not be null", lAuthorization);
		assertNull("Must be null", lAuthorization.getName());
	}

}

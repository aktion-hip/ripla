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
 * 
 * @author Benno
 */
public class RiplaUserAdminTest {
	private static final String PROPERTY_KEY_NAME = "test.user.name";
	
	private UserAdmin userAdmin;

	@Before
	public void setUp() throws Exception {
		userAdmin = new TestRiplaUserAdmin();
		((RiplaUserAdmin)userAdmin).setPreferences(new MockPreferencesService());
	}

	/**
	 * Test method for {@link org.ripla.useradmin.admin.RiplaUserAdmin#createRole(java.lang.String, int)}.
	 * @throws Exception 
	 */
	@Test
	public void testCreateRole() throws Exception {
		String lUserName = "test.user";
		String lGroupName = "test.group";
		
		User lTestUser = (User) userAdmin.createRole(lUserName, Role.USER);
		assertNotNull(lTestUser);
		assertEquals(lUserName, lTestUser.getName());
		
		lTestUser = (User) userAdmin.createRole(lUserName, Role.USER);
		assertNull(lTestUser);
		
		Group lTestGroup = (Group) userAdmin.createRole(lGroupName, Role.GROUP);
		assertNotNull(lTestGroup);
		assertEquals(lGroupName, lTestGroup.getName());
		
		lTestGroup = (Group) userAdmin.createRole(lGroupName, Role.GROUP);
		assertNull(lTestGroup);
		
		try {
			lTestUser = (User) userAdmin.createRole(lUserName, Role.ROLE);
			fail("shouldn't get here");
		}
		catch (IllegalArgumentException exc) {
			//intentionally left empty
		}
	}

	/**
	 * Test method for {@link org.ripla.useradmin.admin.RiplaUserAdmin#removeRole(java.lang.String)}.
	 */
	@Test
	public void testRemoveRole() {
		String lUserName = "test.user";
		String lGroupName = "test.group";
		
		User lTestUser = (User) userAdmin.createRole(lUserName, Role.USER);
		Group lTestGroup = (Group) userAdmin.createRole(lGroupName, Role.GROUP);
		lTestGroup.addMember(lTestUser);
		
		Role[] lMembers = lTestGroup.getMembers();
		assertEquals(1, lMembers.length);
		assertEquals(lUserName, lMembers[0].getName());
		
		assertTrue(userAdmin.removeRole(lUserName));
		assertNull(lTestGroup.getMembers());
		
		assertTrue(userAdmin.removeRole(Role.USER_ANYONE));
		
		assertFalse(userAdmin.removeRole("foo.bar"));
		assertFalse(userAdmin.removeRole(lUserName));
	}

	/**
	 * Test method for {@link org.ripla.useradmin.admin.RiplaUserAdmin#getRole(java.lang.String)}.
	 */
	@Test
	public void testGetRole() {
		String lUserName = "test.user";
		String lGroupName = "test.group";
		
		userAdmin.createRole(lUserName, Role.USER);
		userAdmin.createRole(lGroupName, Role.GROUP);
		
		User lTestUser = (User) userAdmin.getRole(lUserName);
		assertNotNull(lTestUser);
		assertEquals(lUserName, lTestUser.getName());
		
		Group lTestGroup = (Group) userAdmin.getRole(lGroupName);
		assertNotNull(lTestGroup);
		assertEquals(lGroupName, lTestGroup.getName());
		
		Role lAnonymous = userAdmin.getRole(Role.USER_ANYONE);
		assertEquals(Role.USER_ANYONE, lAnonymous.getName());
		
		assertNull(userAdmin.getRole("foo.bar"));
	}

	/**
	 * Test method for {@link org.ripla.useradmin.admin.RiplaUserAdmin#getRoles(java.lang.String)}.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetRoles() throws Exception {
		String lUserName = "test.user";
		String lGroupName = "test.group";
		
		Role lRole = userAdmin.createRole(lUserName, Role.USER);
		lRole.getProperties().put(PROPERTY_KEY_NAME, lUserName);
		
		lRole = userAdmin.createRole(lGroupName, Role.GROUP);
		lRole.getProperties().put(PROPERTY_KEY_NAME, lGroupName);

		Role[] lRoles = userAdmin.getRoles(null);
		assertEquals(3, lRoles.length);
		assertContains(lRoles, lUserName);
		assertContains(lRoles, lGroupName);
		assertContains(lRoles, Role.USER_ANYONE);
		
		//we can't test filtering without OSGi bundle context
//		String lFilter = String.format("(%s=%s)", PROPERTY_KEY_NAME, lUserName);
//		lRoles = userAdmin.getRoles(lFilter);
	}
	
	private void assertContains(Role[] inRoles, String inRoleName) {
		for (Role lRole : inRoles) {
			if (inRoleName.equals(lRole.getName())) {
				return;
			}
		}
		fail();
	}

	/**
	 * Test method for {@link org.ripla.useradmin.admin.RiplaUserAdmin#getUser(java.lang.String, java.lang.String)}.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetUser() {
		String lUserName = "test.user";
		
		Role lRole = userAdmin.createRole(lUserName, Role.USER);
		lRole.getProperties().put(PROPERTY_KEY_NAME, lUserName);
		
		User lUser = userAdmin.getUser(PROPERTY_KEY_NAME, lUserName);
		assertNotNull(lUser);
		assertEquals(lUserName, lUser.getName());
		
		assertNull(userAdmin.getUser(PROPERTY_KEY_NAME, "foo"));
		
		lRole = userAdmin.createRole("foo", Role.USER);
		lRole.getProperties().put(PROPERTY_KEY_NAME, lUserName);
		
		assertNull(userAdmin.getUser(PROPERTY_KEY_NAME, lUserName));		
	}

	/**
	 * Test method for {@link org.ripla.useradmin.admin.RiplaUserAdmin#getAuthorization(org.osgi.service.useradmin.User)}.
	 */
	@Test
	public void testGetAuthorization() {
		String lUserName = "test.user";
		
		User lRole = (User) userAdmin.createRole(lUserName, Role.USER);
		
		Authorization lAuthorization = userAdmin.getAuthorization(lRole);
		assertNotNull(lAuthorization);
		assertEquals(lUserName, lAuthorization.getName());
		
		lAuthorization = userAdmin.getAuthorization(null);
		assertNotNull(lAuthorization);
		assertNull(lAuthorization.getName());
	}	

}

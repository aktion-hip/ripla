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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.osgi.service.useradmin.Authorization;
import org.osgi.service.useradmin.Group;
import org.osgi.service.useradmin.Role;
import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;

/**
 * @author Luthiger
 */
public class RiplaAuthorizationTest {
	private static final String USER_NAME = "test.user";
	private static final String GROUP_NAME = "test.group";

	/**
	 * Test method for
	 * {@link org.ripla.useradmin.admin.RiplaAuthorization#getName()}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetName() throws Exception { // NOPMD by Luthiger on
													// 08.09.12 23:44
		final UserAdmin lUserAdmin = TestRiplaUserAdmin.createUserAdmin();
		final User lUser = (User) lUserAdmin.createRole(USER_NAME, Role.USER);
		final Authorization lAuthorization = lUserAdmin.getAuthorization(lUser);
		assertEquals("Wrong username.", USER_NAME, lAuthorization.getName());
	}

	/**
	 * Test method for
	 * {@link org.ripla.useradmin.admin.RiplaAuthorization#getRoles()}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetRoles() throws Exception { // NOPMD by Luthiger on
													// 08.09.12 23:45
		final UserAdmin lUserAdmin = TestRiplaUserAdmin.createUserAdmin();
		final User lUser = (User) lUserAdmin.createRole(USER_NAME, Role.USER);
		Authorization lAuthorization = lUserAdmin.getAuthorization(lUser);
		String[] lRoles = lAuthorization.getRoles();
		assertEquals("wrong length", 1, lRoles.length); // NOPMD by Luthiger on
														// 08.09.12 23:50
		assertEquals("wrong username", USER_NAME, lRoles[0]);

		final Group lGroup = (Group) lUserAdmin.createRole(GROUP_NAME,
				Role.GROUP);
		lAuthorization = lUserAdmin.getAuthorization(lGroup);
		lRoles = lAuthorization.getRoles();
		assertEquals("wrong length", 1, lRoles.length);
		assertEquals("wrong group name", GROUP_NAME, lRoles[0]);

		lGroup.addMember(lUser);
		lAuthorization = lUserAdmin.getAuthorization(lUser);
		lRoles = lAuthorization.getRoles();
		assertEquals("wrong length", 2, lRoles.length);
		assertContains(lRoles, GROUP_NAME);
		assertContains(lRoles, USER_NAME);

		lAuthorization = lUserAdmin.getAuthorization(lGroup);
		lRoles = lAuthorization.getRoles();
		assertEquals("wrong length", 1, lRoles.length);
		assertEquals("wrong group name", GROUP_NAME, lRoles[0]);
	}

	private void assertContains(final String[] inRoles, final String inRoleName) {
		for (final String lRole : inRoles) {
			if (inRoleName.equals(lRole)) {
				return;
			}
		}
		fail();
	}

	/**
	 * Test method for
	 * {@link org.ripla.useradmin.admin.RiplaAuthorization#hasRole(java.lang.String)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public void testHasRole() throws Exception { // NOPMD by Luthiger on
													// 08.09.12 23:45
		final UserAdmin lUserAdmin = TestRiplaUserAdmin.createUserAdmin();
		final User lUser = (User) lUserAdmin.createRole(USER_NAME, Role.USER);
		Authorization lAuthorization = lUserAdmin.getAuthorization(lUser);
		assertFalse("authorization must have role",
				lAuthorization.hasRole("test.role"));

		// test examples r4.cmpn: 107.8.3, see {@link Group}
		final String lRoleName = "foo"; // NOPMD by Luthiger
		final String lGroupName = "marketing"; // NOPMD by Luthiger
		final String lUserName1 = "alice"; // NOPMD by Luthiger
		final String lUserName2 = "bob"; // NOPMD by Luthiger
		final String lUserName3 = "adam"; // NOPMD by Luthiger

		final User lUser1 = (User) lUserAdmin.createRole(lUserName1, Role.USER);
		final User lUser2 = (User) lUserAdmin.createRole(lUserName2, Role.USER);
		final User lUser3 = (User) lUserAdmin.createRole(lUserName3, Role.USER);
		final Group lMarketing = (Group) lUserAdmin.createRole(lGroupName,
				Role.GROUP);
		final Group lFoo = (Group) lUserAdmin.createRole(lRoleName, Role.GROUP);

		lMarketing.addMember(lUser1);
		lMarketing.addMember(lUser2);
		lMarketing.addMember(lUser3);
		lFoo.addRequiredMember(lMarketing);
		lFoo.addMember(lUser1);
		lFoo.addMember(lUser2);

		lAuthorization = lUserAdmin.getAuthorization(lUser1);
		assertTrue("authorization should have role " + lRoleName, // NOPMD by Luthiger on 08.09.12 23:55
				lAuthorization.hasRole(lRoleName));
		lAuthorization = lUserAdmin.getAuthorization(lUser2);
		assertTrue("authorization should have role " + lRoleName,
				lAuthorization.hasRole(lRoleName));
		lAuthorization = lUserAdmin.getAuthorization(lUser3);
		assertFalse("authorization must not have role " + lRoleName,
				lAuthorization.hasRole(lRoleName));

		lFoo.removeMember(lUser2);
		lAuthorization = lUserAdmin.getAuthorization(lUser2);
		assertFalse("authorization must not have role " + lRoleName,
				lAuthorization.hasRole(lRoleName));
		lFoo.removeMember(lUser1);
		lAuthorization = lUserAdmin.getAuthorization(lUser1);
		assertFalse("authorization must not have role " + lRoleName,
				lAuthorization.hasRole(lRoleName));

		// add anonymous as basic member to foo
		lFoo.addMember(lUserAdmin.getRole(Role.USER_ANYONE));

		lAuthorization = lUserAdmin.getAuthorization(lUser1);
		assertTrue("authorization should have role " + lRoleName,
				lAuthorization.hasRole(lRoleName));
		lAuthorization = lUserAdmin.getAuthorization(lUser2);
		assertTrue("authorization should have role " + lRoleName,
				lAuthorization.hasRole(lRoleName));
		lAuthorization = lUserAdmin.getAuthorization(lUser3);
		assertTrue("authorization should have role " + lRoleName,
				lAuthorization.hasRole(lRoleName));
	}

}

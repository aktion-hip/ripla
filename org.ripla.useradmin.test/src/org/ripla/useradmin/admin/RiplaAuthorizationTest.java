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

import static org.junit.Assert.*;

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
public class RiplaAuthorizationTest {
	private static final String USER_NAME = "test.user";
	private static final String GROUP_NAME = "test.group";

	/**
	 * Test method for {@link org.ripla.useradmin.admin.RiplaAuthorization#getName()}.
	 * @throws Exception 
	 */
	@Test
	public void testGetName() throws Exception {
		UserAdmin lUserAdmin = TestRiplaUserAdmin.createUserAdmin();
		User lUser = (User) lUserAdmin.createRole(USER_NAME, Role.USER);		
		Authorization lAuthorization = lUserAdmin.getAuthorization(lUser);
		assertEquals(USER_NAME, lAuthorization.getName());
	}
	
	/**
	 * Test method for {@link org.ripla.useradmin.admin.RiplaAuthorization#getRoles()}.
	 * @throws Exception 
	 */
	@Test
	public void testGetRoles() throws Exception {
		UserAdmin lUserAdmin = TestRiplaUserAdmin.createUserAdmin();
		User lUser = (User) lUserAdmin.createRole(USER_NAME, Role.USER);		
		Authorization lAuthorization = lUserAdmin.getAuthorization(lUser);
		String[] lRoles = lAuthorization.getRoles();
		assertEquals(1, lRoles.length);
		assertEquals(USER_NAME, lRoles[0]);
		
		Group lGroup = (Group) lUserAdmin.createRole(GROUP_NAME, Role.GROUP);
		lAuthorization = lUserAdmin.getAuthorization(lGroup);
		lRoles = lAuthorization.getRoles();
		assertEquals(1, lRoles.length);
		assertEquals(GROUP_NAME, lRoles[0]);
		
		lGroup.addMember(lUser);
		lAuthorization = lUserAdmin.getAuthorization(lUser);
		lRoles = lAuthorization.getRoles();
		assertEquals(2, lRoles.length);
		assertContains(lRoles, GROUP_NAME);
		assertContains(lRoles, USER_NAME);
		
		lAuthorization = lUserAdmin.getAuthorization(lGroup);
		lRoles = lAuthorization.getRoles();
		assertEquals(1, lRoles.length);
		assertEquals(GROUP_NAME, lRoles[0]);
	}

	private void assertContains(String[] inRoles, String inRoleName) {
		for (String lRole : inRoles) {
			if (inRoleName.equals(lRole)) {
				return;
			}
		}
		fail();
	}
	
	/**
	 * Test method for {@link org.ripla.useradmin.admin.RiplaAuthorization#hasRole(java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void testHasRole() throws Exception {
		UserAdmin lUserAdmin = TestRiplaUserAdmin.createUserAdmin();
		User lUser = (User) lUserAdmin.createRole(USER_NAME, Role.USER);		
		Authorization lAuthorization = lUserAdmin.getAuthorization(lUser);
		assertFalse(lAuthorization.hasRole("test.role"));
		
		//test examples r4.cmpn: 107.8.3, see {@link Group} 
		String lRoleName = "foo";
		String lGroupName = "marketing";
		String lUserName1 = "alice";
		String lUserName2 = "bob";
		String lUserName3 = "adam";
		
		User lUser1 = (User) lUserAdmin.createRole(lUserName1, Role.USER);
		User lUser2 = (User) lUserAdmin.createRole(lUserName2, Role.USER);
		User lUser3 = (User) lUserAdmin.createRole(lUserName3, Role.USER);
		Group lMarketing = (Group) lUserAdmin.createRole(lGroupName, Role.GROUP);
		Group lFoo = (Group) lUserAdmin.createRole(lRoleName, Role.GROUP);
		
		lMarketing.addMember(lUser1);
		lMarketing.addMember(lUser2);
		lMarketing.addMember(lUser3);
		lFoo.addRequiredMember(lMarketing);
		lFoo.addMember(lUser1);
		lFoo.addMember(lUser2);
		
		lAuthorization = lUserAdmin.getAuthorization(lUser1);
		assertTrue(lAuthorization.hasRole(lRoleName));
		lAuthorization = lUserAdmin.getAuthorization(lUser2);
		assertTrue(lAuthorization.hasRole(lRoleName));
		lAuthorization = lUserAdmin.getAuthorization(lUser3);
		assertFalse(lAuthorization.hasRole(lRoleName));
		
		lFoo.removeMember(lUser2);
		lAuthorization = lUserAdmin.getAuthorization(lUser2);
		assertFalse(lAuthorization.hasRole(lRoleName));
		lFoo.removeMember(lUser1);
		lAuthorization = lUserAdmin.getAuthorization(lUser1);
		assertFalse(lAuthorization.hasRole(lRoleName));
		
		//add anonymous as basic member to foo
		lFoo.addMember(lUserAdmin.getRole(Role.USER_ANYONE));
		
		lAuthorization = lUserAdmin.getAuthorization(lUser1);
		assertTrue(lAuthorization.hasRole(lRoleName));
		lAuthorization = lUserAdmin.getAuthorization(lUser2);
		assertTrue(lAuthorization.hasRole(lRoleName));
		lAuthorization = lUserAdmin.getAuthorization(lUser3);
		assertTrue(lAuthorization.hasRole(lRoleName));
	}

}

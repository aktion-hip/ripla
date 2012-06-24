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
import org.osgi.service.useradmin.Group;
import org.osgi.service.useradmin.Role;
import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;

/**
 * 
 * @author Benno
 */
public class RiplaGroupTest {
	private static final String USER_NAME = "test.user";
	private static final String GROUP_NAME = "test.group";
	private static final String TEST_GROUP_NAME = "testing.group";

	/**
	 * Test method for {@link org.ripla.useradmin.admin.RiplaGroup#addMember(org.osgi.service.useradmin.Role)}.
	 * @throws Exception 
	 */
	@Test
	public void testAddMember() throws Exception {
		UserAdmin lUserAdmin = TestRiplaUserAdmin.createUserAdmin();
		User lUser = (User) lUserAdmin.createRole(USER_NAME, Role.USER);
		Group lGroup = (Group) lUserAdmin.createRole(GROUP_NAME, Role.GROUP);
		Group lTestGroup = (Group) lUserAdmin.createRole(TEST_GROUP_NAME, Role.GROUP);
		
		assertTrue(lTestGroup.addMember(lUser));
		assertTrue(lTestGroup.addMember(lGroup));
		
		assertFalse(lTestGroup.addMember(lUser));
		assertFalse(lTestGroup.addMember(lGroup));
		
		Role[] lMembers = lTestGroup.getMembers();
		assertEquals(2, lMembers.length);
	}
	
	/**
	 * Test method for {@link org.ripla.useradmin.admin.RiplaGroup#getMembers()}.
	 * @throws Exception 
	 */
	@Test
	public void testGetMembers() throws Exception {
		Group lGroup = TestRiplaUserAdmin.createGroup(TEST_GROUP_NAME);
		assertNull(lGroup.getMembers());
	}

	/**
	 * Test method for {@link org.ripla.useradmin.admin.RiplaGroup#addRequiredMember(org.osgi.service.useradmin.Role)}.
	 * @throws Exception 
	 */
	@Test
	public void testAddRequiredMember() throws Exception {
		UserAdmin lUserAdmin = TestRiplaUserAdmin.createUserAdmin();
		User lUser = (User) lUserAdmin.createRole(USER_NAME, Role.USER);
		Group lGroup = (Group) lUserAdmin.createRole(GROUP_NAME, Role.GROUP);
		Group lTestGroup = (Group) lUserAdmin.createRole(TEST_GROUP_NAME, Role.GROUP);
		
		assertTrue(lTestGroup.addRequiredMember(lUser));
		assertTrue(lTestGroup.addRequiredMember(lGroup));
		
		assertFalse(lTestGroup.addRequiredMember(lUser));
		assertFalse(lTestGroup.addRequiredMember(lGroup));
		
		Role[] lMembers = lTestGroup.getRequiredMembers();
		assertEquals(2, lMembers.length);
	}

	/**
	 * Test method for {@link org.ripla.useradmin.admin.RiplaGroup#getRequiredMembers()}.
	 * @throws Exception 
	 */
	@Test
	public void testGetRequiredMembers() throws Exception {
		Group lGroup = TestRiplaUserAdmin.createGroup(TEST_GROUP_NAME);
		assertNull(lGroup.getRequiredMembers());
	}
	
	/**
	 * Test method for {@link org.ripla.useradmin.admin.RiplaGroup#removeMember(org.osgi.service.useradmin.Role)}.
	 * @throws Exception 
	 */
	@Test
	public void testRemoveMember() throws Exception {
		UserAdmin lUserAdmin = TestRiplaUserAdmin.createUserAdmin();
		Group lTestGroup = (Group) lUserAdmin.createRole(TEST_GROUP_NAME, Role.GROUP);

		User lUser1 = (User) lUserAdmin.createRole(USER_NAME, Role.USER);
		User lUser2 = (User) lUserAdmin.createRole(USER_NAME+".r", Role.USER);
		Group lGroup1 = (Group) lUserAdmin.createRole(GROUP_NAME, Role.GROUP);
		Group lGroup2 = (Group) lUserAdmin.createRole(GROUP_NAME+".r", Role.GROUP);
		
		lTestGroup.addMember(lUser1);
		lTestGroup.addRequiredMember(lUser2);
		lTestGroup.addMember(lGroup1);
		lTestGroup.addRequiredMember(lGroup2);
		
		assertEquals(2, lTestGroup.getMembers().length);
		assertEquals(2, lTestGroup.getRequiredMembers().length);
		
		assertTrue(lTestGroup.removeMember(lUser1));
		assertTrue(lTestGroup.removeMember(lGroup1));
		assertFalse(lTestGroup.removeMember(lUser1));
		
		assertNull(lTestGroup.getMembers());
		assertEquals(2, lTestGroup.getRequiredMembers().length);
		
		assertTrue(lTestGroup.removeMember(lUser2));
		assertTrue(lTestGroup.removeMember(lGroup2));
		assertNull(lTestGroup.getRequiredMembers());
	}

}

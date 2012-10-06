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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.osgi.service.useradmin.Group;
import org.osgi.service.useradmin.Role;
import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;

/**
 * @author Luthiger
 */
public class RiplaGroupTest {
	private static final String USER_NAME = "test.user";
	private static final String GROUP_NAME = "test.group";
	private static final String TEST_GROUP_NAME = "testing.group";

	/**
	 * Test method for
	 * {@link org.ripla.useradmin.admin.RiplaGroup#addMember(org.osgi.service.useradmin.Role)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddMember() throws Exception { // NOPMD by Luthiger on
													// 09.09.12 00:05
		final UserAdmin lUserAdmin = TestRiplaUserAdmin.createUserAdmin();
		final User lUser = (User) lUserAdmin.createRole(USER_NAME, Role.USER);
		final Group lGroup = (Group) lUserAdmin.createRole(GROUP_NAME,
				Role.GROUP);
		final Group lTestGroup = (Group) lUserAdmin.createRole(TEST_GROUP_NAME,
				Role.GROUP);

		assertTrue("Could not add member", lTestGroup.addMember(lUser)); // NOPMD
																			// by
																			// Luthiger
																			// on
																			// 09.09.12
																			// 00:11
		assertTrue("Could not add member", lTestGroup.addMember(lGroup));

		assertFalse("Should not add member", lTestGroup.addMember(lUser)); // NOPMD
																			// by
																			// Luthiger
																			// on
																			// 09.09.12
																			// 00:11
		assertFalse("Should not add member", lTestGroup.addMember(lGroup));

		final Role[] lMembers = lTestGroup.getMembers();
		assertEquals("Wrong length", 2, lMembers.length); // NOPMD by Luthiger
															// on 09.09.12 00:11
	}

	/**
	 * Test method for {@link org.ripla.useradmin.admin.RiplaGroup#getMembers()}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetMembers() throws Exception { // NOPMD by Luthiger on
													// 09.09.12 00:06
		final Group lGroup = TestRiplaUserAdmin.createGroup(TEST_GROUP_NAME);
		assertNull("Group should not contain members", lGroup.getMembers());
	}

	/**
	 * Test method for
	 * {@link org.ripla.useradmin.admin.RiplaGroup#addRequiredMember(org.osgi.service.useradmin.Role)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddRequiredMember() throws Exception { // NOPMD by Luthiger
															// on 09.09.12 00:07
		final UserAdmin lUserAdmin = TestRiplaUserAdmin.createUserAdmin();
		final User lUser = (User) lUserAdmin.createRole(USER_NAME, Role.USER);
		final Group lGroup = (Group) lUserAdmin.createRole(GROUP_NAME,
				Role.GROUP);
		final Group lTestGroup = (Group) lUserAdmin.createRole(TEST_GROUP_NAME,
				Role.GROUP);

		assertTrue("Could not add member", lTestGroup.addRequiredMember(lUser));
		assertTrue("Could not add member", lTestGroup.addRequiredMember(lGroup));

		assertFalse("Should not add member",
				lTestGroup.addRequiredMember(lUser));
		assertFalse("Should not add member",
				lTestGroup.addRequiredMember(lGroup));

		final Role[] lMembers = lTestGroup.getRequiredMembers();
		assertEquals("Wrong length", 2, lMembers.length);
	}

	/**
	 * Test method for
	 * {@link org.ripla.useradmin.admin.RiplaGroup#getRequiredMembers()}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetRequiredMembers() throws Exception { // NOPMD by Luthiger
															// on 09.09.12 00:07
		final Group lGroup = TestRiplaUserAdmin.createGroup(TEST_GROUP_NAME);
		assertNull("Group should not contain members",
				lGroup.getRequiredMembers());
	}

	/**
	 * Test method for
	 * {@link org.ripla.useradmin.admin.RiplaGroup#removeMember(org.osgi.service.useradmin.Role)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRemoveMember() throws Exception { // NOPMD by Luthiger on
														// 09.09.12 00:07
		final UserAdmin lUserAdmin = TestRiplaUserAdmin.createUserAdmin();
		final Group lTestGroup = (Group) lUserAdmin.createRole(TEST_GROUP_NAME,
				Role.GROUP);

		final User lUser1 = (User) lUserAdmin.createRole(USER_NAME, Role.USER);
		final User lUser2 = (User) lUserAdmin.createRole(USER_NAME + ".r",
				Role.USER);
		final Group lGroup1 = (Group) lUserAdmin.createRole(GROUP_NAME,
				Role.GROUP);
		final Group lGroup2 = (Group) lUserAdmin.createRole(GROUP_NAME + ".r",
				Role.GROUP);

		lTestGroup.addMember(lUser1);
		lTestGroup.addRequiredMember(lUser2);
		lTestGroup.addMember(lGroup1);
		lTestGroup.addRequiredMember(lGroup2);

		assertEquals("Wrong length", 2, lTestGroup.getMembers().length);
		assertEquals("Wrong length", 2, lTestGroup.getRequiredMembers().length);

		assertTrue("Could not remove member", lTestGroup.removeMember(lUser1)); // NOPMD
		assertTrue("Could not remove member", lTestGroup.removeMember(lGroup1));
		assertFalse("Should not remove member", lTestGroup.removeMember(lUser1));

		assertNull("Should not contain members", lTestGroup.getMembers());
		assertEquals("Wrong length", 2, lTestGroup.getRequiredMembers().length);

		assertTrue("Could not remove member", lTestGroup.removeMember(lUser2));
		assertTrue("Could not remove member", lTestGroup.removeMember(lGroup2));
		assertNull("Should not contain members",
				lTestGroup.getRequiredMembers());
	}

}

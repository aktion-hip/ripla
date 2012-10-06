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

import java.util.Dictionary;
import java.util.Enumeration;

import org.junit.Test;
import org.osgi.service.useradmin.Role;
import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;

/**
 * @author Luthiger
 */
public class RiplaUserTest {
	private static final String USER_NAME = "test.user";

	/**
	 * Test method for {@link org.ripla.useradmin.admin.RiplaUser#getType()}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetType() throws Exception { // NOPMD by Luthiger on
		final User lUser = TestRiplaUserAdmin.createUser(USER_NAME);
		assertEquals("Wrong type", Role.USER, lUser.getType());
	}

	/**
	 * Test method for {@link org.ripla.useradmin.admin.RiplaRole#getName()}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetName() throws Exception { // NOPMD by Luthiger on
		final User lUser = TestRiplaUserAdmin.createUser(USER_NAME);
		assertEquals("Wrong name", USER_NAME, lUser.getName());
	}

	/**
	 * Test method for
	 * {@link org.ripla.useradmin.admin.RiplaRole#getProperties()}.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test(expected = IllegalArgumentException.class)
	public void testGetProperties() throws Exception { // NOPMD by Luthiger on
		final UserAdmin lUserAdmin = TestRiplaUserAdmin.createUserAdmin();
		User lUser = (User) lUserAdmin.createRole(USER_NAME, Role.USER);
		Dictionary lProperties = lUser.getProperties();
		assertTrue("Properties must be empty", lProperties.isEmpty());

		final String lKey1 = "test.key.1"; // NOPMD
		final String lKey2 = "test.key.2"; // NOPMD
		final String lValue1 = "String"; // NOPMD
		final byte[] lValue2 = new byte[] { '1', '2', '3' };
		lProperties.put(lKey1, lValue1);
		lProperties.put(lKey2, lValue2);

		lUser = (User) lUserAdmin.getRole(USER_NAME);
		lProperties = lUser.getProperties();
		assertEquals("Wrong value", lValue1, lProperties.get(lKey1)); // NOPMD
		assertEquals("Wrong value", lValue2, lProperties.get(lKey2));

		final Enumeration lKeys = lUser.getProperties().keys();
		int i = 0; // NOPMD
		while (lKeys.hasMoreElements()) {
			lKeys.nextElement();
			i++;
		}
		assertEquals("Wrong count", 2, i);

		lProperties.put("test.failing", Integer.valueOf(0));
	}

	/**
	 * Test method for
	 * {@link org.ripla.useradmin.admin.RiplaUser#getCredentials()}.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test(expected = IllegalArgumentException.class)
	public void testGetCredentials() throws Exception { // NOPMD by Luthiger on
		final UserAdmin lUserAdmin = TestRiplaUserAdmin.createUserAdmin();
		User lUser = (User) lUserAdmin.createRole(USER_NAME, Role.USER);
		Dictionary lCredentials = lUser.getCredentials();
		assertTrue("Credential must be empty", lCredentials.isEmpty());

		final String lKey1 = "test.credential.1"; // NOPMD
		final String lKey2 = "test.credential.2"; // NOPMD
		final String lValue1 = "String"; // NOPMD
		final byte[] lValue2 = new byte[] { '1', '2', '3' };
		lCredentials.put(lKey1, lValue1);
		lCredentials.put(lKey2, lValue2);

		lUser = (User) lUserAdmin.getRole(USER_NAME);
		lCredentials = lUser.getCredentials();
		assertEquals("Wrong value", lValue1, lCredentials.get(lKey1));
		assertEquals("Wrong value", lValue2, lCredentials.get(lKey2));

		final Enumeration lKeys = lUser.getCredentials().keys();
		int i = 0; // NOPMD
		while (lKeys.hasMoreElements()) {
			lKeys.nextElement();
			i++;
		}
		assertEquals("Wrong count", 2, i);

		lCredentials.put("test.failing", Integer.valueOf(0));
	}

	/**
	 * Test method for
	 * {@link org.ripla.useradmin.admin.RiplaUser#hasCredential(java.lang.String, java.lang.Object)}
	 * .
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testHasCredential() throws Exception { // NOPMD by Luthiger on
		final UserAdmin lUserAdmin = TestRiplaUserAdmin.createUserAdmin();
		User lUser = (User) lUserAdmin.createRole(USER_NAME, Role.USER);
		final Dictionary lCredentials = lUser.getCredentials();
		final String lKey = "test.credential"; // NOPMD
		final String lValue = "secret"; // NOPMD
		lCredentials.put(lKey, lValue);

		lUser = (User) lUserAdmin.getRole(USER_NAME);
		assertTrue("Wrong credential", lUser.hasCredential(lKey, lValue)); // NOPMD
		assertFalse("Wrong credential", lUser.hasCredential(lKey, "top.secret"));
		assertFalse("Wrong credential",
				lUser.hasCredential(lKey, Integer.valueOf(0)));
		assertFalse("Wrong credential", lUser.hasCredential(lKey + ".", lValue));
	}

}

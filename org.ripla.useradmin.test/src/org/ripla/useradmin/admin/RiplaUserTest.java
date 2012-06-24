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

import java.util.Dictionary;
import java.util.Enumeration;

import org.junit.Test;
import org.osgi.service.useradmin.Role;
import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;

/**
 * 
 * @author Benno
 */
public class RiplaUserTest {
	private static final String USER_NAME = "test.user";
	
	
	/**
	 * Test method for {@link org.ripla.useradmin.admin.RiplaUser#getType()}.
	 * @throws Exception 
	 */
	@Test
	public void testGetType() throws Exception {
		User lUser = TestRiplaUserAdmin.createUser(USER_NAME);
		assertEquals(Role.USER, lUser.getType());
	}
	
	/**
	 * Test method for {@link org.ripla.useradmin.admin.RiplaRole#getName()}.
	 * @throws Exception 
	 */
	@Test
	public void testGetName() throws Exception {
		User lUser = TestRiplaUserAdmin.createUser(USER_NAME);
		assertEquals(USER_NAME, lUser.getName());
	}
	
	/**
	 * Test method for {@link org.ripla.useradmin.admin.RiplaRole#getProperties()}.
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testGetProperties() throws Exception {
		UserAdmin lUserAdmin = TestRiplaUserAdmin.createUserAdmin();
		User lUser = (User) lUserAdmin.createRole(USER_NAME, Role.USER);
		Dictionary lProperties = lUser.getProperties();
		assertTrue(lProperties.isEmpty());
		
		String lKey1 = "test.key.1";
		String lKey2 = "test.key.2";
		String lValue1 = "String";
		byte[] lValue2 = new byte[] {'1','2','3'};
		lProperties.put(lKey1, lValue1);
		lProperties.put(lKey2, lValue2);
		
		lUser = (User) lUserAdmin.getRole(USER_NAME);
		lProperties = lUser.getProperties();
		assertEquals(lValue1, lProperties.get(lKey1));
		assertEquals(lValue2, lProperties.get(lKey2));
		
		Enumeration lKeys = lUser.getProperties().keys();
		int i = 0;
		while (lKeys.hasMoreElements()) {
			lKeys.nextElement();
			i++;
		}
		assertEquals(2, i);
		
		try {
			lProperties.put("test.failing", new Integer(0));
			fail("shouldn't get here");
		}
		catch (IllegalArgumentException exc) {
			//intentionally left empty
		}
	}

	/**
	 * Test method for {@link org.ripla.useradmin.admin.RiplaUser#getCredentials()}.
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testGetCredentials() throws Exception {
		UserAdmin lUserAdmin = TestRiplaUserAdmin.createUserAdmin();
		User lUser = (User) lUserAdmin.createRole(USER_NAME, Role.USER);
		Dictionary lCredentials = lUser.getCredentials();
		assertTrue(lCredentials.isEmpty());
		
		String lKey1 = "test.credential.1";
		String lKey2 = "test.credential.2";
		String lValue1 = "String";
		byte[] lValue2 = new byte[] {'1','2','3'};
		lCredentials.put(lKey1, lValue1);
		lCredentials.put(lKey2, lValue2);
		
		lUser = (User) lUserAdmin.getRole(USER_NAME);
		lCredentials = lUser.getCredentials();
		assertEquals(lValue1, lCredentials.get(lKey1));
		assertEquals(lValue2, lCredentials.get(lKey2));
		
		Enumeration lKeys = lUser.getCredentials().keys();
		int i = 0;
		while (lKeys.hasMoreElements()) {
			lKeys.nextElement();
			i++;
		}
		assertEquals(2, i);
		
		try {
			lCredentials.put("test.failing", new Integer(0));
			fail("shouldn't get here");
		}
		catch (IllegalArgumentException exc) {
			//intentionally left empty
		}
	}

	/**
	 * Test method for {@link org.ripla.useradmin.admin.RiplaUser#hasCredential(java.lang.String, java.lang.Object)}.
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testHasCredential() throws Exception {
		UserAdmin lUserAdmin = TestRiplaUserAdmin.createUserAdmin();
		User lUser = (User) lUserAdmin.createRole(USER_NAME, Role.USER);
		Dictionary lCredentials = lUser.getCredentials();
		String lKey = "test.credential";
		String lValue = "secret";
		lCredentials.put(lKey, lValue);
		
		lUser = (User) lUserAdmin.getRole(USER_NAME);
		assertTrue(lUser.hasCredential(lKey, lValue));
		assertFalse(lUser.hasCredential(lKey, "top.secret"));
		assertFalse(lUser.hasCredential(lKey, new Integer(0)));
		assertFalse(lUser.hasCredential(lKey+".", lValue));
	}

}

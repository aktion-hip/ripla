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
package org.ripla.useradmin.internal;

import java.util.Enumeration;
import java.util.Hashtable;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.useradmin.Role;
import org.ripla.useradmin.admin.RiplaUserAdmin;
import org.ripla.useradmin.interfaces.IUserAdminStore;

/**
 * 
 * 
 * @author Luthiger
 */
public abstract class UserAdminHashtable extends Hashtable<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -397030865421289240L;
	private Role role;
	private RiplaUserAdmin userAdmin;
	private IUserAdminStore userAdminStore;

	protected UserAdminHashtable(Role inRole, RiplaUserAdmin inUserAdmin) {
		role = inRole;
		userAdmin = inUserAdmin;
		userAdminStore = inUserAdmin.getUserAdminStore();
	}
	
	protected RiplaUserAdmin getUserAdmin() {
		return userAdmin;
	}
	
	protected IUserAdminStore getUserAdminStore() {
		return userAdminStore;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Hashtable#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(String inKey, Object inValue) {
		if (!(inKey instanceof String)) {
			throw new IllegalArgumentException("Invalide type: the key has to be a String!");
		}

		if (!((inValue instanceof String) || (inValue instanceof byte[]))) {
			throw new IllegalArgumentException("Invalid type: the value has to be a String!");
		}

		String lName = (String) inKey;
		checkChangePermission(lName);
		return put(lName, inValue, role, true);
	}
	
	abstract protected Object put(String inKey, Object inValue, Role inRole, boolean inGenerateEvent);
	
	abstract protected void checkChangePermission(String inName);
	
	protected Object putHash(String inKey, Object inValue) {
		return super.put(inKey, inValue);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Hashtable#remove(java.lang.Object)
	 */
	public synchronized Object remove(Object inKey) {
		if (!(inKey instanceof String)) {
			throw new IllegalArgumentException("Invalide type: the key has to be a String!");
		}

		String lName = (String) inKey;
		try {
			removeItem(role, lName);
		} 
		catch (BackingStoreException exc) {
			return null;
		}
		return super.remove(lName);
	}
	
	abstract protected void removeItem(Role inRole, String inName) throws BackingStoreException;

	public synchronized void clear() {
		Enumeration<String> lKeys = keys();
		while (lKeys.hasMoreElements()) {
			String lName = (String) lKeys.nextElement();
			checkChangePermission(lName);
		}
		try {
			clearItem(role);
		}
		catch (BackingStoreException exc) {
			return;
		}
		super.clear();
	}
	
	abstract protected void clearItem(Role inRole) throws BackingStoreException;

	public Object get(Object inKey) {
		if (!(inKey instanceof String)) {
			throw new IllegalArgumentException("Invalid type: the key has to be a String!");
		}

		String lName = (String) inKey;
		checkGetCredentialPermission(lName);
		return super.get(lName);
	}
	
	abstract protected void checkGetCredentialPermission(String inName);
	
}
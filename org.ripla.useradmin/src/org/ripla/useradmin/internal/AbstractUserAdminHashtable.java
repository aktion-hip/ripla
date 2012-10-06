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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class for tables that can be used to store roles and
 * credentials.
 * 
 * @author Luthiger
 */
public abstract class AbstractUserAdminHashtable extends
		Hashtable<String, Object> {
	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractUserAdminHashtable.class);

	private static final long serialVersionUID = -397030865421289240L;
	private final transient Role role;
	private final transient RiplaUserAdmin userAdmin;
	private transient IUserAdminStore userAdminStore;

	protected AbstractUserAdminHashtable(final Role inRole,
			final RiplaUserAdmin inUserAdmin) {
		super();

		role = inRole;
		userAdmin = inUserAdmin;
		try {
			userAdminStore = inUserAdmin.getUserAdminStore();
		}
		catch (final BackingStoreException exc) {
			LOG.error(
					"Error encountered while retrieving the user admin store!",
					exc);
		}
	}

	protected RiplaUserAdmin getUserAdmin() {
		return userAdmin;
	}

	protected IUserAdminStore getUserAdminStore() throws BackingStoreException {
		if (userAdminStore == null) {
			throw new BackingStoreException("No user admin store created!");
		}
		return userAdminStore;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Hashtable#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object put(final String inKey, final Object inValue) {
		if (!(inKey instanceof String)) {
			throw new IllegalArgumentException(
					"Invalide type: the key has to be a String!");
		}

		if (!((inValue instanceof String) || (inValue instanceof byte[]))) {
			throw new IllegalArgumentException(
					"Invalid type: the value has to be a String!");
		}

		final String lName = inKey;
		checkChangePermission(lName);
		return put(lName, inValue, role, true);
	}

	abstract protected Object put(String inKey, Object inValue, Role inRole,
			boolean inGenerateEvent);

	abstract protected void checkChangePermission(String inName);

	protected Object putHash(final String inKey, final Object inValue) {
		return super.put(inKey, inValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Hashtable#remove(java.lang.Object)
	 */
	@Override
	public synchronized Object remove(final Object inKey) { // NOPMD by Luthiger
															// on 07.09.12 00:27
		if (!(inKey instanceof String)) {
			throw new IllegalArgumentException(
					"Invalide type: the key has to be a String!");
		}

		final String lName = (String) inKey;
		try {
			removeItem(role, lName);
		}
		catch (final BackingStoreException exc) {
			return null;
		}
		return super.remove(lName);
	}

	abstract protected void removeItem(Role inRole, String inName)
			throws BackingStoreException;

	@Override
	public synchronized void clear() { // NOPMD by Luthiger on 07.09.12 00:27
		final Enumeration<String> lKeys = keys();
		while (lKeys.hasMoreElements()) {
			final String lName = lKeys.nextElement();
			checkChangePermission(lName);
		}
		try {
			clearItem(role);
		}
		catch (final BackingStoreException exc) {
			return;
		}
		super.clear();
	}

	abstract protected void clearItem(Role inRole) throws BackingStoreException;

	@Override
	public Object get(final Object inKey) {
		if (!(inKey instanceof String)) {
			throw new IllegalArgumentException(
					"Invalid type: the key has to be a String!");
		}

		final String lName = (String) inKey;
		checkGetCredentialPermission(lName);
		return super.get(lName);
	}

	abstract protected void checkGetCredentialPermission(String inName);

}
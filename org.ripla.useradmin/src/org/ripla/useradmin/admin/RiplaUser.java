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

import java.util.Dictionary;
import java.util.List;

import org.osgi.service.useradmin.Role;
import org.osgi.service.useradmin.User;
import org.ripla.useradmin.internal.CredentialsHashtable;

/**
 * <p>
 * A <tt>User</tt> role managed by a User Admin service.
 * </p>
 * <p>
 * In this context, the term "user" is not limited to just human beings.
 * Instead, it refers to any entity that may have any number of credentials
 * associated with it that it may use to authenticate itself.
 * </p>
 * <p>
 * In general, <tt>User</tt>objects are associated with a specific User Admin
 * service (namely the one that created them), and cannot be used with other
 * User Admin services.
 * </p>
 * <p>
 * A <tt>User</tt>object may have credentials (and properties, inherited from
 * the <a href="../../../../org/osgi/service/useradmin/Role.html"
 * title="interface in org.osgi.service.useradmin"><code>Role</code></a>class)
 * associated with it. Specific <a
 * href="../../../../org/osgi/service/useradmin/UserAdminPermission.html"
 * title="class in org.osgi.service.useradmin"><code>UserAdminPermission</code>
 * </a>objects are required to read or change a <tt>User</tt> object's
 * credentials.
 * </p>
 * <p>
 * Credentials are <tt>Dictionary</tt> objects and have semantics that are
 * similar to the properties in the <tt>Role</tt> class.
 * </p>
 * 
 * @author Luthiger
 */
public class RiplaUser extends RiplaRole implements User {

	private final transient RiplaUserAdmin userAdmin;
	private final transient CredentialsHashtable credentials;

	/**
	 * RiplaUser constructor.
	 * 
	 * @param inName
	 *            String
	 * @param inUserAdmin
	 *            {@link RiplaUserAdmin}
	 */
	public RiplaUser(final String inName, final RiplaUserAdmin inUserAdmin) {
		super(inName, inUserAdmin);
		userAdmin = inUserAdmin;
		credentials = new CredentialsHashtable(this, inUserAdmin);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.service.useradmin.User#getCredentials()
	 */
	@Override
	public Dictionary<String, Object> getCredentials() {
		userAdmin.checkAlive();
		return credentials;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.service.useradmin.User#hasCredential(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public boolean hasCredential(final String inKey, final Object inValue) {
		userAdmin.checkAlive();
		final Object lCheckValue = credentials.get(inKey);
		if (lCheckValue != null) {
			if (inValue instanceof String) { // NOPMD by Luthiger on 07.09.12
												// 00:19
				if (lCheckValue.equals(inValue)) {
					return true;
				} else if (inValue instanceof byte[]) { // NOPMD by Luthiger on
														// 07.09.12 00:19
					if (!(lCheckValue instanceof byte[])) {
						return false;
					}
					final byte[] lValueArray = (byte[]) inValue;
					final byte[] lCheckValueArray = (byte[]) lCheckValue;
					final int lLength = lValueArray.length;
					if (lLength != lCheckValueArray.length) {
						return false;
					}
					for (int i = 0; i < lLength; i++) {
						if (lValueArray[i] != lCheckValueArray[i]) {
							return false;
						}
					}
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.useradmin.admin.RiplaRole#getType()
	 */
	@Override
	public int getType() {
		userAdmin.checkAlive();
		return USER;
	}

	/*
	 * A user always implies itself
	 */
	@Override
	protected boolean isImpliedBy(final Role inRole,
			final List<String> inCheckLoop) {
		if (inCheckLoop.contains(getName())) {
			return false;
		}
		inCheckLoop.add(getName());
		return inRole.getName().equals(getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.useradmin.admin.RiplaRole#toString()
	 */
	@Override
	public String toString() {
		return String.format("User: ", getName());
	}

}

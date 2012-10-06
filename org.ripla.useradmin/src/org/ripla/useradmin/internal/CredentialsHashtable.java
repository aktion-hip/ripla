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

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.useradmin.Role;
import org.osgi.service.useradmin.UserAdminEvent;
import org.ripla.useradmin.admin.RiplaUserAdmin;

/**
 * The store for credentials.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class CredentialsHashtable extends AbstractUserAdminHashtable {

	/**
	 * CredentialsHashtable constructor.
	 * 
	 * @param inRole
	 * @param inUserAdmin
	 * @param inPropertyType
	 */
	public CredentialsHashtable(final Role inRole,
			final RiplaUserAdmin inUserAdmin) {
		super(inRole, inUserAdmin);
	}

	/*
	 * We want to generate an event every time we put something into the
	 * hashtable, except upon initialization where role data is being read from
	 * persistent store.
	 */
	@Override
	protected synchronized Object put(final String inKey, final Object inValue, // NOPMD
																				// by
																				// Luthiger
																				// on
																				// 07.09.12
																				// 00:24
			final Role inRole, final boolean inGenerateEvent) {
		if (inGenerateEvent) {
			try {
				getUserAdminStore().addCredential(inRole, inKey, inValue);
			}
			catch (final BackingStoreException exc) {
				return null;
			}
			getUserAdmin().getEventProducer().generateEvent(
					UserAdminEvent.ROLE_CHANGED, inRole);
		}
		return putHash(inKey, inValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ripla.useradmin.internal.UserAdminHashtable#checkChangePermission
	 * (java.lang.String)
	 */
	@Override
	protected void checkChangePermission(final String inName) {
		getUserAdmin().checkChangeCredentialPermission(inName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ripla.useradmin.internal.UserAdminHashtable#removeItem(org.osgi.service
	 * .useradmin.Role, java.lang.String)
	 */
	@Override
	protected void removeItem(final Role inRole, final String inName)
			throws BackingStoreException {
		getUserAdmin().checkChangeCredentialPermission(inName);
		getUserAdminStore().removeCredential(inRole, inName);
		getUserAdmin().getEventProducer().generateEvent(
				UserAdminEvent.ROLE_CHANGED, inRole);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ripla.useradmin.internal.UserAdminHashtable#clearItem(org.osgi.service
	 * .useradmin.Role)
	 */
	@Override
	protected void clearItem(final Role inRole) throws BackingStoreException {
		getUserAdminStore().clearCredentials(inRole);
		getUserAdmin().getEventProducer().generateEvent(
				UserAdminEvent.ROLE_CHANGED, inRole);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ripla.useradmin.internal.UserAdminHashtable#checkGetCredentialPermission
	 * (java.lang.String)
	 */
	@Override
	protected void checkGetCredentialPermission(final String inName) {
		getUserAdmin().checkGetCredentialPermission(inName);
	}

}

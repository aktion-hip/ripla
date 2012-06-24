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
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class PropertiesHashtable extends UserAdminHashtable {

	/**
	 * @param inRole
	 * @param inUserAdmin
	 * @param inPropertyType
	 */
	public PropertiesHashtable(Role inRole, RiplaUserAdmin inUserAdmin) {
		super(inRole, inUserAdmin);
	}

	/*
	 *  We want to generate an event every time we put something into the hashtable, except
	 *  upon initialization where role data is being read from persistent store.
	 */
	protected synchronized Object put(String inKey, Object inValue, Role inRole, boolean inGenerateEvent) {
		if (inGenerateEvent) {
			try {
				getUserAdminStore().addProperty(inRole, inKey, inValue);
			} 
			catch (BackingStoreException exc) {
				return null;
			}
			getUserAdmin().getEventProducer().generateEvent(UserAdminEvent.ROLE_CHANGED, inRole);
		}
		return putHash(inKey, inValue);
	}

	/* (non-Javadoc)
	 * @see org.ripla.useradmin.internal.UserAdminHashtable#checkChangePermission(java.lang.String)
	 */
	@Override
	protected void checkChangePermission(String inName) {
		getUserAdmin().checkChangePropertyPermission(inName);		
	}

	/* (non-Javadoc)
	 * @see org.ripla.useradmin.internal.UserAdminHashtable#removeItem(org.osgi.service.useradmin.Role, java.lang.String)
	 */
	@Override
	protected void removeItem(Role inRole, String inName) throws BackingStoreException {
		getUserAdmin().checkChangePropertyPermission(inName);
		getUserAdminStore().removeProperty(inRole, inName);
		getUserAdmin().getEventProducer().generateEvent(UserAdminEvent.ROLE_CHANGED, inRole);
		
	}

	/* (non-Javadoc)
	 * @see org.ripla.useradmin.internal.UserAdminHashtable#clearItem(org.osgi.service.useradmin.Role)
	 */
	@Override
	protected void clearItem(Role inRole) throws BackingStoreException {
		getUserAdminStore().clearProperties(inRole);
		getUserAdmin().getEventProducer().generateEvent(UserAdminEvent.ROLE_CHANGED, inRole);
		
	}

	/* (non-Javadoc)
	 * @see org.ripla.useradmin.internal.UserAdminHashtable#checkGetCredentialPermission(java.lang.String)
	 */
	@Override
	protected void checkGetCredentialPermission(String inName) {
		// nothing to do
	}
	
}

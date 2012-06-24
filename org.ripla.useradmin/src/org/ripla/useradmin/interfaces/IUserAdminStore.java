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

package org.ripla.useradmin.interfaces;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.useradmin.Group;
import org.osgi.service.useradmin.Role;

/**
 * Interface for classes storing the entities managed by the user admin.
 * 
 * @author Luthiger
 */
public interface IUserAdminStore {
	
	/**
	 * Initializes the store, i.e. loads the entities from the storage.
	 *  
	 * @throws BackingStoreException
	 */
	void initialize() throws BackingStoreException;
	
	/**
	 * Add a <code>Role</code>.
	 * 
	 * @param inRole {@link Role}
	 * @throws BackingStoreException
	 */
	void addRole(final Role inRole) throws BackingStoreException;
	
	/**
	 * Remove the specified <code>Role</code>.
	 * 
	 * @param inRole {@link Role}
	 * @throws BackingStoreException
	 */
	void removeRole(final Role inRole) throws BackingStoreException;
	
	/**
	 * Add a (basic) member to the specified group.
	 * 
	 * @param inGroup {@link Group}
	 * @param inRole {@link Role}
	 * @throws BackingStoreException
	 */
	void addMember(final Group inGroup, final Role inRole) throws BackingStoreException;
	
	/**
	 * Add a required member to the specified group.
	 * 
	 * @param inGroup {@link Group}
	 * @param inRole {@link Role}
	 * @throws BackingStoreException
	 */
	void addRequiredMember(final Group inGroup, final Role inRole) throws BackingStoreException;
	
	/**
	 * Remove the specified member from the specified group.
	 * 
	 * @param inGroup {@link Group}
	 * @param inRole {@link Role}
	 * @throws BackingStoreException
	 */
	void removeMember(final Group inGroup, final Role inRole) throws BackingStoreException;
	
	/**
	 * Set the specified credential to the specified role.
	 * 
	 * @param inRole {@link Role}
	 * @param inKey String
	 * @param inValue Object
	 * @throws BackingStoreException
	 */
	void addCredential(final Role inRole, final String inKey, final Object inValue) throws BackingStoreException;

	/**
	 * Remove the credential with the specified key.
	 * 
	 * @param inRole {@link Role}
	 * @param inKey String
	 * @throws BackingStoreException
	 */
	void removeCredential(final Role inRole, final String inKey) throws BackingStoreException;
	
	/**
	 * Clear the role's credentials.
	 * 
	 * @param inRole {@link Role}
	 * @throws BackingStoreException
	 */
	void clearCredentials(final Role inRole) throws BackingStoreException;
	
	/**
	 * Set the specified property to the specified role.
	 * 
	 * @param inRole {@link Role}
	 * @param inKey String
	 * @param inValue Object
	 * @throws BackingStoreException
	 */
	void addProperty(final Role inRole, final String inKey, final Object inValue) throws BackingStoreException;

	/**
	 * Remove the specified property from the specified role.
	 * 
	 * @param inRole {@link Role}
	 * @param inKey String
	 * @throws BackingStoreException
	 */
	void removeProperty(final Role inRole, final String inKey) throws BackingStoreException;

	/**
	 * Clear the specified role's properties.
	 * 
	 * @param inRole {@link Role}
	 * @throws BackingStoreException
	 */
	void clearProperties(final Role inRole) throws BackingStoreException;

	/**
	 * Removes all resources.
	 */
	void destroy();
	
}

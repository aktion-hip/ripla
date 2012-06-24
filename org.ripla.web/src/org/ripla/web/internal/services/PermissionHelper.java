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

package org.ripla.web.internal.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;

import org.osgi.service.useradmin.Group;
import org.osgi.service.useradmin.Role;
import org.osgi.service.useradmin.UserAdmin;
import org.ripla.web.Constants;
import org.ripla.web.services.IPermissionEntry;

/**
 * Helper class to manage registrations of <code>IPermissionEntry</code> instances.
 * 
 * @author Luthiger
 */
public class PermissionHelper {
	private Collection<IPermissionEntry> permissionsToCreate = new ArrayList<IPermissionEntry>();
	private Collection<IPermissionEntry> permissionsToRemove = new ArrayList<IPermissionEntry>();
	private UserAdmin userAdmin;
	private boolean initialized;
	
	/**
	 * Sets (or removes) the user admin instance.
	 * 
	 * @param inUserAdmin {@link UserAdmin} may be <code>null</code>
	 */
	public synchronized void setUserAdmin(UserAdmin inUserAdmin) {
		userAdmin = inUserAdmin;
		if (initialized && userAdmin != null) {
			processPermissions();
		}
	}
	
	private void processPermissions() {		
		for (IPermissionEntry lPermission : permissionsToCreate) {
			createPermission(lPermission, userAdmin);
		}
		permissionsToCreate.clear();
		for (IPermissionEntry lPermission : permissionsToRemove) {
			destroyPermission(lPermission, userAdmin);
		}
		permissionsToRemove.clear();
	}

	/**
	 * @param inPermission IPermissionEntry adds the permission, i.e. creates the action group instance
	 */
	public void addPermission(IPermissionEntry inPermission) {
		if (initialized && userAdmin != null) {
			createPermission(inPermission, userAdmin);
		}
		else {
			permissionsToCreate.add(inPermission);
		}
	}

	/**
	 * @param inPermission {@link IPermissionEntry} removes the permission, i.e. destroys the action group instance
	 */
	public void removePermission(IPermissionEntry inPermission) {
		if (userAdmin != null) {
			destroyPermission(inPermission, userAdmin);
		}
		else {
			if (permissionsToCreate.contains(inPermission)) {
				permissionsToCreate.remove(inPermission);
			}
			else {				
				permissionsToRemove.add(inPermission);
			}
		}
	}
	
	/**
	 * Marks this instance as initialized and processes pending permission registrations.
	 */
	public void initializePermissions() {
		initialized = true;
		if (userAdmin == null) {
			return;
		}
		processPermissions();
	}

	private void destroyPermission(IPermissionEntry inPermission, UserAdmin inUserAdmin) {
		inUserAdmin.removeRole(inPermission.getPermissionName());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void createPermission(IPermissionEntry inPermission, UserAdmin inUserAdmin) {
		Group lPermission = (Group) inUserAdmin.createRole(inPermission.getPermissionName(), Role.GROUP);
		if (lPermission != null) {
			Dictionary lProperties = lPermission.getProperties();
			lProperties.put(Constants.PERMISSION_DESCRIPTION_KEY, inPermission.getPermissionDescription());
			
			addMembers(inUserAdmin, lPermission, inPermission.getMemberNames());
			addMembers(inUserAdmin, lPermission, inPermission.getRequieredMemberNames());
		}
	}
	
	private void addMembers(UserAdmin inUserAdmin, Group inPermission, String[] inMemberNames) {
		Role lMember = null;
		for (String lMemberName : inMemberNames) {
			lMember = inUserAdmin.getRole(lMemberName);
			if (lMember != null) {					
				inPermission.addMember(lMember);
			}
		}
	}
	
}

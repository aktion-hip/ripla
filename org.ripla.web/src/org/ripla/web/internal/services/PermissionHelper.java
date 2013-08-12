/*******************************************************************************
 * Copyright (c) 2012-2013 RelationWare, Benno Luthiger
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
import org.ripla.services.IPermissionEntry;
import org.ripla.web.Constants;

/**
 * Helper class to manage registrations of <code>IPermissionEntry</code>
 * instances.
 * 
 * @author Luthiger
 */
public final class PermissionHelper {
	private final transient Collection<IPermissionEntry> permissionsToCreate = new ArrayList<IPermissionEntry>();
	private final transient Collection<IPermissionEntry> permissionsToRemove = new ArrayList<IPermissionEntry>();
	private transient UserAdmin userAdmin;
	private transient boolean initialized;

	/**
	 * Sets (or removes) the user admin instance.
	 * 
	 * @param inUserAdmin
	 *            {@link UserAdmin} may be <code>null</code>
	 */
	public synchronized void setUserAdmin(final UserAdmin inUserAdmin) { // NOPMD
		userAdmin = inUserAdmin;
		if (initialized && userAdmin != null) {
			processPermissions();
		}
	}

	private void processPermissions() {
		for (final IPermissionEntry lPermission : permissionsToCreate) {
			createPermission(lPermission, userAdmin);
		}
		permissionsToCreate.clear();
		for (final IPermissionEntry lPermission : permissionsToRemove) {
			destroyPermission(lPermission, userAdmin);
		}
		permissionsToRemove.clear();
	}

	/**
	 * @param inPermission
	 *            IPermissionEntry adds the permission, i.e. creates the action
	 *            group instance
	 */
	public void addPermission(final IPermissionEntry inPermission) {
		if (initialized && userAdmin != null) {
			createPermission(inPermission, userAdmin);
		} else {
			permissionsToCreate.add(inPermission);
		}
	}

	/**
	 * @param inPermission
	 *            {@link IPermissionEntry} removes the permission, i.e. destroys
	 *            the action group instance
	 */
	public void removePermission(final IPermissionEntry inPermission) {
		if (userAdmin == null) {
			if (permissionsToCreate.contains(inPermission)) {
				permissionsToCreate.remove(inPermission);
			} else {
				permissionsToRemove.add(inPermission);
			}
		} else {
			destroyPermission(inPermission, userAdmin);
		}
	}

	/**
	 * Marks this instance as initialized and processes pending permission
	 * registrations.
	 */
	public void initializePermissions() {
		initialized = true;
		if (userAdmin == null) {
			return;
		}
		processPermissions();
	}

	private void destroyPermission(final IPermissionEntry inPermission,
			final UserAdmin inUserAdmin) {
		inUserAdmin.removeRole(inPermission.getPermissionName());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void createPermission(final IPermissionEntry inPermission,
			final UserAdmin inUserAdmin) {
		final Group lPermission = (Group) inUserAdmin.createRole(
				inPermission.getPermissionName(), Role.GROUP);
		if (lPermission != null) {
			final Dictionary lProperties = lPermission.getProperties();
			lProperties.put(Constants.PERMISSION_DESCRIPTION_KEY,
					inPermission.getPermissionDescription());

			addMembers(inUserAdmin, lPermission, inPermission.getMemberNames());
			addMembers(inUserAdmin, lPermission,
					inPermission.getRequieredMemberNames());
		}
	}

	private void addMembers(final UserAdmin inUserAdmin,
			final Group inPermission, final String[] inMemberNames) {
		Role lMember = null;
		for (final String lMemberName : inMemberNames) {
			lMember = inUserAdmin.getRole(lMemberName);
			if (lMember != null) {
				inPermission.addMember(lMember);
			}
		}
	}

}

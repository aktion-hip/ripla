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

import org.osgi.service.useradmin.UserAdmin;
import org.ripla.services.IExtendibleMenuContribution;
import org.ripla.services.IPermissionEntry;
import org.ripla.web.services.IUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The service component consuming the <code>IUseCase</code> implementations.
 * 
 * @author Luthiger
 */
public class UseCaseComponent { // NOPMD
	private static final Logger LOG = LoggerFactory
			.getLogger(UseCaseComponent.class);

	/**
	 * Registers the specified use case.
	 * 
	 * @param inUseCase
	 *            {@link IUseCase}
	 */
	public void addUseCase(final IUseCase inUseCase) {
		LOG.debug("Added use case {}.", inUseCase);
		UseCaseRegistry.INSTANCE.addUseCase(inUseCase);
	}

	/**
	 * Unregisters the specified use case.
	 * 
	 * @param inUseCase
	 *            {@link IUseCase}
	 */
	public void removeUseCase(final IUseCase inUseCase) {
		LOG.debug("Removed use case {}.", inUseCase);
		UseCaseRegistry.INSTANCE.removeUseCase(inUseCase);
	}

	/**
	 * Registers the contribution to an extendible menu.
	 * 
	 * @param inMenuContribution
	 *            {@link IExtendibleMenuContribution}
	 */
	public void registerMenuContribution(
			final IExtendibleMenuContribution inMenuContribution) {
		LOG.debug("Registered extendible menu contribution '{}'.",
				inMenuContribution.getExtendibleMenuID());
		UseCaseRegistry.INSTANCE.registerMenuContribution(inMenuContribution);
	}

	/**
	 * Unregisters the contribution of an extendible menu.
	 * 
	 * @param inMenuContribution
	 *            {@link IExtendibleMenuContribution}
	 */
	public void unregisterMenuContribution(
			final IExtendibleMenuContribution inMenuContribution) {
		LOG.debug("Unregistered extendible menu contribution '{}'.",
				inMenuContribution.getExtendibleMenuID());
		UseCaseRegistry.INSTANCE.unregisterMenuContribution(inMenuContribution);
	}

	/**
	 * Injects the user admin.
	 * 
	 * @param inUserAdmin
	 *            {@link UserAdmin}
	 */
	public void setUserAdmin(final UserAdmin inUserAdmin) {
		UseCaseRegistry.INSTANCE.setUserAdmin(inUserAdmin);
		LOG.debug("The OSGi user admin service is made available.");
	}

	/**
	 * Unbinds the user admin.
	 * 
	 * @param inUserAdmin
	 *            {@link UserAdmin}
	 */
	public void unsetUserAdmin(final UserAdmin inUserAdmin) {
		UseCaseRegistry.INSTANCE.setUserAdmin(null);
		LOG.debug("Removed the OSGi user admin service.");
	}

	/**
	 * Injects a permission entry.
	 * 
	 * @param inPermission
	 *            {@link IPermissionEntry}
	 */
	public void registerPermission(final IPermissionEntry inPermission) {
		LOG.debug("Registered permission '{}'.",
				inPermission.getPermissionName());
		UseCaseRegistry.INSTANCE.getPermissionHelper().addPermission(
				inPermission);
	}

	/**
	 * Unbinds the permission entry.
	 * 
	 * @param inPermission
	 *            {@link IPermissionEntry}
	 */
	public void unregisterPermission(final IPermissionEntry inPermission) {
		LOG.debug("Unregistered permission '{}'.",
				inPermission.getPermissionName());
		UseCaseRegistry.INSTANCE.getPermissionHelper().removePermission(
				inPermission);
	}

}

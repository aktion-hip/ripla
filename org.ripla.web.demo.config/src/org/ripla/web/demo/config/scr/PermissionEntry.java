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

package org.ripla.web.demo.config.scr;

import org.ripla.services.IPermissionEntry;
import org.ripla.web.demo.config.Constants;

/**
 * <p>
 * Provider of the <code>IPermissionEntry</code> service.
 * </p>
 * <p>
 * We protect the skin configuration functionality. We want to authorize only
 * members of the <code>admin</code> group to make skin configuration.
 * </p>
 * 
 * @author Luthiger
 */
public class PermissionEntry implements IPermissionEntry {

	@Override
	public String getPermissionName() {
		return Constants.PERMISSION_SELECT_SKIN;
	}

	@Override
	public String getPermissionDescription() {
		return "Permission to select the skins.";
	}

	@Override
	public String[] getRequieredMemberNames() {
		return new String[] {};
	}

	@Override
	public String[] getMemberNames() {
		return new String[] { org.ripla.web.demo.Constants.ADMIN_GROUP_NAME };
	}

}

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

package org.ripla.demo.config.scr;

import org.ripla.demo.config.Constants;
import org.ripla.web.services.IPermissionEntry;

/**
 * <p>Provider of the <code>IPermissionEntry</code> service.</p>
 * <p>We protect the skin configuration functionality.
 * We want to authorize only members of the <code>admin</code> group
 * to make skin configuration.</p>
 * 
 * @author Luthiger
 */
public class PermissionEntry implements IPermissionEntry {

	/* (non-Javadoc)
	 * @see org.ripla.web.services.IPermissionEntry#getPermissionName()
	 */
	@Override
	public String getPermissionName() {
		return Constants.PERMISSION_SELECT_SKIN;
	}

	/* (non-Javadoc)
	 * @see org.ripla.web.services.IPermissionEntry#getPermissionDescription()
	 */
	@Override
	public String getPermissionDescription() {
		return "Permission to select the skins.";
	}

	/* (non-Javadoc)
	 * @see org.ripla.web.services.IPermissionEntry#getRequieredMemberNames()
	 */
	@Override
	public String[] getRequieredMemberNames() {
		return new String[] {};
	}

	/* (non-Javadoc)
	 * @see org.ripla.web.services.IPermissionEntry#getMemberNames()
	 */
	@Override
	public String[] getMemberNames() {
		return new String[] {org.ripla.demo.Constants.ADMIN_GROUP_NAME};
	}

}

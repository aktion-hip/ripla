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

package org.ripla.demo.useradmin.internal;

import org.osgi.service.useradmin.UserAdmin;
import org.ripla.useradmin.admin.RiplaUserAdmin;

/**
 * Provider of the OSGi <code>UserAdmin</code> service.
 * 
 * @author Luthiger
 */
public class DemoUserAdmin extends RiplaUserAdmin implements UserAdmin {

	/**
	 * @throws Exception
	 */
	public DemoUserAdmin() throws Exception { // NOPMD by Luthiger on 06.09.12 23:50
		super();
	}

}

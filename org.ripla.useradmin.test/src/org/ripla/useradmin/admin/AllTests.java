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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * 
 * @author Benno
 */
@RunWith(Suite.class)
@SuiteClasses({ RiplaAuthorizationTest.class, RiplaGroupTest.class,
		RiplaUserAdminTest.class, RiplaUserTest.class })
public class AllTests { // NOPMD by Luthiger on 08.09.12 23:38

}

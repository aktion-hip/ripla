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

package org.ripla.web.interfaces;

import org.osgi.service.event.EventAdmin;
import org.ripla.web.RiplaApplication;

/**
 * Interface for actions triggered by a user interaction on a toolbar item.
 *
 * @author Luthiger
 */
public interface IToolbarAction {
	
	/**
	 * Executes the action, i.e. modify the application's state.
	 * 
	 * @param inApplication {@link RiplaApplication} the application instance
	 * @param inEventAdmin {@link EventAdmin} the application's event admin
	 */
	void run(RiplaApplication inApplication, EventAdmin inEventAdmin);

}

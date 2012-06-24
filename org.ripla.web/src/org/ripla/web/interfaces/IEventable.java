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

/**
 * Interface for controllers that can send events using the OSGi <code>EventAdmin</code>.
 * 
 * @author Luthiger
 */
public interface IEventable {

	/**
	 * Sets the OSGi event admin to enable event handling.
	 * 
	 * @param inEventAdmin {@link EventAdmin}
	 */
	void setEventAdmin(EventAdmin inEventAdmin);

}

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

package org.ripla.interfaces;

import java.util.Map;

/**
 * Interface for even dispatchers used by the Ripla application.<br />
 * The event dispatcher is used to send events of the specified
 * <code>Event</code> type between components.
 * 
 * @author Luthiger
 */
public interface IRiplaEventDispatcher {
	public enum Event {
		LOAD_CONTROLLER, LOAD_CONTEXT_MENU, REFRESH, REFRESH_SKIN, REFRESH_UI, LOGOUT;
	}

	/**
	 * Dispatch the event with the specified type and properties.
	 * 
	 * @param inType
	 * @param inProperties
	 */
	void dispatch(final Event inType, final Map<String, Object> inProperties);

}

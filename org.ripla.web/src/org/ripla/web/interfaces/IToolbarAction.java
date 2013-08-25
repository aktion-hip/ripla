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


/**
 * Interface for actions triggered by a user interaction on a toolbar item.
 * 
 * @author Luthiger
 */
public interface IToolbarAction {

	/**
	 * Executes the action, i.e. modify the application's state. Usage:
	 * 
	 * <pre>
	 * public void run() {
	 * 	VaadinSession
	 * 			.getCurrent()
	 * 			.getAttribute(IRiplaEventDispatcher.class)
	 * 			.dispatch(IRiplaEventDispatcher.Event.REFRESH,
	 * 					new HashMap&lt;String, Object&gt;());
	 * }
	 * </pre>
	 */
	void run();

}

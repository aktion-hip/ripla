/*******************************************************************************
 * Copyright (c) 2013 RelationWare, Benno Luthiger
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * RelationWare, Benno Luthiger
 ******************************************************************************/
package org.ripla.rap.interfaces;

/**
 * Interface for listeners of <code>IToolbarAction</code> events.
 * 
 * @author Luthiger
 */
public interface IToolbarActionListener {

	/**
	 * Process the specified <code>IToolbarAction</code>. This method will be
	 * called when the user triggers an action on the toolbar item.
	 * 
	 * @param inAction
	 *            {@link IToolbarAction}
	 */
	void processAction(IToolbarAction inAction);

}

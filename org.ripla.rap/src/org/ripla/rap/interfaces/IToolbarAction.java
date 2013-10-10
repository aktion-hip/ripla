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
 * Interface for actions triggered by a user interaction on a toolbar item.
 * 
 * @author Luthiger
 */
public interface IToolbarAction {

	/**
	 * Executes the action, i.e. modify the application's state.
	 */
	void run();

}

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
package org.ripla.interfaces;

/**
 * Interface for classes that configure the menu items on the application's main
 * menu.
 * 
 * @author Luthiger
 */
public interface IMenuItem extends IMenuElement {

	/**
	 * @return int position in Vaadin menu bar.
	 */
	int getPosition();

}

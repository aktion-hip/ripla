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
package org.ripla.services;

import org.ripla.interfaces.IMenuElement;
import org.ripla.util.ExtendibleMenuMarker;

/**
 * Interface for a contribution to a <code>IMenuExtendible</code> type of menu.
 * 
 * @author Luthiger
 */
public interface IExtendibleMenuContribution extends IMenuElement {

	/**
	 * Returns the ID of the extendible menu this item is contributing to.
	 * 
	 * @return String
	 */
	String getExtendibleMenuID();

	/**
	 * Returns the position of this contribution within the extendible menu.
	 * 
	 * @return ExtendibleMenuMarker.{@link Position}
	 */
	ExtendibleMenuMarker.Position getPosition();

}

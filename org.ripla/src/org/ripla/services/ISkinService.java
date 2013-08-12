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

package org.ripla.services;

/**
 * Service to create new skin instances.
 * 
 * @author Luthiger
 */
public interface ISkinService {

	/**
	 * @return String this skin bundle's ID, i.e. symbolic name:
	 *         <code>bundleContext.getBundle().getSymbolicName()</code>.
	 */
	String getSkinID();

	/**
	 * @return String the name of the skin, displayed in the skin select view
	 */
	String getSkinName();

	/**
	 * Creates a new skin instance.
	 * 
	 * @return {@link ISkin} the new <code>ISkin</code> instance
	 */
	ISkin createSkin();

}

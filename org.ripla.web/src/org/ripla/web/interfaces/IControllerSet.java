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
 * Interface for classes providing a set of <code>IPluggable</code>s.
 * 
 * @author Luthiger
 */
public interface IControllerSet {

	/**
	 * @return IControllerConfiguration[] the set of configurations for the controllers provided by this bundle.
	 */
	IControllerConfiguration[] getControllerConfigurations();
	
}

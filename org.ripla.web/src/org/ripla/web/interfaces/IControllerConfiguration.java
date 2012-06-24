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

import org.osgi.framework.Bundle;

/**
 * Interface for classes configuring a controller.
 * 
 * @author Luthiger
 */
public interface IControllerConfiguration {

	/**
	 * @return {@link Bundle} the bundle providing the controller
	 */
	Bundle getBundle();

	/**
	 * @return String the name of the controller. The controller class has to implement <code>IPluggable</code>
	 */
	String getControllerName();

}

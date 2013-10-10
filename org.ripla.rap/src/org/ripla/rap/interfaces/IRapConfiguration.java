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

import org.eclipse.rap.rwt.service.ResourceLoader;
import org.ripla.interfaces.IAppConfiguration;
import org.ripla.rap.app.RiplaBase;

/**
 * Interface for objects to configure a Ripla application on RAP.
 * 
 * @author Luthiger
 */
public interface IRapConfiguration extends IAppConfiguration {

	/**
	 * @return String a valid path to register the entry point at
	 */
	String getPath();

	/**
	 * @return Class&lt;? extends RiplaBase> the type to be use as the
	 *         application's entry point
	 */
	Class<? extends RiplaBase> getEntryPointType();

	/**
	 * @return {@link ResourceLoader} the resource loader for the favicon
	 */
	ResourceLoader getFaviconLoader();

}

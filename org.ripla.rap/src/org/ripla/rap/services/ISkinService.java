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

package org.ripla.rap.services;

import org.eclipse.rap.rwt.application.Application;

/**
 * Service to create new skin instances for Ripla RAP.
 * 
 * @author Luthiger
 */
public interface ISkinService extends org.ripla.services.ISkinService {

	/**
	 * Make the specified application using the skins created by this service.
	 * 
	 * @param inApplication
	 */
	void handleStyle(Application inApplication);

}

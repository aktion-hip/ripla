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

package org.ripla.web.interfaces;

import org.osgi.service.useradmin.UserAdmin;
import org.ripla.exceptions.RiplaException;

import com.vaadin.ui.Component;

/**
 * Interface for all Controllers in the framework. Controllers implementing this
 * interface have basically to implement the <code>run()</code> method, which
 * should contain all the actions of this task.
 * 
 * @author Luthiger
 */
public interface IPluggable {

	/**
	 * Sets the OSGi user admin to enable user administration.
	 * 
	 * @param inUserAdmin
	 *            {@link UserAdmin}
	 */
	void setUserAdmin(UserAdmin inUserAdmin);

	/**
	 * Runs this task.
	 * 
	 * @return {@link Component} the component created by the task.
	 * @throws RiplaException
	 */
	Component run() throws RiplaException;

}

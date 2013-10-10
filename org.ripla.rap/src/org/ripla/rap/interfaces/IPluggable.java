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

import org.eclipse.swt.widgets.Composite;
import org.osgi.service.useradmin.UserAdmin;
import org.ripla.exceptions.RiplaException;

/**
 * Interface for all Controllers in the framework. Controllers implementing this
 * interface have basically to implement the <code>run()</code> method, which
 * should contain all the actions of this task and return the view as result.
 * <p>
 * Instances of this class implement the controller functionality in the MVC
 * pattern.
 * </p>
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
	 * @param inParent
	 *            {@link Composite} the parent the view this controller is
	 *            managing has to be placed on
	 */
	void setParent(Composite inParent);

	/**
	 * Runs this pluggable use case.
	 * 
	 * @return {@link Composite} the view controlled by this instance.
	 * @throws RiplaException
	 */
	Composite run() throws RiplaException;

}

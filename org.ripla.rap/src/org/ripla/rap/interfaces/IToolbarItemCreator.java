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
import org.eclipse.swt.widgets.Control;
import org.osgi.service.useradmin.User;

/**
 * Factory class for toolbar item components.
 * 
 * @author Luthiger
 */
public interface IToolbarItemCreator {

	/**
	 * Creates an instance of the UI component to display on the toolbar.
	 * 
	 * @param inToolbar
	 *            {@link Composite} the toolbar composite
	 * @param inBody
	 *            {@link IBodyComponent}
	 * @param inUser
	 *            {@link User} the user
	 * @return {@link Control} the created component to display in the toolbar
	 */
	Control createToolbarItem(Composite inToolbar, IBodyComponent inBody,
			User inUser);

}

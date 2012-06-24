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

import org.osgi.service.useradmin.User;
import org.ripla.web.RiplaApplication;

import com.vaadin.ui.Component;

/**
 * Factory class for toolbar item components.
 * 
 * @author Luthiger
 */
public interface IToolbarItemCreator {

	/**
	 * Creates an instance of the UI component to display on the toolbar.
	 * 
	 * @param inApplication {@link RiplaApplication} the application instance
	 * @param inUser {@link User} the user
	 * @return {@link Component} the created component to display in the toolbar
	 */
	Component createToolbarItem(RiplaApplication inApplication, User inUser);
	
}

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
package org.ripla.web.internal.menu;

import java.util.Collection;
import java.util.Vector;

import org.ripla.web.interfaces.IMenuExtendible;
import org.ripla.web.services.IExtendibleMenuContribution;

/**
 * Helper class for handling extendible menus (i.e. <code>IVIFMenuExtendible</code>) and their contributions.<br />
 * This class contains all contributions for the same menu, identified by <code>IExtendibleMenuContribution.getExtendibleMenuID()</code>.
 * 
 * @author Luthiger
 * Created: 29.10.2011
 * @see IVIFMenuExtendible
 */
public class ExtendibleMenuHandler {
	private Collection<IExtendibleMenuContribution> contributions = new Vector<IExtendibleMenuContribution>();
	
	/**
	 * Constructor 
	 * 
	 * @param inContribution {@link IExtendibleMenuContribution}
	 */
	public ExtendibleMenuHandler(IExtendibleMenuContribution inContribution) {
		contributions.add(inContribution);
	}

	/**
	 * Returns the factory to create the extendible menu.
	 * 
	 * @return {@link MenuFactory}
	 */
	public MenuFactory getMenuFactory(IMenuExtendible inExtendibleMenu) {
		return new ExtendibleMenuFactory(inExtendibleMenu, contributions);
	}

	/**
	 * @param inContribution {@link IExtendibleMenuContribution} adds the menu contribution
	 */
	public void addContribution(IExtendibleMenuContribution inContribution) {
		contributions.add(inContribution);		
	}

	/**
	 * @param inContribution {@link IExtendibleMenuContribution} removes the menu contribution
	 */
	public void removeContribution(IExtendibleMenuContribution inContribution) {
		contributions.remove(inContribution);
	}

}

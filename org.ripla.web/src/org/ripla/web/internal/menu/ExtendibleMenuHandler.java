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
package org.ripla.web.internal.menu;

import java.util.ArrayList;
import java.util.Collection;

import org.ripla.interfaces.IMenuExtendible;
import org.ripla.services.IExtendibleMenuContribution;

/**
 * Helper class for handling extendible menus (i.e. <code>IMenuExtendible</code>
 * ) and their contributions.<br />
 * This class contains all contributions for the same menu, identified by
 * <code>IExtendibleMenuContribution.getExtendibleMenuID()</code>.
 * 
 * @author Luthiger
 * @see IMenuExtendible
 */
public final class ExtendibleMenuHandler {
	private final transient Collection<IExtendibleMenuContribution> contributions = new ArrayList<IExtendibleMenuContribution>();

	/**
	 * Constructor
	 * 
	 * @param inContribution
	 *            {@link IExtendibleMenuContribution}
	 */
	public ExtendibleMenuHandler(
			final IExtendibleMenuContribution inContribution) {
		contributions.add(inContribution);
	}

	/**
	 * Returns the factory to create the extendible menu.
	 * 
	 * @return {@link MenuFactory}
	 */
	public MenuFactory getMenuFactory(final IMenuExtendible inExtendibleMenu) {
		return new ExtendibleMenuFactory(inExtendibleMenu, contributions);
	}

	/**
	 * @param inContribution
	 *            {@link IExtendibleMenuContribution} adds the menu contribution
	 */
	public void addContribution(final IExtendibleMenuContribution inContribution) {
		contributions.add(inContribution);
	}

	/**
	 * @param inContribution
	 *            {@link IExtendibleMenuContribution} removes the menu
	 *            contribution
	 */
	public void removeContribution(
			final IExtendibleMenuContribution inContribution) {
		contributions.remove(inContribution);
	}

}

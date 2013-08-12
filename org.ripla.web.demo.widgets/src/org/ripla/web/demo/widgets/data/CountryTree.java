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

package org.ripla.web.demo.widgets.data;

import java.util.ArrayList;
import java.util.Collection;

import com.vaadin.data.util.HierarchicalContainer;

/**
 * Helper class to prepare the tree of countries.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public final class CountryTree extends HierarchicalContainer {

	private CountryTree() {
		super();
	}

	/**
	 * Factory method, creates a tree of countries in regions.
	 * 
	 * @param inCountries
	 *            {@link CountryContainer}
	 * @return {@link CountryTree}
	 */
	public static CountryTree createContainer(final CountryContainer inCountries) {
		final Collection<String> lRegions = new ArrayList<String>();

		final CountryTree out = new CountryTree();

		for (final CountryBean lCountry : inCountries.getItemIds()) {
			final String lRegion = lCountry.getUnRegion11();
			if (!lRegions.contains(lRegion)) {
				out.addItem(lRegion);
				out.setChildrenAllowed(lRegion, true);
				lRegions.add(lRegion);
			}
			out.addItem(lCountry);
			out.setParent(lCountry, lRegion);
			out.setChildrenAllowed(lCountry, false);
		}

		return out;
	}

}

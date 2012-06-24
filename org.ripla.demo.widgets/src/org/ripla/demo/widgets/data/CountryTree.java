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

package org.ripla.demo.widgets.data;

import java.util.ArrayList;
import java.util.Collection;

import com.vaadin.data.util.HierarchicalContainer;

/**
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class CountryTree extends HierarchicalContainer {
	
	/**
	 * Factory method, creates a tree of countries in regions.
	 * 
	 * @param inCountries {@link CountryContainer}
	 * @return {@link CountryTree}
	 */
	public static CountryTree createContainer(CountryContainer inCountries) {
		Collection<String> lRegions = new ArrayList<String>();
		
		CountryTree out = new CountryTree();
		
		for (CountryBean lCountry : inCountries.getItemIds()) {
			String lRegion = lCountry.getUnRegion11();
			if (!lRegions.contains(lRegion)) {
				out.addItem(lRegion);
				out.setChildrenAllowed(lRegion, true);
				lRegions.add(lRegion);
			}
			out.addItem(lCountry);
			out.setParent(lCountry, lRegion);
			out.setChildrenAllowed(lCountry, false);
		};
		
		return out;
	}

}

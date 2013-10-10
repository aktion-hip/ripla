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

package org.ripla.rap.demo.widgets.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.ripla.rap.demo.widgets.data.Countries.CountryComparator;

/**
 * Model for the RAP tree widgets.
 * 
 * @author Luthiger
 */
public final class CountryTree {

	private CountryTree() {
		// prevent instantiation
	}

	public static TreeObject createTree(final List<CountryBean> inCountries) {
		final Collection<String> lRegions = new ArrayList<String>();
		Collections.sort(inCountries, new CountryComparator(2, true));

		final TreeObject outRoot = new TreeObject(null);
		TreeObject lRegionChild = null;
		for (final CountryBean lCountry : inCountries) {
			final String lRegion = lCountry.getUnRegion11();
			if (!lRegions.contains(lRegion)) {
				lRegionChild = outRoot.addChild(lCountry);
				lRegionChild.setLabel(lRegion);
				lRegions.add(lRegion);
			}
			lRegionChild.addChild(lCountry);
		}

		return outRoot;
	}

	// ---

	public static class TreeObject {
		private final List<TreeObject> children;
		private TreeObject parent;
		private final CountryBean country;
		private String label;

		public TreeObject(final CountryBean inCountry) {
			country = inCountry;
			label = inCountry == null ? "" : inCountry.getName();
			children = new ArrayList<TreeObject>();
		}

		public TreeObject addChild(final CountryBean inCountry) {
			final TreeObject out = new TreeObject(inCountry);
			children.add(out);
			out.setParent(this);
			return out;
		}

		public void setLabel(final String inLabel) {
			label = inLabel;
		}

		public String getLabel() {
			return label;
		}

		public CountryBean getCountry() {
			return country;
		}

		public void setParent(final TreeObject inParent) {
			parent = inParent;
		}

		public TreeObject getParent() {
			return parent;
		}

		/**
		 * @return Object[] children
		 */
		public Object[] getChidren() {
			return children.toArray(new Object[] {});
		}

		/**
		 * @return boolean
		 */
		public boolean hasChildren() {
			return !children.isEmpty();
		}
	}

}

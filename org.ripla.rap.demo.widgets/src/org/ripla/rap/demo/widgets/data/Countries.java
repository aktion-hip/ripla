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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.ripla.interfaces.IMessages;
import org.ripla.rap.demo.widgets.Activator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides the country data.
 * 
 * @author Luthiger
 */
public final class Countries {
	private static final Logger LOG = LoggerFactory.getLogger(Countries.class);
	private static final String INPUT_NAME = "TableInput.csv"; //$NON-NLS-1$

	private static final IMessages MESSAGES = Activator.getMessages();
	public static final ColHeader[] COL_HEADERS = new ColHeader[] {
			new ColHeader("widgets.data.country.code", 60),
			new ColHeader("widgets.data.country.name", 120),
			new ColHeader("widgets.data.country.regions.un11", 60),
			new ColHeader("widgets.data.country.regions.sres4", 60),
			new ColHeader("1990"), new ColHeader("1995"),
			new ColHeader("2000"), new ColHeader("2005"),
			new ColHeader("2010"), new ColHeader("2015"),
			new ColHeader("2020"), new ColHeader("2025"),
			new ColHeader("2030"), new ColHeader("2035"),
			new ColHeader("2040"), new ColHeader("2045"),
			new ColHeader("2050"), new ColHeader("2055"),
			new ColHeader("2060"), new ColHeader("2065"),
			new ColHeader("2070"), new ColHeader("2075"),
			new ColHeader("2080"), new ColHeader("2085"),
			new ColHeader("2090"), new ColHeader("2095"), new ColHeader("2100") };

	private static List<CountryBean> countries = new ArrayList<CountryBean>();

	static {
		countries = loadData(Countries.class.getResourceAsStream(INPUT_NAME));
	}

	private Countries() {
		// prevent instantiation
	}

	private static List<CountryBean> loadData(final InputStream inInput) {
		final List<CountryBean> out = new ArrayList<CountryBean>();

		BufferedReader lReader = null;
		try {
			lReader = new BufferedReader(new InputStreamReader(inInput));
			String lLine = null;
			while ((lLine = lReader.readLine()) != null) { // NOPMD by Luthiger
				out.add(CountryBean.createItem(lLine));
			}
		} catch (final IOException exc) {
			LOG.error("Error encountered while reading the input data!", exc); //$NON-NLS-1$
		} finally {
			if (lReader != null) {
				try {
					lReader.close();
				} catch (final IOException exc) {
					LOG.error("Problem while closing the input stream!", exc); //$NON-NLS-1$
				}
			}
		}
		return out;
	}

	/**
	 * @return List&lt;CountryBean> the countries
	 */
	public static List<CountryBean> getCountries() {
		return new ArrayList<CountryBean>(countries);
	}

	/**
	 * @return String[] an array containing all country names
	 */
	public static String[] getCountryNames() {
		final List<CountryBean> lCountries = getCountries();
		final String[] out = new String[lCountries.size()];
		int i = 0;
		for (final CountryBean lCountry : lCountries) {
			out[i++] = lCountry.toString();
		}
		return out;
	}

	// ---

	/**
	 * Helper class for table heading of countries table.
	 * 
	 * @author Luthiger
	 */
	public static class ColHeader {
		public final String text;
		public final int width;

		protected ColHeader(final String inKey, final int inWidth) {
			text = MESSAGES.getMessage(inKey);
			width = inWidth;
		}

		protected ColHeader(final String inText) {
			text = inText;
			width = 60;
		}
	}

	/**
	 * Comparator class to sort countries according to their properties.
	 * 
	 * @author Luthiger
	 */
	@SuppressWarnings("serial")
	public static class CountryComparator extends ViewerComparator implements
			Comparator<CountryBean> {

		private final int column;
		private final boolean ascending;

		/**
		 * CountryComparator constructor.
		 * 
		 * @param inColumn
		 *            int
		 * @param inAscending
		 *            boolean
		 */
		public CountryComparator(final int inColumn, final boolean inAscending) {
			column = inColumn;
			ascending = inAscending;
		}

		@Override
		public int compare(final Viewer inViewer, final Object inCountry1,
				final Object inCountry2) {
			return this.compare((CountryBean) inCountry1,
					(CountryBean) inCountry2);
		}

		@Override
		public int compare(final CountryBean inCountry1,
				final CountryBean inCountry2) {
			final String lValue1 = inCountry1.getValue(column);
			final String lValue2 = inCountry2.getValue(column);
			if (column == 0 || column > 3) {
				final int lVal1 = Integer.parseInt(lValue1.replace("'", ""));
				final int lVal2 = Integer.parseInt(lValue2.replace("'", ""));
				return (lVal1 - lVal2) * (ascending ? 1 : -1);

			}
			return lValue1.compareToIgnoreCase(lValue2) * (ascending ? 1 : -1);
		}

		@Override
		public boolean isSorterProperty(final Object inElement,
				final String inProperty) {
			return true;
		}

	}

}

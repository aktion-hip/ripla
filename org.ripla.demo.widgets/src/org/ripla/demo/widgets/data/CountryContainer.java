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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.ripla.demo.widgets.Activator;
import org.ripla.web.interfaces.IMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.util.BeanItemContainer;

/**
 * The container for the country data, used as input for the table.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class CountryContainer extends BeanItemContainer<CountryBean> {
	private static final Logger	LOG = LoggerFactory.getLogger(CountryContainer.class);
	
	private static final IMessages MESSAGES = Activator.getMessages();
	public static final String[] NATURAL_COL_ORDER = new String[] {"unCode", "name", "unRegion11", "sresRegion", "pop1990", "pop1995", "pop2000", "pop2005", "pop2010", "pop2015", "pop2020", "pop2025", "pop2030", "pop2035", "pop2040", "pop2045", "pop2050", "pop2055", "pop2060", "pop2065", "pop2070", "pop2075", "pop2080", "pop2085", "pop2090", "pop2095", "pop2100"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$ //$NON-NLS-17$ //$NON-NLS-18$ //$NON-NLS-19$ //$NON-NLS-20$ //$NON-NLS-21$ //$NON-NLS-22$ //$NON-NLS-23$ //$NON-NLS-24$ //$NON-NLS-25$ //$NON-NLS-26$ //$NON-NLS-27$
	public static final String[] COL_HEADERS = new String[] {MESSAGES.getMessage("widgets.data.country.code"), MESSAGES.getMessage("widgets.data.country.name"), MESSAGES.getMessage("widgets.data.country.regions.un11"), MESSAGES.getMessage("widgets.data.country.regions.sres4"), "1990", "1995", "2000", "2005", "2010", "2015", "2020", "2025", "2030", "2035", "2040", "2045", "2050", "2055", "2060", "2065", "2070", "2075", "2080", "2085", "2090", "2095", "2100"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$ //$NON-NLS-17$ //$NON-NLS-18$ //$NON-NLS-19$ //$NON-NLS-20$ //$NON-NLS-21$ //$NON-NLS-22$ //$NON-NLS-23$ //$NON-NLS-24$ //$NON-NLS-25$ //$NON-NLS-26$ //$NON-NLS-27$

	private CountryContainer() throws IllegalArgumentException {
		super(CountryBean.class);
	}
	
	/**
	 * Factory method.
	 * 
	 * @param inInput {@link InputStream}
	 * @return {@link CountryContainer}
	 */
	public static CountryContainer createData(InputStream inInput) {
		CountryContainer out = new CountryContainer();
		
		BufferedReader lReader = null;
		try {
			lReader = new BufferedReader(new InputStreamReader(inInput));
			String lLine = null;
			while ((lLine = lReader.readLine()) != null) {
				out.addItem(CountryBean.createItem(lLine));
			}
		} catch (IOException exc) {
			LOG.error("Error encountered while reading the input data!", exc); //$NON-NLS-1$
		}
		finally {
			if (lReader != null) {
				try {
					lReader.close();
				} catch (IOException exc) {
					LOG.error("Problem while closing the input stream!", exc); //$NON-NLS-1$
				}
			}
		}
		return out;
	}

}

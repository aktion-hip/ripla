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

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * The model for country data.
 * 
 * @author Luthiger
 */
public class CountryBean {
	private static final NumberFormat FORMAT = new DecimalFormat("#,##0"); //$NON-NLS-1$
	private static final String SEPARATOR = ";"; //$NON-NLS-1$
	
	private String pop2100;
	private String pop2095;
	private String pop2090;
	private String pop2085;
	private String pop2080;
	private String pop2075;
	private String pop2070;
	private String pop2065;
	private String pop2060;
	private String pop2055;
	private String pop2050;
	private String pop2045;
	private String pop2040;
	private String pop2035;
	private String pop2030;
	private String pop2025;
	private String pop2020;
	private String pop2015;
	private String pop2010;
	private String pop2005;
	private String pop2000;
	private String pop1995;
	private String pop1990;
	private String sresRegion;
	private String unRegion11;
	private String name;
	private String unCode;
	
	private CountryBean(String inLine) {
		String[] lData = inLine.split(SEPARATOR);
		unCode = lData[0];
		name = lData[1];
		unRegion11 = lData[2];
		sresRegion = lData[3];
		pop1990 = format(lData[4]);
		pop1995 = format(lData[5]);
		pop2000 = format(lData[6]);
		pop2005 = format(lData[7]);
		pop2010 = format(lData[8]);
		pop2015 = format(lData[9]);
		pop2020 = format(lData[10]);
		pop2025 = format(lData[11]);
		pop2030 = format(lData[12]);
		pop2035 = format(lData[13]);
		pop2040 = format(lData[14]);
		pop2045 = format(lData[15]);
		pop2050 = format(lData[16]);
		pop2055 = format(lData[17]);
		pop2060 = format(lData[18]);
		pop2065 = format(lData[19]);
		pop2070 = format(lData[20]);
		pop2075 = format(lData[21]);
		pop2080 = format(lData[22]);
		pop2085 = format(lData[23]);
		pop2090 = format(lData[24]);
		pop2095 = format(lData[25]);
		pop2100 = format(lData[26]);
	}
	
	private String format(String inValue) {
		long lValue = Long.parseLong(inValue);
		return FORMAT.format(lValue);
	}

	/**
	 * Factory method
	 * 
	 * @param inLine String the input data, ';' separated 
	 * @return {@link CountryBean}
	 */
	public static CountryBean createItem(String inLine) {
		return new CountryBean(inLine);
	}

	/**
	 * @return the pop2100
	 */
	public String getPop2100() {
		return pop2100;
	}

	/**
	 * @return the pop2095
	 */
	public String getPop2095() {
		return pop2095;
	}

	/**
	 * @return the pop2090
	 */
	public String getPop2090() {
		return pop2090;
	}

	/**
	 * @return the pop2085
	 */
	public String getPop2085() {
		return pop2085;
	}

	/**
	 * @return the pop2080
	 */
	public String getPop2080() {
		return pop2080;
	}

	/**
	 * @return the pop2075
	 */
	public String getPop2075() {
		return pop2075;
	}

	/**
	 * @return the pop2070
	 */
	public String getPop2070() {
		return pop2070;
	}

	/**
	 * @return the pop2065
	 */
	public String getPop2065() {
		return pop2065;
	}

	/**
	 * @return the pop2060
	 */
	public String getPop2060() {
		return pop2060;
	}

	/**
	 * @return the pop2055
	 */
	public String getPop2055() {
		return pop2055;
	}

	/**
	 * @return the pop2050
	 */
	public String getPop2050() {
		return pop2050;
	}

	/**
	 * @return the pop2045
	 */
	public String getPop2045() {
		return pop2045;
	}

	/**
	 * @return the pop2040
	 */
	public String getPop2040() {
		return pop2040;
	}

	/**
	 * @return the pop2035
	 */
	public String getPop2035() {
		return pop2035;
	}

	/**
	 * @return the pop2030
	 */
	public String getPop2030() {
		return pop2030;
	}

	/**
	 * @return the pop2025
	 */
	public String getPop2025() {
		return pop2025;
	}

	/**
	 * @return the pop2020
	 */
	public String getPop2020() {
		return pop2020;
	}

	/**
	 * @return the pop2015
	 */
	public String getPop2015() {
		return pop2015;
	}

	/**
	 * @return the pop2010
	 */
	public String getPop2010() {
		return pop2010;
	}

	/**
	 * @return the pop2005
	 */
	public String getPop2005() {
		return pop2005;
	}

	/**
	 * @return the pop2000
	 */
	public String getPop2000() {
		return pop2000;
	}

	/**
	 * @return the pop1995
	 */
	public String getPop1995() {
		return pop1995;
	}

	/**
	 * @return the pop1990
	 */
	public String getPop1990() {
		return pop1990;
	}

	/**
	 * @return the sresRegion
	 */
	public String getSresRegion() {
		return sresRegion;
	}

	/**
	 * @return the unRegion11
	 */
	public String getUnRegion11() {
		return unRegion11;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the unCode
	 */
	public String getUnCode() {
		return unCode;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%s (%s)", getName(), getSresRegion()); //$NON-NLS-1$
	}

}

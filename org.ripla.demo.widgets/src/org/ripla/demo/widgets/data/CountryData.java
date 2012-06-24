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


/**
 * Singleton instance to provide the <code>CountryContainer</code>.
 * 
 * @author Luthiger
 */
public enum CountryData {
	INSTANCE;
	
	private static final String INPUT_NAME = "TableInput.csv"; //$NON-NLS-1$
	
	private CountryContainer data;
	
	/**
	 * Loads the data from the file system.
	 * 
	 * @return CountryContainer
	 */
	public CountryContainer getCountryContainer() {
		if (data == null) {
			data = CountryContainer.createData(CountryContainer.class.getResourceAsStream(INPUT_NAME));
		}
		return data;
	}

}

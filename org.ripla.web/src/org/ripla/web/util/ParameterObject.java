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

package org.ripla.web.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic parameter object intended to pass parameters between <code>IPluggable</code> instances.
 * 
 * @author Luthiger
 */
public class ParameterObject {
	private Map<String, Object> parameterSet = new HashMap<String, Object>();
	
	/**
	 * This method sets the value of the specified parameter.
	 * 
	 * @param inName String
	 * @param inValue {@link Object}
	 */
	public void set(String inName, Object inValue) {
		parameterSet.put(inName, inValue);
	}
	
	/**
	 * Returns the value of the specified parameter.
	 * 
	 * @param inName
	 * @return {@link Object}
	 */
	public Object get(String inName) {
		return parameterSet.get(inName);
	}

}

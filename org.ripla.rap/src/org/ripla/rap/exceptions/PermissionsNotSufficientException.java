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
package org.ripla.rap.exceptions;

import org.ripla.exceptions.RiplaException;

/**
 * Exception thrown when a user attempts to execute a task he has not sufficient
 * permissions to do so.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class PermissionsNotSufficientException extends RiplaException {

	/**
	 * @param inSimpleMessage
	 */
	public PermissionsNotSufficientException() {
		super("Not sufficient permissions to execute the task!");
	}

}

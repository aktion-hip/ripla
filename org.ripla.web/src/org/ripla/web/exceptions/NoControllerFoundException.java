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

package org.ripla.web.exceptions;

/**
 * Exception used when the controller manager can't find the controller with the
 * specified name.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class NoControllerFoundException extends RiplaException {

	public NoControllerFoundException(final String inControllerName) {
		super(String.format("No task provided for \"%s\"!", inControllerName));
	}

}

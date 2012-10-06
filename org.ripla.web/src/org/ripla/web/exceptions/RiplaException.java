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
 * Base class for exceptions thrown in the platform framework.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class RiplaException extends Exception {

	public RiplaException(final String inSimpleMessage) {
		super(inSimpleMessage);
	}

}

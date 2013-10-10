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
package org.ripla.rap.util;

import java.util.Locale;

import org.eclipse.rap.rwt.RWT;
import org.ripla.util.AbstractMessages;

/**
 * The base class for messages in a Ripla RAP application.
 * 
 * @author Luthiger
 */
public abstract class AbstractRapMessages extends AbstractMessages {

	@Override
	protected Locale getLocaleChecked() {
		return RWT.getLocale();
	}

}

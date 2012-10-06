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
package org.ripla.demo.skin;

import org.ripla.web.util.AbstractMessages;

/**
 * Bundle specific messages.
 * 
 * @author Luthiger
 */
public class Messages extends AbstractMessages {
	private static final String BASE_NAME = "messages"; //$NON-NLS-1$

	/* (non-Javadoc)
	 * @see org.ripla.web.util.AbstractMessages#getLoader()
	 */
	@Override
	protected ClassLoader getLoader() {
		return getClass().getClassLoader(); // NOPMD by Luthiger on 06.09.12 23:42
	}

	/* (non-Javadoc)
	 * @see org.ripla.web.util.AbstractMessages#getBaseName()
	 */
	@Override
	protected String getBaseName() {
		return BASE_NAME;
	}

}

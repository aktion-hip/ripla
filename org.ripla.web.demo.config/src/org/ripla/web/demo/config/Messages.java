/*******************************************************************************
 * Copyright (c) 2012-2013 RelationWare, Benno Luthiger
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * RelationWare, Benno Luthiger
 ******************************************************************************/
package org.ripla.web.demo.config;

import org.ripla.web.util.AbstractWebMessages;

/**
 * Bundle specific messages.
 * 
 * @author Luthiger
 */
public class Messages extends AbstractWebMessages {
	private static final String BASE_NAME = "messages"; //$NON-NLS-1$

	@Override
	protected ClassLoader getLoader() {
		return getClass().getClassLoader(); // NOPMD by Luthiger
	}

	@Override
	protected String getBaseName() {
		return BASE_NAME;
	}

}

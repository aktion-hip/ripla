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
package org.ripla.web.internal.services;

import org.ripla.web.services.IUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The service component consuming the <code>IUseCase</code> implementations.
 * 
 * @author Luthiger
 */
public class UseCaseComponent {
	private static final Logger LOG = LoggerFactory
			.getLogger(UseCaseComponent.class);

	/**
	 * Registers the specified use case.
	 * 
	 * @param inUseCase
	 *            {@link IUseCase}
	 */
	public void addUseCase(final IUseCase inUseCase) {
		LOG.debug("Added use case {}.", inUseCase);
		UseCaseRegistry.INSTANCE.addUseCase(inUseCase);
	}

	/**
	 * Unregisters the specified use case.
	 * 
	 * @param inUseCase
	 *            {@link IUseCase}
	 */
	public void removeUseCase(final IUseCase inUseCase) {
		LOG.debug("Removed use case {}.", inUseCase);
		UseCaseRegistry.INSTANCE.removeUseCase(inUseCase);
	}

}

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

import org.lunifera.runtime.web.vaadin.osgi.common.CustomOSGiUiProvider;
import org.lunifera.runtime.web.vaadin.osgi.common.IOSGiUiProviderFactory;

import com.vaadin.ui.UI;

/**
 * The provider (i.e. the OSGi DS implementation class) for the
 * <code>IOSGiUiProviderFactory</code> service. This class is responsible to
 * create instances of <code>RiplaUIProvider</code>.<br />
 * This factory is responsible to create the Ripla's UI provider class.
 * 
 * @author Luthiger
 * @see RiplaUIProvider
 */
public class RiplaUiProviderFactory implements IOSGiUiProviderFactory {

	@Override
	public CustomOSGiUiProvider createUiProvider(
			final String inVaadinApplication,
			final Class<? extends UI> inUiClass) {
		return new RiplaUIProvider(inVaadinApplication, inUiClass);
	}

}

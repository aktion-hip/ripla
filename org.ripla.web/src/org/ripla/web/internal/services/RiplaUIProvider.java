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
import org.ripla.web.Constants;
import org.ripla.web.RiplaApplication;

import com.vaadin.server.UICreateEvent;
import com.vaadin.ui.UI;

/**
 * The Ripla application's UI provider. We need this UI provider for theme
 * switching.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class RiplaUIProvider extends CustomOSGiUiProvider {
	private SkinRegistry skinRegistry;

	/**
	 * @param inVaadinApplication
	 * @param inUiClass
	 */
	public RiplaUIProvider(final String inVaadinApplication,
			final Class<? extends UI> inUiClass) {
		super(inVaadinApplication, inUiClass);
	}

	@Override
	public String getTheme(final UICreateEvent inEvent) {
		if (skinRegistry == null) {
			return Constants.DFT_SKIN_ID;
		}
		return skinRegistry.getActiveSkinService().getSkinID();
	}

	@Override
	public UI createInstance(final UICreateEvent inEvent) {
		@SuppressWarnings("restriction")
		final UI outUI = super.createInstance(inEvent);
		if (outUI instanceof RiplaApplication) {
			skinRegistry = ((RiplaApplication) outUI).getSkinRegistry();
		}
		return outUI;
	}

}
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

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.service.ResourceLoader;
import org.ripla.rap.services.ISkinService;

/**
 * Base class for skin services.
 * 
 * @author Luthiger
 */
public abstract class AbstractSkinService implements ISkinService {

	@Override
	public void handleStyle(final Application inApplication) {
		inApplication.addStyleSheet(RWT.DEFAULT_THEME_ID,
				getStyleSheetLocation(), new SkinResourceLoader(
						getSkinBundleClass()));
	}

	/**
	 * @return String the skin's style sheet location
	 */
	protected abstract String getStyleSheetLocation();

	protected abstract Class<? extends ISkinService> getSkinBundleClass();

	// ---

	private static class SkinResourceLoader implements ResourceLoader {
		private final Class<? extends ISkinService> bundleClass;

		public SkinResourceLoader(
				final Class<? extends ISkinService> inBundleClass) {
			bundleClass = inBundleClass;
		}

		@Override
		public InputStream getResourceAsStream(final String inResourceName)
				throws IOException {
			return bundleClass.getClassLoader().getResourceAsStream(
					inResourceName);
		}
	}

}

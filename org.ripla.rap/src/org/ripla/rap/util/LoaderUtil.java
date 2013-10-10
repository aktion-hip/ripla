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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.rap.rwt.service.ResourceLoader;
import org.ripla.rap.Constants;

/**
 * Helper class for resource loading.
 * 
 * @author Luthiger
 */
public final class LoaderUtil {

	private LoaderUtil() {
		// prevent instantiation
	}

	/**
	 * @return ResourceLoader the resource loader for the application's default
	 *         favicon <code>icons/favicon.png</code>
	 */
	public static ResourceLoader getDftFavion() {
		return new ResourceLoader() {
			@Override
			public InputStream getResourceAsStream(final String inResourceName)
					throws IOException {
				return getClass().getClassLoader().getResourceAsStream(
						Constants.FAVICON_PATH);
			}
		};
	}

	/**
	 * @param inResourceName
	 *            String
	 * @return {@link ResourceLoader} loader for the specified resource
	 */
	public static ResourceLoader createResourceLoader(
			final String inResourceName) {
		return new ResourceLoader() {
			@Override
			public InputStream getResourceAsStream(final String inResourceName)
					throws IOException {
				return getClass().getClassLoader().getResourceAsStream(
						inResourceName);
			}
		};
	}

	/**
	 * Reads the specified resource.
	 * 
	 * @param inResourceName
	 *            String path/name
	 * @param inCharset
	 *            String
	 * @return String the resource read from the specified path
	 */
	public static String readTextFromResource(final String inResourceName,
			final String inCharset) {
		final StringBuilder outResource = new StringBuilder();
		try {
			final InputStream lInputStream = LoaderUtil.class.getClassLoader()
					.getResourceAsStream(inResourceName);
			if (lInputStream == null) {
				throw new RuntimeException("Resource not found: "
						+ inResourceName);
			}
			try {
				final BufferedReader lReader = new BufferedReader(
						new InputStreamReader(lInputStream, inCharset));
				String lLine = lReader.readLine();
				while (lLine != null) {
					outResource.append(lLine);
					outResource.append('\n');
					lLine = lReader.readLine();
				}
			} finally {
				lInputStream.close();
			}
		} catch (final IOException exc) {
			throw new RuntimeException("Failed to read text from resource: "
					+ inResourceName);
		}
		return new String(outResource);
	}

}

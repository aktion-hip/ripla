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

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to load images.
 * 
 * @author Luthiger
 */
public final class ImageUtil {
	private static final Logger LOG = LoggerFactory.getLogger(ImageUtil.class);

	private static final String RESSOURCES_DIR = "resources/";

	private ImageUtil() {
		// prevent instantiation
	}

	/**
	 * Returns an image provided by the bundle of the specified class.
	 * 
	 * @param inDevice
	 *            {@link Device} <code>Composite.getDisplay()</code>
	 * @param inClass
	 *            {@link Class} an arbitrary class from the bundle
	 * @param inPath
	 *            String the relative path of the resource within the bundle's
	 *            <code>resources/</code> directory
	 * @return {@link Image}
	 */
	public static Image getImage(final Device inDevice, final Class<?> inClass,
			final String inPath) {
		return getImage(inDevice, RESSOURCES_DIR + inPath,
				inClass.getClassLoader());
	}

	/**
	 * Returns an image provided by the bundle of the specified class.
	 * 
	 * @param inDevice
	 *            {@link Device} <code>Composite.getDisplay()</code>
	 * @param inClass
	 *            {@link Class} an arbitrary class from the bundle
	 * @param inPath
	 *            String the relative path of the resource within the bundle
	 * @return {@link Image}
	 */
	public static Image getImageFrom(final Device inDevice,
			final Class<?> inClass, final String inPath) {
		return getImage(inDevice, inPath, ImageUtil.class.getClassLoader());
	}

	/**
	 * Returns an image provided by the <code>org.ripla.rap</code> bundle.
	 * 
	 * @param inDevice
	 *            {@link Device} <code>Composite.getDisplay()</code>
	 * @param inPath
	 *            String the relative path of the resource within the bundle's
	 *            <code>resources/</code> directory
	 * @return {@link Image}
	 */
	public static Image getImage(final Device inDevice, final String inPath) {
		return getImage(inDevice, RESSOURCES_DIR + inPath,
				ImageUtil.class.getClassLoader());
	}

	private static Image getImage(final Device inDevice, final String inPath,
			final ClassLoader inLoader) {
		final InputStream lInputStream = inLoader.getResourceAsStream(inPath);
		Image lResult = null;
		if (lInputStream != null) {
			try {
				lResult = new Image(inDevice, lInputStream);
			} finally {
				try {
					lInputStream.close();
				} catch (final IOException exc) {
					LOG.error(
							"Problem encountered while trying to load image resource!",
							exc);
				}
			}
		}
		return lResult;
	}

}

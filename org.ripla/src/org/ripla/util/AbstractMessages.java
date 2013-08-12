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
package org.ripla.util;

import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import org.ripla.interfaces.IMessages;

/**
 * <p>
 * Abstract class providing generic functionality for OSGi bundle specific
 * messages classes. Subclasses must specify the bundle's base name, e.g.
 * <code>messages</code> for the properties resources
 * <code>messages.properties</code> and <code>messages_de.properties</code>.
 * This name has to be set by the class' getBaseName() method.
 * </p>
 * <p>
 * The messages returned by the concrete subclasses are localized using the
 * actual <code>Locale</code> handled by the application.
 * </p>
 * <p>
 * To speed up access to the resource bundles, this class uses a cache. This,
 * however, implies that there is only one instance of this class per OSGi
 * bundle. This can be achieved by accessing the messages instance through the
 * bundles <code>Activator</code> class.
 * </p>
 * 
 * @author Luthiger
 */
public abstract class AbstractMessages implements IMessages {
	private final transient Map<String, ResourceBundle> bundleCache = new ConcurrentHashMap<String, ResourceBundle>();

	/**
	 * Provides the bundle's classloader to load the bundle specific messages.
	 * 
	 * <pre>
	 * getClass().getClassLoader()
	 * </pre>
	 * 
	 * @return ClassLoader
	 */
	protected abstract ClassLoader getLoader();

	/**
	 * Provides the base name (i.e. without i18n specific parts) of the file
	 * containing the bundle specific messages, i.e. <code>messages</code> for
	 * messages in file <code>messages.propertis</code> or
	 * <code>messages_de.properties</code>.
	 * 
	 * @return String
	 */
	protected abstract String getBaseName();

	@Override
	public final String getMessage(final String inKey) {
		try {
			return getBundle(getLocaleChecked()).getString(inKey);
		} catch (final MissingResourceException exc) {
			return '!' + inKey + '!';
		}
	}

	/**
	 * @return {@link Locale} application specific method to get the actual
	 *         local from the session
	 */
	abstract protected Locale getLocaleChecked();

	@Override
	public final String getFormattedMessage(final String inKey,
			final Object... inArgs) {
		try {
			return String.format(
					getBundle(getLocaleChecked()).getString(inKey), inArgs);
		} catch (final MissingResourceException exc) {
			return '!' + inKey + '!';
		}
	}

	private ResourceBundle getBundle(final Locale inLocale) {
		final String lLocale = inLocale.toString();
		ResourceBundle outBundle = bundleCache.get(lLocale);
		if (outBundle == null) {
			outBundle = ResourceBundle.getBundle(getBaseName(), inLocale,
					getLoader());
			bundleCache.put(lLocale, outBundle);
		}
		return outBundle;
	}

}

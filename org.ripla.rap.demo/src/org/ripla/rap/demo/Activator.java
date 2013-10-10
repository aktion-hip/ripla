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
package org.ripla.rap.demo;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.ripla.interfaces.IMessages;

/**
 * The bundle's activator.
 * 
 * @author Luthiger
 */
public class Activator implements BundleActivator {
	volatile private static IMessages cMessages; // NOPMD
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		cMessages = new Messages();
	}

	@Override
	public void stop(final BundleContext bundleContext) throws Exception {
		cMessages = null;
		Activator.context = null;
	}

	public static IMessages getMessages() {
		return cMessages == null ? new Messages() : cMessages;
	}

}

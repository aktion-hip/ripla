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
package org.ripla.web.demo.widgets;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.ripla.interfaces.IMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The bundle's activator.
 * 
 * @author Luthiger
 */
public class Activator implements BundleActivator {
	private final static Logger LOG = LoggerFactory.getLogger(Activator.class);
	volatile private static IMessages cMessages; // NOPMD by Luthiger

	@Override
	public void start(final BundleContext inContext) throws Exception { // NOPMD
		cMessages = new Messages();
		LOG.debug("{} started.", inContext.getBundle().getSymbolicName()); //$NON-NLS-1$
	}

	@Override
	public void stop(final BundleContext inContext) throws Exception { // NOPMD
		cMessages = null; // NOPMD by Luthiger on 06.09.12 23:51
		LOG.debug("{} stopped.", inContext.getBundle().getSymbolicName()); //$NON-NLS-1$
	}

	public static IMessages getMessages() {
		return cMessages == null ? new Messages() : cMessages;
	}

}

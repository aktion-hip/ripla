/*******************************************************************************
* Copyright (c) 2012 RelationWare, Benno Luthiger
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* RelationWare, Benno Luthiger
******************************************************************************/
package org.ripla.demo.skin.modest;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.ripla.web.interfaces.IMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The bundle's activator.
 * 
 * @author Luthiger
 */
public class Activator implements BundleActivator {
	private final static Logger LOG = LoggerFactory.getLogger(Activator.class);
	volatile private static IMessages cMessages;

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext inContext) throws Exception {
		cMessages = new Messages();
		LOG.debug("{} started.", inContext.getBundle().getSymbolicName()); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext inContext) throws Exception {
		cMessages = null;
		LOG.debug("{} stopped.", inContext.getBundle().getSymbolicName()); //$NON-NLS-1$
	}

	public static IMessages getMessages() {
		return cMessages != null ? cMessages : new Messages();
	}

}

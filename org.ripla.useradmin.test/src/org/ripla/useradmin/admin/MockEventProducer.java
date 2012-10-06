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

package org.ripla.useradmin.admin;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.useradmin.UserAdmin;
import org.ripla.useradmin.internal.UserAdminEventProducer;

/**
 * @author Luthiger
 */
public class MockEventProducer extends UserAdminEventProducer {

	/**
	 * @param inUserAdminRef
	 * @param inContext
	 */
	public MockEventProducer(
			final ServiceReference<? extends UserAdmin> inUserAdminRef,
			final BundleContext inContext) {
		super(inUserAdminRef, inContext);
	}

	public MockEventProducer() {
		super(null, new MockBundleContext());
	}

}

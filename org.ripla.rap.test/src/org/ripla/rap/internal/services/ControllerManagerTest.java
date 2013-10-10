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

package org.ripla.rap.internal.services;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.Bundle;
import org.ripla.interfaces.IControllerConfiguration;
import org.ripla.interfaces.IControllerSet;

/**
 * 
 * @author Luthiger
 */
@RunWith(MockitoJUnitRunner.class)
public class ControllerManagerTest {
	@Mock
	private IControllerSet controllerSet;
	@Mock
	private IControllerConfiguration controllerConfiguration;
	@Mock
	private Bundle bundle;

	private ControllerManager controllerManager;

	@Before
	public void setUp() {
		final IControllerConfiguration[] configs = new IControllerConfiguration[] { controllerConfiguration };
		when(controllerSet.getControllerConfigurations()).thenReturn(configs);
		when(controllerConfiguration.getBundle()).thenReturn(bundle);
		when(controllerConfiguration.getControllerName()).thenReturn(
				"hallo.velo");

		controllerManager = new ControllerManager();
	}

	/**
	 * Test method for
	 * {@link org.ripla.rap.internal.services.ControllerManager#addControllerSet(org.ripla.interfaces.IControllerSet)}
	 * .
	 */
	@Test
	public void testAddControllerSet() {
		controllerManager.addControllerSet(controllerSet);
	}

	/**
	 * Test method for
	 * {@link org.ripla.rap.internal.services.ControllerManager#getContent(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetContent() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.ripla.rap.internal.services.ControllerManager#removeControllerSet(org.ripla.interfaces.IControllerSet)}
	 * .
	 */
	@Test
	public void testRemoveControllerSet() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.ripla.rap.internal.services.ControllerManager#setEventAdmin(org.osgi.service.event.EventAdmin)}
	 * .
	 */
	@Test
	public void testSetEventAdmin() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.ripla.rap.internal.services.ControllerManager#getEventAdmin()}
	 * .
	 */
	@Test
	public void testGetEventAdmin() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.ripla.rap.internal.services.ControllerManager#setUserAdmin(org.osgi.service.useradmin.UserAdmin)}
	 * .
	 */
	@Test
	public void testSetUserAdmin() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link org.ripla.rap.internal.services.ControllerManager#getUserAdmin()}.
	 */
	@Test
	public void testGetUserAdmin() {
		fail("Not yet implemented");
	}

}

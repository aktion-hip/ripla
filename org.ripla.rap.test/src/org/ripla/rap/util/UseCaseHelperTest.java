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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import org.eclipse.swt.widgets.Composite;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleReference;
import org.osgi.service.useradmin.UserAdmin;
import org.ripla.exceptions.RiplaException;
import org.ripla.rap.interfaces.IPluggable;

/**
 * 
 * @author Luthiger
 */
@RunWith(MockitoJUnitRunner.class)
public class UseCaseHelperTest {

	@Mock
	Bundle bundle;

	@Test
	public void testCreateFullyQualifiedControllerName() {
		when(bundle.getSymbolicName()).thenReturn("org.ripla");

		assertEquals("Symbolic name", "org.ripla/org.ripla.web.test.mock",
				UseCaseHelper.createFullyQualifiedControllerName(bundle,
						"org.ripla.web.test.mock"));
	}

	@Test
	@Ignore
	public void testCreateFullyQualifiedControllerName2() throws Exception {
		final Bundle bundle = mock(Bundle.class);
		when(bundle.getSymbolicName()).thenReturn("org.ripla");

		final ClassLoader classLoader = mock(ClassLoader.class, withSettings()
				.extraInterfaces(BundleReference.class));
		when(((BundleReference) classLoader).getBundle()).thenReturn(bundle);

		@SuppressWarnings("unchecked")
		// we can't do this because mockito can not mock Class objects!
		final Class<IPluggable> clazz = mock(Class.class);
		when(clazz.getClassLoader()).thenReturn(classLoader);

		assertEquals("some/thing",
				UseCaseHelper.createFullyQualifiedControllerName(clazz));
	}

	@Test
	public void testEmpty() throws Exception {
		assertNotNull("Empty context menu set must not be null",
				UseCaseHelper.EMPTY_CONTEXT_MENU_SET);
		assertEquals("Wrong length", 0,
				UseCaseHelper.EMPTY_CONTEXT_MENU_SET.length);

		assertNotNull("Empty controller set must not be null",
				UseCaseHelper.EMPTY_CONTROLLER_SET);
		assertEquals("Wrong length", 0,
				UseCaseHelper.EMPTY_CONTROLLER_SET
						.getControllerConfigurations().length);
	}

	// ---

	@SuppressWarnings("unused")
	private static class TextController implements IPluggable {

		@Override
		public void setUserAdmin(final UserAdmin inUserAdmin) {
		}

		@Override
		public void setParent(final Composite inParent) {
		}

		@Override
		public Composite run() throws RiplaException {
			return null;
		}
	}

}

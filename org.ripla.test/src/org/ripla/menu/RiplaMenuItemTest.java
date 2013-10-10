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

package org.ripla.menu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * 
 * @author Luthiger
 */
public class RiplaMenuItemTest {

	@Test
	public void testRiplaMenuItemString() {
		final String lTitle = "My test item";
		final String lControllerName = "org.ripla.test/1";

		final RiplaMenuItem lMenuItem = new RiplaMenuItem(lTitle);
		lMenuItem.setControllerName(lControllerName);

		assertEquals(lTitle, lMenuItem.getLabel());
		assertEquals(0, lMenuItem.getPosition());
		assertEquals(lControllerName, lMenuItem.getControllerName());
		assertEquals(lControllerName, lMenuItem.getMenuCommand()
				.getControllerName());
		assertEquals("", lMenuItem.getPermission());
		assertTrue(lMenuItem.getSubMenu().isEmpty());
	}
}

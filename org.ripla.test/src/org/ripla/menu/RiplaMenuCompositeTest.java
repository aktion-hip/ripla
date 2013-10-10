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

import org.junit.Test;
import org.ripla.interfaces.IMenuItem;

/**
 * 
 * @author Luthiger
 */
public class RiplaMenuCompositeTest {

	/**
	 * Test method for
	 * {@link org.ripla.lMenuItem.RiplaMenuComposite#RiplaMenuComposite(java.lang.String)}
	 * .
	 */
	@Test
	public void testRiplaMenuCompositeString() {
		final String lTitle = "Top";
		final String lControllerName = "org.ripla.test/1";

		final RiplaMenuComposite lMenu = new RiplaMenuComposite(lTitle, 10); //$NON-NLS-1$
		lMenu.setControllerName(lControllerName);
		lMenu.setPermission("");

		RiplaMenuComposite lSubMenu = new RiplaMenuComposite("Sub 1", 10); //$NON-NLS-1$
		lSubMenu.setControllerName("org.ripla.rap/1");
		lMenu.add(lSubMenu);

		lSubMenu = new RiplaMenuComposite("Sub 2", 20); //$NON-NLS-1$
		lSubMenu.setControllerName("org.ripla.rap/2");
		lMenu.add(lSubMenu);

		lSubMenu = new RiplaMenuComposite("Sub 3", 30); //$NON-NLS-1$
		lSubMenu.setControllerName("org.ripla.rap/3");
		lMenu.add(lSubMenu);

		lSubMenu = new RiplaMenuComposite("Sub 4", 40); //$NON-NLS-1$
		lSubMenu.setControllerName("org.ripla.rap/4");
		lMenu.add(lSubMenu);

		lSubMenu = new RiplaMenuComposite("Sub 5", 50); //$NON-NLS-1$
		lSubMenu.setControllerName("org.ripla.rap/5");
		lMenu.add(lSubMenu);

		assertEquals(lTitle, lMenu.getLabel());
		assertEquals(10, lMenu.getPosition());
		assertEquals(lControllerName, lMenu.getControllerName());
		assertEquals(lControllerName, lMenu.getMenuCommand()
				.getControllerName());
		assertEquals("", lMenu.getPermission());

		int i = 0;
		for (final IMenuItem lSub : lMenu.getSubMenu()) {
			assertEquals("Sub " + (++i), lSub.getLabel());
			assertEquals("org.ripla.rap/" + i, lSub.getControllerName());
		}
		assertEquals(5, i);
	}
}

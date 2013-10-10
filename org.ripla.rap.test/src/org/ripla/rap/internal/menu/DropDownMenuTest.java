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

package org.ripla.rap.internal.menu;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.service.useradmin.Authorization;
import org.ripla.interfaces.IMenuItem;
import org.ripla.menu.RiplaMenuComposite;
import org.ripla.rap.DataHouskeeper;
import org.ripla.rap.internal.menu.DropDownMenu.IMenuItemsCreator;

/**
 * 
 * @author Luthiger
 */
@RunWith(MockitoJUnitRunner.class)
public class DropDownMenuTest {
	private static String[][] EXPECTED2 = new String[][] {
			{ "MenuItem {Sub 1}", "1956661274" },
			{ "MenuItem {Sub 2}", "1956661275" },
			{ "MenuItem {Sub 3}", "1956661276" },
			{ "MenuItem {Sub 4}", "1956661277" },
			{ "MenuItem {Sub 5}", "1956661273" } };
	private static String[][] EXPECTED3 = new String[][] {
			{ "MenuItem {Sub 1}", "1956661274" },
			{ "MenuItem {Sub 3}", "1956661276" },
			{ "MenuItem {Sub 4}", "1956661277" },
			{ "MenuItem {Sub 5}", "1956661273" } };

	@Mock
	private Authorization authorization;
	@Mock
	private SelectionAdapter listener;

	private Shell shell;
	private Menu menu;

	@Before
	public void setUp() {
		DataHouskeeper.createServiceContext();
		shell = new Shell(Display.getDefault());
		menu = new Menu(shell, SWT.NONE);
	}

	@SuppressWarnings("serial")
	@Test
	@Ignore
	public void testDropDownMenu() {
		final IMenuItemsCreator lCreator = DropDownMenu.getItemsCreator(
				createMenu("", ""), listener, authorization);
		final DropDownMenu lMenu = new DropDownMenu(shell, lCreator) {

		};
		System.out.println(lMenu);
	}

	@SuppressWarnings("serial")
	@Test
	public void testMenuItemsCreator() throws Exception {
		final IMenuItemsCreator lTest = DropDownMenu.getItemsCreator(
				createMenu("", ""), listener, authorization);
		lTest.create(menu);

		final MenuItem[] lItems = menu.getItems();
		int i = 0;
		for (final MenuItem lMenuItem : lItems) {
			assertItem(lMenuItem, EXPECTED2[i++]);
		}
		assertEquals(EXPECTED2.length, lItems.length);
	}

	@SuppressWarnings("serial")
	@Test
	public void testMenuItemsCreator2() throws Exception {
		// item 2 is displayed only with permission "test.sub"
		IMenuItemsCreator lTest = DropDownMenu.getItemsCreator(
				createMenu("test.top", "test.sub"), listener, authorization);
		lTest.create(menu);

		// no display of item 2
		MenuItem[] lItems = menu.getItems();
		int i = 0;
		for (final MenuItem lMenuItem : lItems) {
			assertItem(lMenuItem, EXPECTED3[i++]);
		}
		assertEquals(EXPECTED3.length, lItems.length);

		// still no display of item 2
		when(authorization.hasRole("test.top")).thenReturn(true);

		lTest = DropDownMenu.getItemsCreator(
				createMenu("test.top", "test.sub"), listener, authorization);
		menu = new Menu(shell, SWT.NONE);
		lTest.create(menu);
		lItems = menu.getItems();
		assertEquals(EXPECTED3.length, lItems.length);

		// try the correct permission now
		when(authorization.hasRole("test.sub")).thenReturn(true);

		lTest = DropDownMenu.getItemsCreator(
				createMenu("test.top", "test.sub"), listener, authorization);
		menu = new Menu(shell, SWT.NONE);
		lTest.create(menu);
		lItems = menu.getItems();
		i = 0;
		for (final MenuItem lMenuItem : lItems) {
			assertItem(lMenuItem, EXPECTED2[i++]);
		}
		assertEquals(EXPECTED2.length, lItems.length);
	}

	private void assertItem(final MenuItem inMenuItem, final String[] inExpected) {
		assertEquals(inExpected[0], inMenuItem.toString());
		assertEquals(inExpected[1], String.valueOf(inMenuItem.getID()));
	}

	// ---
	private IMenuItem createMenu(final String inPermissionTop,
			final String inPermissionSub) {
		final RiplaMenuComposite outMenu = new RiplaMenuComposite("Top", 10); //$NON-NLS-1$
		outMenu.setControllerName("org.ripla.rap/1");
		outMenu.setPermission(inPermissionTop);

		RiplaMenuComposite lSubMenu = new RiplaMenuComposite("Sub 1", 10); //$NON-NLS-1$
		lSubMenu.setControllerName("org.ripla.rap/2");
		outMenu.add(lSubMenu);

		lSubMenu = new RiplaMenuComposite("Sub 2", 20); //$NON-NLS-1$
		lSubMenu.setControllerName("org.ripla.rap/3");
		lSubMenu.setPermission(inPermissionSub);
		outMenu.add(lSubMenu);

		lSubMenu = new RiplaMenuComposite("Sub 3", 30); //$NON-NLS-1$
		lSubMenu.setControllerName("org.ripla.rap/4");
		outMenu.add(lSubMenu);

		lSubMenu = new RiplaMenuComposite("Sub 4", 40); //$NON-NLS-1$
		lSubMenu.setControllerName("org.ripla.rap/5");
		outMenu.add(lSubMenu);

		lSubMenu = new RiplaMenuComposite("Sub 5", 50); //$NON-NLS-1$
		lSubMenu.setControllerName("org.ripla.rap/1");
		outMenu.add(lSubMenu);

		return outMenu;
	}

}

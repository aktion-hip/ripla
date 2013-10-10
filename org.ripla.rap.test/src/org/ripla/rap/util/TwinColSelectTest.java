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

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.ripla.rap.DataHouskeeper;

/**
 * @author Luthiger
 */
@RunWith(MockitoJUnitRunner.class)
public class TwinColSelectTest {

	private Shell shell;

	@Before
	public void setUp() {
		DataHouskeeper.createServiceContext();
		shell = new Shell(Display.getDefault());
	}

	@Test
	public void testTwinColSelect() {
		final TwinColSelect lTester = new TwinColSelect(shell);

		// normal case
		final String[] lExpected = { "aaa", "bbb", "ccc", "eee", "mmm", "nnn" };
		final String[] lSorted = { "aaa", "bbb", "eee", "nnn" };
		final String[] lToAdd = { "ccc", "mmm" };

		final String[] lOut = lTester.addSorted(lSorted, lToAdd);
		assertArrays(lExpected, lOut);

		//
		final String[] lExpected2 = { "aaa", "bbb", "ccc", "eee", "mmm", "nnn",
				"ooo", "ppp", "qqq", "zzz" };
		final String[] lSorted2 = { "aaa", "ccc", "mmm", "ooo", "qqq" };
		final String[] lToAdd2 = { "bbb", "eee", "nnn", "ppp", "zzz" };
		assertArrays(lExpected2, lTester.addSorted(lSorted2, lToAdd2));

		final String[] lSorted3 = { "bbb", "eee", "nnn", "ppp", "zzz" };
		final String[] lToAdd3 = { "aaa", "ccc", "mmm", "ooo", "qqq" };
		assertArrays(lExpected2, lTester.addSorted(lSorted3, lToAdd3));

		// corner cases
		final String[] lSorted4 = { "aaa", "bbb", "ccc", "eee", "mmm" };
		final String[] lToAdd4 = { "nnn", "ooo", "ppp", "qqq", "zzz" };
		assertArrays(lExpected2, lTester.addSorted(lSorted4, lToAdd4));

		final String[] lSorted5 = { "nnn", "ooo", "ppp", "qqq", "zzz" };
		final String[] lToAdd5 = { "aaa", "bbb", "ccc", "eee", "mmm" };
		assertArrays(lExpected2, lTester.addSorted(lSorted5, lToAdd5));

		final String[] lSorted6 = { "aaa", "bbb", "ccc", "eee", "mmm", "nnn",
				"ooo", "ppp", "qqq", "zzz" };
		final String[] lToAdd6 = {};
		assertArrays(lExpected2, lTester.addSorted(lSorted6, lToAdd6));

		final String[] lSorted7 = {};
		final String[] lToAdd7 = { "aaa", "bbb", "ccc", "eee", "mmm", "nnn",
				"ooo", "ppp", "qqq", "zzz" };
		assertArrays(lExpected2, lTester.addSorted(lSorted7, lToAdd7));
	}

	private void assertArrays(final String[] inExpected, final String[] inTest) {
		assertEquals(inExpected.length, inTest.length);
		int i = 0;
		for (final String lVal : inTest) {
			assertEquals(inExpected[i++], lVal);
		}
	}

}

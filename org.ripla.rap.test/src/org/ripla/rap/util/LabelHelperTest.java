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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;
import org.ripla.rap.DataHouskeeper;

/**
 * 
 * @author Luthiger
 */
public class LabelHelperTest {

	private Shell shell;

	@Before
	public void setUp() {
		DataHouskeeper.createServiceContext();
		shell = new Shell(Display.getDefault());
	}

	@Test
	public void testCreateLabel() {
		Label lLabel = LabelHelper.createLabel(shell, "Text without markup!");
		assertEquals("Label {Text without markup!}", lLabel.toString());
		assertNull(lLabel.getData(RWT.CUSTOM_VARIANT));

		lLabel = LabelHelper.createLabel(shell,
				"Text <i>with</i> <b>markup</b>!");
		assertEquals("Label {Text <i>with</i> <b>markup</b>!}",
				lLabel.toString());
		assertNull(lLabel.getData(RWT.CUSTOM_VARIANT));

		try {
			lLabel = LabelHelper.createLabel(shell,
					"Text <i>with</i> incorrect <b>markup</p>!");
			fail("Should not get here!");

		} catch (final IllegalArgumentException exc) {
			// intentionally left empty
		}

		lLabel = LabelHelper.createLabel(shell, "Test 1", "test");
		assertEquals("test", lLabel.getData(RWT.CUSTOM_VARIANT));
	}

}

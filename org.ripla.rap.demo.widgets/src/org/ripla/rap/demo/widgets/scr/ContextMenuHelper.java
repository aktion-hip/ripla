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
package org.ripla.rap.demo.widgets.scr;

import org.ripla.interfaces.IMessages;
import org.ripla.rap.demo.widgets.Activator;
import org.ripla.rap.demo.widgets.Constants;
import org.ripla.rap.demo.widgets.controllers.ButtonWidgetsController;
import org.ripla.rap.demo.widgets.controllers.FormController;
import org.ripla.rap.demo.widgets.controllers.InputWidgetsController;
import org.ripla.rap.demo.widgets.controllers.SelectionWidgetsController;
import org.ripla.rap.demo.widgets.controllers.TableWidgetsController;
import org.ripla.rap.demo.widgets.controllers.TreeWidgetsController;
import org.ripla.rap.interfaces.IContextMenuItem;
import org.ripla.rap.interfaces.IMenuSet;
import org.ripla.rap.menu.ContextMenuItem;

/**
 * Helper class to create the widgets bundle's context menu.
 * 
 * @author Luthiger
 */
public final class ContextMenuHelper {
	private static final IMessages MESSAGES = Activator.getMessages();

	private static final IContextMenuItem INPUT_WIDGETS = new ContextMenuItem(
			InputWidgetsController.class,
			"widgets.menu.input", Constants.PERMISSION_INPUT_WIDGETS, MESSAGES); //$NON-NLS-1$
	private static final IContextMenuItem BUTTON_WIDGETS = new ContextMenuItem(
			ButtonWidgetsController.class,
			"widgets.menu.button", Constants.PERMISSION_BUTTON_WIDGETS, MESSAGES); //$NON-NLS-1$
	private static final IContextMenuItem SELECTION_WIDGETS = new ContextMenuItem(
			SelectionWidgetsController.class,
			"widgets.menu.selection", Constants.PERMISSION_SELECTION_WIDGETS, MESSAGES); //$NON-NLS-1$
	private static final IContextMenuItem FORM = new ContextMenuItem(
			FormController.class,
			"widgets.menu.form", Constants.PERMISSION_FORM, MESSAGES); //$NON-NLS-1$
	private static final IContextMenuItem TABLE_WIDGETS = new ContextMenuItem(
			TableWidgetsController.class,
			"widgets.menu.table", Constants.PERMISSION_TABLE_WIDGETS, MESSAGES); //$NON-NLS-1$
	private static final IContextMenuItem TREE_WIDGETS = new ContextMenuItem(
			TreeWidgetsController.class,
			"widgets.menu.tree", Constants.PERMISSION_TREE_WIDGETS, MESSAGES); //$NON-NLS-1$

	private ContextMenuHelper() {
		super();
	}

	public static IMenuSet createContextMenuSet() {
		return new IMenuSet() {
			@Override
			public String getSetID() {
				return Constants.CONTEXT_MENU_SET_WIDGETS;
			}

			@Override
			public IContextMenuItem[] getContextMenuItems() {
				return new IContextMenuItem[] { INPUT_WIDGETS, BUTTON_WIDGETS,
						SELECTION_WIDGETS, FORM, TABLE_WIDGETS, TREE_WIDGETS };
			}
		};
	}

}

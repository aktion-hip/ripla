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

package org.ripla.rap.demo.widgets.controllers;

import org.eclipse.swt.widgets.Composite;
import org.ripla.annotations.UseCaseController;
import org.ripla.exceptions.RiplaException;
import org.ripla.rap.controllers.AbstractController;
import org.ripla.rap.demo.widgets.Constants;
import org.ripla.rap.demo.widgets.views.TableWidgetsView;

/**
 * Controller for the RAP table widgets.
 * 
 * @author Luthiger
 */
@UseCaseController
public class TableWidgetsController extends AbstractController {

	@Override
	protected String needsPermission() {
		return Constants.PERMISSION_TABLE_WIDGETS;
	}

	@Override
	protected Composite runChecked() throws RiplaException {
		loadContextMenu(Constants.CONTEXT_MENU_SET_WIDGETS);
		return new TableWidgetsView(getParent());
	}

}

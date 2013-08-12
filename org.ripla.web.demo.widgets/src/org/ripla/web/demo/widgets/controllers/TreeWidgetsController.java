/*******************************************************************************
 * Copyright (c) 2012-2013 RelationWare, Benno Luthiger
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * RelationWare, Benno Luthiger
 ******************************************************************************/

package org.ripla.web.demo.widgets.controllers;

import org.ripla.annotations.UseCaseController;
import org.ripla.exceptions.RiplaException;
import org.ripla.web.controllers.AbstractController;
import org.ripla.web.demo.widgets.Constants;
import org.ripla.web.demo.widgets.data.CountryData;
import org.ripla.web.demo.widgets.data.CountryTree;
import org.ripla.web.demo.widgets.views.TreeWidgetsView;

import com.vaadin.ui.Component;

/**
 * Controller for the Vaadin tree widgets.
 * 
 * @author Luthiger
 */
@UseCaseController
public class TreeWidgetsController extends AbstractController {

	@Override
	protected String needsPermission() {
		return Constants.PERMISSION_TREE_WIDGETS;
	}

	@Override
	protected Component runChecked() throws RiplaException {
		loadContextMenu(Constants.CONTEXT_MENU_SET_WIDGETS);

		return new TreeWidgetsView(
				CountryTree.createContainer(CountryData.INSTANCE
						.getCountryContainer()));
	}

}

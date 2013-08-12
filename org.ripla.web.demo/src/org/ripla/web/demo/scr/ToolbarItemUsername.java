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

package org.ripla.web.demo.scr;

import org.osgi.service.useradmin.User;
import org.ripla.web.RiplaApplication;
import org.ripla.web.demo.Activator;
import org.ripla.web.interfaces.IToolbarActionListener;
import org.ripla.web.interfaces.IToolbarItemCreator;
import org.ripla.web.services.IToolbarItem;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * A provider for the <code>IToolbarItem</code> service. This toolbar item
 * displays the user's name.
 * 
 * @author Luthiger
 */
public class ToolbarItemUsername implements IToolbarItem {

	@Override
	public Component getComponent() {
		return null;
	}

	@Override
	public IToolbarItemCreator getCreator() {
		return new IToolbarItemCreator() {
			@Override
			public Component createToolbarItem(
					final RiplaApplication inApplication, final User inUser) {
				if (inUser == null) {
					return null;
				}

				final HorizontalLayout out = new HorizontalLayout();
				out.setSizeUndefined();
				out.addComponent(new Label(Activator.getMessages()
						.getFormattedMessage("toolbar.username.label",
								inUser.getName())));
				return out;
			}
		};
	}

	@Override
	public int getPosition() {
		return 5;
	}

	@Override
	public void registerToolbarActionListener(
			final IToolbarActionListener inListener) {
		// this item has no action
	}

}

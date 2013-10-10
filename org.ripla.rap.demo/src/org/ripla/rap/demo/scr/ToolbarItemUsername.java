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
package org.ripla.rap.demo.scr;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.osgi.service.useradmin.User;
import org.ripla.rap.demo.Activator;
import org.ripla.rap.interfaces.IBodyComponent;
import org.ripla.rap.interfaces.IToolbarActionListener;
import org.ripla.rap.interfaces.IToolbarItemCreator;
import org.ripla.rap.services.IToolbarItem;
import org.ripla.rap.util.ToolbarItemFactory;

/**
 * A provider for the <code>IToolbarItem</code> service. This toolbar item
 * displays the user's name.
 * 
 * @author Luthiger
 */
public class ToolbarItemUsername implements IToolbarItem {

	@Override
	public IToolbarItemCreator getCreator() {
		return new IToolbarItemCreator() {
			@Override
			public Control createToolbarItem(final Composite inToolbar,
					final IBodyComponent inBody, final User inUser) {
				if (inUser == null) {
					return null;
				}
				final Label out = new Label(
						ToolbarItemFactory.createItemHolder(inToolbar),
						SWT.NONE);
				out.setText(Activator.getMessages().getFormattedMessage(
						"toolbar.username.label", inUser.getName()));
				out.setData(RWT.CUSTOM_VARIANT, "ripla-toolbar-label");
				return out;
			}
		};
	}

	@Override
	public Control getComponent(final Composite inToolbar) {
		return null;
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

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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Link;
import org.osgi.service.useradmin.User;
import org.ripla.rap.app.RiplaApplication;
import org.ripla.rap.demo.Activator;
import org.ripla.rap.interfaces.IBodyComponent;
import org.ripla.rap.interfaces.IToolbarActionListener;
import org.ripla.rap.interfaces.IToolbarItemCreator;
import org.ripla.rap.services.IToolbarItem;
import org.ripla.rap.util.ToolbarItemFactory;

/**
 * A provider for the <code>IToolbarItem</code> service. This toolbar item
 * displays the logout link.
 * 
 * @author Luthiger
 */
public class ToolbarItemLogout implements IToolbarItem {

	@Override
	public IToolbarItemCreator getCreator() {
		return new IToolbarItemCreator() {
			@Override
			public Control createToolbarItem(final Composite inToolbar,
					final IBodyComponent inBody, final User inUser) {
				if (inUser == null) {
					return null;
				}
				return getComponent(inToolbar);
			}
		};
	}

	@SuppressWarnings("serial")
	@Override
	public Control getComponent(final Composite inToolbar) {
		final Composite lHolder = ToolbarItemFactory
				.createItemHolder(inToolbar);
		lHolder.setData(RWT.CUSTOM_VARIANT, "ripla-toolbar-item-logout");
		final Link out = new Link(lHolder, SWT.NONE);
		out.setText(String.format("<a href=\"\">%s</a>", Activator
				.getMessages().getMessage("toolbar.logout.label")));
		out.setData(RWT.CUSTOM_VARIANT, "ripla-toolbar-label");
		out.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent inEvent) {
				RiplaApplication.restart();
			}
		});
		return out;
	}

	@Override
	public int getPosition() {
		return 4;
	}

	@Override
	public void registerToolbarActionListener(
			final IToolbarActionListener inListener) {
		// nothing to do
	}

}

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
import org.ripla.web.interfaces.IToolbarAction;
import org.ripla.web.interfaces.IToolbarActionListener;
import org.ripla.web.interfaces.IToolbarItemCreator;
import org.ripla.web.services.IToolbarItem;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.BaseTheme;

/**
 * A provider for the <code>IToolbarItem</code> service. This toolbar item
 * displays the logout link.
 * 
 * @author Luthiger
 */
public class ToolbarItemLogout implements IToolbarItem {

	private transient IToolbarActionListener listener;

	@SuppressWarnings("serial")
	@Override
	public Component getComponent() {
		final HorizontalLayout out = new HorizontalLayout();
		out.setSizeUndefined();
		final Button lLogout = new Button(Activator.getMessages().getMessage(
				"toolbar.logout.label"));
		lLogout.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(final ClickEvent inEvent) {
				if (inEvent.getButton() != lLogout) {
					return;
				}

				if (listener != null) {
					listener.processAction(new IToolbarAction() {
						@Override
						public void run() {
							// TODO
							// inApplication.getMainWindow().getApplication()
							// .close();
						}
					});
				}
			}
		});
		lLogout.setStyleName(BaseTheme.BUTTON_LINK);
		out.addComponent(lLogout);
		return out;
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
				return getComponent();
			}
		};
	}

	@Override
	public int getPosition() {
		return 4;
	}

	@Override
	public void registerToolbarActionListener(
			final IToolbarActionListener inListener) {
		listener = inListener;
	}

}

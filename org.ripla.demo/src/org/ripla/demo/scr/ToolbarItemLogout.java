/*******************************************************************************
* Copyright (c) 2012 RelationWare, Benno Luthiger
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* RelationWare, Benno Luthiger
******************************************************************************/

package org.ripla.demo.scr;

import org.osgi.service.event.EventAdmin;
import org.osgi.service.useradmin.User;
import org.ripla.demo.Activator;
import org.ripla.web.RiplaApplication;
import org.ripla.web.interfaces.IToolbarAction;
import org.ripla.web.interfaces.IToolbarActionListener;
import org.ripla.web.interfaces.IToolbarItemCreator;
import org.ripla.web.services.IToolbarItem;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.BaseTheme;

/**
 * A provider for the <code>IToolbarItem</code> service.
 * This toolbar item displays the logout link.
 * 
 * @author Luthiger
 */
public class ToolbarItemLogout implements IToolbarItem {

	private IToolbarActionListener listener;

	/* (non-Javadoc)
	 * @see org.ripla.web.services.IToolbarItem#getComponent()
	 */
	@SuppressWarnings("serial")
	@Override
	public Component getComponent() {
		HorizontalLayout out = new HorizontalLayout();
		out.setSizeUndefined();
		final Button lLogout = new Button(Activator.getMessages().getMessage("toolbar.logout.label"));
		lLogout.addListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent inEvent) {
				if (inEvent.getButton() != lLogout) {
					return;
				}
					
				if (listener != null) {
					listener.processAction(new IToolbarAction() {
						@Override
						public void run(RiplaApplication inApplication, EventAdmin inEventAdmin) {
							inApplication.getMainWindow().getApplication().close();
						}
					});
				}
			}
		});
		lLogout.setStyleName(BaseTheme.BUTTON_LINK);
		out.addComponent(lLogout);
		return out;
	}

	/* (non-Javadoc)
	 * @see org.ripla.web.services.IToolbarItem#getCreator()
	 */
	@Override
	public IToolbarItemCreator getCreator() {
		return new IToolbarItemCreator() {
			@Override
			public Component createToolbarItem(RiplaApplication inApplication, User inUser) {
				if (inUser == null) {
					return null;
				}
				return getComponent();
			}
		};
	}

	/* (non-Javadoc)
	 * @see org.ripla.web.services.IToolbarItem#getPosition()
	 */
	@Override
	public int getPosition() {
		return 4;
	}

	/* (non-Javadoc)
	 * @see org.ripla.web.services.IToolbarItem#registerToolbarActionListener(org.ripla.web.interfaces.IToolbarActionListener)
	 */
	@Override
	public void registerToolbarActionListener(IToolbarActionListener inListener) {
		listener = inListener;
	}

}

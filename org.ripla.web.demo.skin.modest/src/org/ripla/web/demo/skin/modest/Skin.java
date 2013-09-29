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
package org.ripla.web.demo.skin.modest;

import org.ripla.web.services.ISkin;
import org.ripla.web.util.FooterHelper;
import org.ripla.web.util.LabelHelper;

import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * The modest demo skin.
 * 
 * @author Luthiger
 */
public class Skin implements ISkin {

	private final transient HorizontalLayout menuBarMedium;
	private final transient HorizontalLayout menuBar;

	public Skin() {
		menuBar = new HorizontalLayout();
		menuBar.setWidth("100%"); //$NON-NLS-1$
		menuBar.setHeight(39, Unit.PIXELS);
		menuBar.setStyleName("modest-menubar");

		menuBarMedium = new HorizontalLayout();
		menuBarMedium.setStyleName("ripla-menubar"); //$NON-NLS-1$
		menuBarMedium.setWidth("100%"); //$NON-NLS-1$
		menuBarMedium.setHeight(39, Unit.PIXELS);

		menuBarMedium.addComponent(menuBar);
		menuBarMedium.setExpandRatio(menuBar, 1);
	}

	@Override
	public boolean hasHeader() {
		return true;
	}

	@Override
	public Component getHeader(final String inAppName) {
		final HorizontalLayout out = new HorizontalLayout();
		out.setStyleName("demo-header"); //$NON-NLS-1$
		out.setWidth("100%"); //$NON-NLS-1$
		out.setHeight(65, Unit.PIXELS);

		final Label lTitle = LabelHelper.createLabel(
				"Ripla Demo [with modest skin]", "demo-header-text");
		lTitle.setSizeUndefined();
		out.addComponent(lTitle);

		return out;
	}

	@Override
	public boolean hasFooter() {
		return true;
	}

	@Override
	public Component getFooter() {
		final FooterHelper out = FooterHelper
				.createFooter(FooterHelper.DFT_FOOTER_TEXT);
		out.setHeight(27);
		out.setStyleName("demo-footer");
		return out;
	}

	@Override
	public boolean hasToolBar() {
		return true;
	}

	@Override
	public boolean hasMenuBar() {
		return true;
	}

	@Override
	public Label getToolbarSeparator() {
		final Label outSeparator = new Label("|", ContentMode.HTML); //$NON-NLS-1$
		outSeparator.setSizeUndefined();
		return outSeparator;
	}

	@Override
	public HorizontalLayout getMenuBar() {
		return menuBar;
	}

	@Override
	public HorizontalLayout getMenuBarMedium() {
		return menuBarMedium;
	}

	@Override
	public Resource getSubMenuIcon() {
		return null;
	}

}

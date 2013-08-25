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
package org.ripla.web.demo.skin.stylish;

import org.ripla.web.services.ISkin;
import org.ripla.web.util.FooterHelper;
import org.ripla.web.util.LabelHelper;

import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * The stylish demo skin.
 * 
 * @author Luthiger
 */
public class Skin implements ISkin {

	private final transient HorizontalLayout menuBarComponent;
	private final transient HorizontalLayout menuBarLayout;

	public Skin() {
		menuBarLayout = new HorizontalLayout();
		menuBarLayout.setWidth("100%"); //$NON-NLS-1$
		menuBarLayout.setHeight(45, Unit.PIXELS);
		menuBarLayout.setStyleName("stylish-menubar");

		menuBarComponent = new HorizontalLayout();
		menuBarComponent.setStyleName("ripla-menubar"); //$NON-NLS-1$
		menuBarComponent.setWidth("100%"); //$NON-NLS-1$
		menuBarComponent.setHeight(45, Unit.PIXELS);

		menuBarComponent.addComponent(createMargin("stylish-menubar-left", 6));
		menuBarComponent.addComponent(menuBarLayout);
		menuBarComponent.setExpandRatio(menuBarLayout, 1);
		menuBarComponent
				.addComponent(createMargin("stylish-menubar-right", 11));
	}

	private Label createMargin(final String inStyle, final int inWidth) {
		final Label out = new Label("&#160;", ContentMode.HTML);
		out.setStyleName(inStyle);
		out.setWidth(inWidth, Unit.PIXELS);
		out.setHeight(45, Unit.PIXELS);
		return out;
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
		out.setHeight(50, Unit.PIXELS);

		final Label lTitle = LabelHelper.createLabel(
				"Ripla Demo [with stylish skin]", "demo-header-text");
		lTitle.setSizeUndefined();
		out.addComponent(lTitle);
		out.setComponentAlignment(lTitle, Alignment.MIDDLE_CENTER);

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
		out.setHeight(19);
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
		return menuBarLayout;
	}

	@Override
	public HorizontalLayout getMenuBarMedium() {
		return menuBarComponent;
	}

	@Override
	public Resource getSubMenuIcon() {
		return null;
	}

}

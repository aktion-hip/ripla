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
package org.ripla.rap.demo.skin.stylish;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.ripla.rap.services.ISkin;
import org.ripla.rap.util.GridLayoutHelper;
import org.ripla.rap.util.LabelHelper;

/**
 * The stylish demo skin.
 * 
 * @author Luthiger
 */
public class Skin implements ISkin {
	private static final int HEADER_HEIGHT = 50;
	private static final int FOOTER_HEIGHT = 19;

	@Override
	public boolean hasHeader() {
		return true;
	}

	@Override
	public Composite getHeader(final Composite inParent, final String inAppName) {
		final Composite outHeader = new Composite(inParent, SWT.NONE);
		outHeader.setData(RWT.CUSTOM_VARIANT, "stylish-header");
		outHeader.setBackgroundMode(SWT.INHERIT_DEFAULT);

		final GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		outHeader.setLayout(layout);

		final GridData layoutData = new GridData(SWT.DEFAULT, HEADER_HEIGHT);
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.horizontalAlignment = GridData.FILL;
		outHeader.setLayoutData(layoutData);

		final Composite lTextCell = new Composite(outHeader, SWT.NONE);
		lTextCell.setLayout(new GridLayout());
		lTextCell
				.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));

		LabelHelper.createLabel(lTextCell,
				"Ripla RAP Demo [with stylish skin]", "stylish-header-text");

		return outHeader;
	}

	@Override
	public boolean hasFooter() {
		return true;
	}

	@Override
	public Composite getFooter(final Composite inParent) {
		final Composite outFooter = new Composite(inParent, SWT.NONE);
		outFooter.setBackgroundMode(SWT.INHERIT_DEFAULT);
		outFooter.setData(RWT.CUSTOM_VARIANT, "demo-footer");

		final GridLayout lLayout = new GridLayout();
		lLayout.marginWidth = 0;
		lLayout.marginHeight = 0;
		outFooter.setLayout(lLayout);

		final GridData lLayoutData = new GridData(SWT.DEFAULT, FOOTER_HEIGHT);
		lLayoutData.grabExcessHorizontalSpace = true;
		lLayoutData.horizontalAlignment = GridData.FILL;
		outFooter.setLayoutData(lLayoutData);

		final Composite lTextCell = new Composite(outFooter, SWT.NONE);
		lTextCell.setLayout(lLayout);
		lTextCell
				.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));

		LabelHelper.createLabel(lTextCell, "@ RelationWare",
				"demo-footer-text", 10);

		return outFooter;
	}

	@Override
	public boolean hasToolBar() {
		return true;
	}

	@Override
	public Label getToolbarSeparator(final Composite inParent) {
		final Label out = LabelHelper.createLabel(inParent, "&#160;|");
		out.setData(RWT.CUSTOM_VARIANT, "demo-toolbar-sep");
		return out;
	}

	@Override
	public boolean hasMenuBar() {
		return true;
	}

	@Override
	public Composite getMenuBarMedium(final Composite inParent) {
		final Composite lHolder = new Composite(inParent, SWT.NONE);
		final GridLayout lLayout = GridLayoutHelper.createGridLayout();
		lLayout.numColumns = 3;
		lLayout.marginWidth = 0;
		lLayout.horizontalSpacing = 0;
		lHolder.setLayout(lLayout);
		final GridData lLayoutData = new GridData(SWT.DEFAULT, 45);
		lLayoutData.grabExcessHorizontalSpace = true;
		lLayoutData.horizontalAlignment = GridData.FILL;
		lHolder.setLayoutData(lLayoutData);

		createMargin(lHolder, "stylish-menubar-left", 6);
		final Composite out = new Composite(lHolder, SWT.NONE);
		createMargin(lHolder, "stylish-menubar-right", 11);

		out.setLayout(GridLayoutHelper.createGridLayout());
		out.setData(RWT.CUSTOM_VARIANT, "stylish-menubar");
		out.setLayoutData(lLayoutData);
		return out;
	}

	private void createMargin(final Composite inParent, final String inStyle,
			final int inWidth) {
		final Label out = LabelHelper.createLabel(inParent, "&#160;");
		out.setLayoutData(new GridData(inWidth, 45));
		out.setData(RWT.CUSTOM_VARIANT, inStyle);
	}

	@Override
	public Composite getMenuBar(final Composite inParent) {
		final Composite lFill = new Composite(inParent, SWT.NONE);
		lFill.setLayout(GridLayoutHelper.createGridLayout());

		final Composite out = new Composite(lFill, SWT.BORDER);
		final RowLayout lLayout = new RowLayout(SWT.HORIZONTAL);
		lLayout.marginTop = 0;
		out.setLayout(lLayout);
		out.setLayoutData(GridLayoutHelper.createFillLayoutData());
		out.setData(RWT.CUSTOM_VARIANT, "stylish-menubar");

		return out;
	}

	@Override
	public Image getSubMenuIcon() {
		return null;
	}

}

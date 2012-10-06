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

package org.ripla.web.internal.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.ripla.web.services.ISkin;
import org.ripla.web.util.LabelHelper;
import org.ripla.web.util.PreferencesHelper;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;

/**
 * Helper class for managing <code>org.ripla.web.services.ISkin</code>
 * instances.
 * 
 * @author Luthiger
 */
public final class SkinRegistry {
	public static final String DFT_SKIN_ID = "org.ripla.web.skin";

	private final transient Collection<ISkin> skins = Collections
			.synchronizedList(new ArrayList<ISkin>());
	private transient ISkin activeSkin = null;

	private final transient PreferencesHelper preferences;
	private transient String dftSkinID = DFT_SKIN_ID;

	/**
	 * SkinRegistry constructor.
	 * 
	 * @param inPreferences
	 *            {@link PreferencesHelper}
	 */
	public SkinRegistry(final PreferencesHelper inPreferences) {
		preferences = inPreferences;
		skins.add(new RiplaSkin());
	}

	/**
	 * Registering a new skin. Called by the component's bind method.
	 * 
	 * @param inSkin
	 *            {@link ISkin}
	 */
	public void registerSkin(final ISkin inSkin) {
		skins.add(inSkin);
	}

	/**
	 * Unregistering a skin. Called by the component's unbind method.
	 * 
	 * @param inSkin
	 *            {@link ISkin}
	 */
	public void unregisterSkin(final ISkin inSkin) {
		skins.remove(inSkin);
	}

	/**
	 * Returns the active skin (according to the settings in the application
	 * preferences).
	 * 
	 * @return {@link ISkin} the active skin, may be <code>null</code>, if no
	 *         skins are registered
	 */
	public ISkin getActiveSkin() {
		if (activeSkin == null) {
			final String lSkinID = preferences.getActiveSkinID();
			if (lSkinID == null) {
				return calculateSkin(dftSkinID);
			}

			activeSkin = calculateSkin(lSkinID);
		}
		return activeSkin;
	}

	private ISkin calculateSkin(final String inSkinID) {
		for (final ISkin lSkin : skins) {
			if (lSkin.getSkinID().equals(inSkinID)) {
				return lSkin;
			}
		}
		return skins.iterator().next();
	}

	/**
	 * Notify the registry about the new skin selection.
	 * 
	 * @param inSkinID
	 *            String the new skin's id
	 */
	public void changeSkin(final String inSkinID) {
		preferences.set(PreferencesHelper.KEY_SKIN, inSkinID);
		activeSkin = calculateSkin(inSkinID);
	}

	/**
	 * Sets the application's default skin.
	 * 
	 * @param inDftSkinID
	 *            String the default skin's ID
	 */
	public void setDefaultSkin(final String inDftSkinID) {
		dftSkinID = inDftSkinID;
	}

	// ---

	/**
	 * The Ripla default skin.
	 * 
	 * @author Luthiger
	 */
	private static class RiplaSkin implements ISkin {

		@Override
		public String getSkinID() {
			return DFT_SKIN_ID;
		}

		@Override
		public String getSkinName() {
			return "Ripla Default Skin";
		}

		@Override
		public boolean hasHeader() {
			return true;
		}

		@Override
		public Component getHeader() {
			final Layout outHeader = new HorizontalLayout();
			outHeader.setStyleName("ripla-header"); //$NON-NLS-1$
			outHeader.setWidth("100%"); //$NON-NLS-1$
			outHeader.setHeight(70, Sizeable.UNITS_PIXELS);
			outHeader.addComponent(LabelHelper.createLabel("header",
					"ripla-header-text"));
			return outHeader;
		}

		@Override
		public boolean hasFooter() {
			return true;
		}

		@Override
		public Component getFooter() {
			final Layout outFooter = new HorizontalLayout();
			outFooter.setStyleName("ripla-footer"); //$NON-NLS-1$
			outFooter.setWidth("100%"); //$NON-NLS-1$
			outFooter.setHeight(18, Sizeable.UNITS_PIXELS);
			outFooter.addComponent(LabelHelper.createLabel("footer",
					"ripla-footer-text"));
			return outFooter;
		}

		@Override
		public boolean hasToolBar() {
			return true;
		}

		@Override
		public Label getToolbarSeparator() {
			final Label outSeparator = new Label("&bull;", Label.CONTENT_XHTML); //$NON-NLS-1$
			outSeparator.setSizeUndefined();
			return outSeparator;
		}

		@Override
		public boolean hasMenuBar() {
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.ripla.web.services.ISkin#getMenuBarLayout()
		 */
		@Override
		public HorizontalLayout getMenuBarLayout() {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.ripla.web.services.ISkin#getMenuBarComponent()
		 */
		@Override
		public HorizontalLayout getMenuBarComponent() {
			return null;
		}
	}

}

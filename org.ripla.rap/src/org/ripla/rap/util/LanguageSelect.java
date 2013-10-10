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
package org.ripla.rap.util;

import java.util.HashMap;
import java.util.Locale;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.osgi.service.useradmin.User;
import org.ripla.interfaces.IRiplaEventDispatcher;
import org.ripla.interfaces.IRiplaEventDispatcher.Event;
import org.ripla.rap.Activator;
import org.ripla.rap.Constants;
import org.ripla.rap.app.RiplaBase;
import org.ripla.rap.interfaces.IToolbarAction;
import org.ripla.rap.interfaces.IToolbarActionListener;
import org.ripla.rap.internal.services.ConfigManager;
import org.ripla.util.PreferencesHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Language selection component.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class LanguageSelect extends Composite {
	private static final Logger LOG = LoggerFactory
			.getLogger(LanguageSelect.class);

	private IToolbarActionListener listener;

	/**
	 * LanguageSelect constructor.
	 * 
	 * @param inParent
	 *            {@link Composite}
	 * @param inPreferences
	 *            {@link PreferencesHelper}
	 * @param inConfigManager
	 *            {@link ConfigManager}
	 * @param inUser
	 *            {@link User}
	 */
	public LanguageSelect(final Composite inParent,
			final PreferencesHelper inPreferences,
			final ConfigManager inConfigManager, final User inUser) {
		super(inParent, SWT.NONE);

		setLayout(GridLayoutHelper.createGridLayout());
		setData(RWT.CUSTOM_VARIANT, "ripla-language-holder");

		final Composite lHolder = new Composite(this, SWT.NONE);
		lHolder.setData(RWT.CUSTOM_VARIANT, "ripla-language-toolitem"); //$NON-NLS-1$
		final GridLayout lLayout = GridLayoutHelper.createGridLayout();
		lLayout.numColumns = 2;
		lHolder.setLayout(lLayout);
		lHolder.setLayoutData(new GridData(SWT.DEFAULT,
				RiplaBase.DFT_TOOLBAR_HEIGHT - 1));
		final Composite lLabelHolder = ToolbarItemFactory
				.createItemHolder(lHolder);
		lLabelHolder.setData(RWT.CUSTOM_VARIANT, "ripla-toolbar-item-language");
		final Label lLabel = new Label(lLabelHolder, SWT.NONE);
		lLabel.setText(Activator.getMessages().getMessage(
				"toolbar.label.language"));
		lLabel.setData(RWT.CUSTOM_VARIANT, "ripla-language-label");

		// select
		final String lActiveLanguage = RWT.getLocale().getLanguage();
		final Combo lSelect = createSelect(lHolder, Constants.LANGUAGES,
				lActiveLanguage);
		lSelect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent inEvent) {
				final Locale lOld = RWT.getLocale();
				final String lNew = lSelect.getItem(lSelect.getSelectionIndex());
				if (lOld == null || !lNew.equals(lOld.getLanguage())) {
					LOG.trace("Setting language preference to {}.", lNew);
					final Locale lLocale = Constants.LANGUAGES[lSelect
							.getSelectionIndex()];
					RWT.setLocale(lLocale);
					if (inUser == null) {
						inPreferences.setLocale(lLocale);
					} else {
						inPreferences.setLocale(lLocale, inUser);
					}
					if (listener != null) {
						listener.processAction(new IToolbarAction() {
							@Override
							public void run() {
								((IRiplaEventDispatcher) RWT.getUISession()
										.getAttribute(
												Constants.RS_EVENT_DISPATCHER))
										.dispatch(Event.REFRESH,
												new HashMap<String, Object>());
							}
						});
					}
				}
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent inEvent) {
				this.widgetSelected(inEvent);
			}
		});
	}

	private Combo createSelect(final Composite inParent,
			final Locale[] inLanguages, final String inActiveLanguage) {
		final Combo outCombo = new Combo(inParent, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		// items
		final String[] lLanguages = new String[inLanguages.length];
		int i = 0;
		int lActive = 0;
		for (final Locale lLocale : inLanguages) {
			if (inActiveLanguage.equals(lLocale.getLanguage())) {
				lActive = i;
			}
			lLanguages[i++] = lLocale.getLanguage();
		}
		outCombo.setItems(lLanguages);
		outCombo.select(lActive);
		// layout
		outCombo.setData(RWT.CUSTOM_VARIANT, "ripla-language-select"); //$NON-NLS-1$
		outCombo.setLayout(GridLayoutHelper.createGridLayout());
		outCombo.setLayoutData(new GridData(SWT.DEFAULT,
				RiplaBase.DFT_TOOLBAR_HEIGHT - 3));
		return outCombo;
	}

	/**
	 * @param inListeners
	 *            IToolbarActionListener the listener to actions on the toolbar
	 *            item
	 */
	public void setListener(final IToolbarActionListener inListener) {
		listener = inListener;
	}

	/**
	 * Sets the language selections widget's height.
	 * 
	 * @param inHeight
	 *            int the component's height in pixels
	 */
	public void setHeight(final int inHeight) {
		setLayoutData(new GridData(SWT.DEFAULT, inHeight));
		for (final Control lChild : getChildren()) {
			final Object lChildLayout = lChild.getLayoutData();
			if (lChildLayout instanceof GridData) {
				lChild.setLayoutData(new GridData(
						((GridData) lChildLayout).widthHint, inHeight));
			}
		}
		pack();
	}
}

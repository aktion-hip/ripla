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

package org.ripla.web.util;

import java.util.HashMap;
import java.util.Locale;

import org.osgi.service.useradmin.User;
import org.ripla.interfaces.IRiplaEventDispatcher;
import org.ripla.util.PreferencesHelper;
import org.ripla.web.Activator;
import org.ripla.web.Constants;
import org.ripla.web.interfaces.IToolbarAction;
import org.ripla.web.interfaces.IToolbarActionListener;
import org.ripla.web.internal.services.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * Language selection component.
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public final class LanguageSelect extends CustomComponent {
	private static final Logger LOG = LoggerFactory
			.getLogger(LanguageSelect.class);

	private final HorizontalLayout layout;
	private IToolbarActionListener listener;

	private final ComboBox select;

	/**
	 * LanguageSelect constructor.
	 * 
	 * @param inPreferences
	 *            {@link PreferencesHelper}
	 * @param inConfigManager
	 *            {@link ConfigManager}
	 * @param inUser
	 *            {@link User}
	 */
	public LanguageSelect(final PreferencesHelper inPreferences,
			final ConfigManager inConfigManager, final User inUser) {
		super();
		// initialize language form prefs (1) or config admin (2)
		// final String lActiveLanguage = inPreferences.getLocale(inUser,
		// new Locale(inConfigManager.getLanguage())).getLanguage();

		setStyleName("ripla-language-select"); //$NON-NLS-1$
		setSizeUndefined();

		layout = new HorizontalLayout();
		setCompositionRoot(layout);
		layout.setHeight(22, Unit.PIXELS);
		layout.setSpacing(true);

		final Label lLabel = new Label(Activator.getMessages().getMessage(
				"toolbar.label.language"), ContentMode.HTML); //$NON-NLS-1$
		lLabel.setStyleName("ripla-toolbar-label"); //$NON-NLS-1$
		lLabel.setSizeUndefined();
		layout.addComponent(lLabel);
		layout.setComponentAlignment(lLabel, Alignment.MIDDLE_LEFT);
		layout.setExpandRatio(lLabel, 1);

		select = createSelect();
		select.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(final ValueChangeEvent inEvent) {
				final Locale lNew = ((LocaleAdapter) select.getValue())
						.getLocale();
				final Locale lOld = VaadinSession.getCurrent().getLocale();
				if (!lOld.equals(lNew)) {
					VaadinSession.getCurrent().setLocale(lNew);
					if (listener != null) {
						listener.processAction(new IToolbarAction() {
							@Override
							public void run() {
								LOG.trace("Setting language preference to {}.",
										lNew.getLanguage());
								VaadinSession
										.getCurrent()
										.getAttribute(
												IRiplaEventDispatcher.class)
										.dispatch(
												org.ripla.interfaces.IRiplaEventDispatcher.Event.REFRESH,
												new HashMap<String, Object>());
							}
						});
					}
				}
			}
		});
		layout.addComponent(select);
	}

	/**
	 * @param inHeight
	 *            int the component's height in pixels
	 */
	public void setHeight(final int inHeight) {
		layout.setHeight(inHeight, Unit.PIXELS);
	}

	/**
	 * @param inListeners
	 *            IToolbarActionListener the listener to actions on the toolbar
	 *            item
	 */
	public void setListener(final IToolbarActionListener inListener) {
		listener = inListener;
	}

	private ComboBox createSelect() {
		final ComboBox outSelect = new ComboBox(null);
		outSelect.setStyleName("ripla-select"); //$NON-NLS-1$
		outSelect.setWidth(55, Unit.PIXELS);
		outSelect.setNullSelectionAllowed(false);
		outSelect.setImmediate(true);
		return outSelect;
	}

	@Override
	public void attach() {
		super.attach();

		final LanguagesContainer lLanguages = LanguagesContainer.getLanguages(
				Constants.LANGUAGES, getSession().getLocale().getLanguage());
		select.setContainerDataSource(lLanguages);
		select.select(lLanguages.getActiveLanguage());
	}

	// ---

	private static class LanguagesContainer extends
			BeanItemContainer<LocaleAdapter> {
		private LocaleAdapter activeLanguage;

		private LanguagesContainer() {
			super(LocaleAdapter.class);
		}

		protected static LanguagesContainer getLanguages(
				final Locale[] inLanguages, final String inActiveLanguage) {
			final LanguagesContainer out = new LanguagesContainer();
			for (final Locale lLocale : inLanguages) {
				final LocaleAdapter lWrapped = new LocaleAdapter(lLocale); // NOPMD
				out.addItem(lWrapped);
				if (inActiveLanguage.equals(lLocale.getLanguage())) {
					out.setActiveLanguage(lWrapped);
				}
			}
			return out;
		}

		private void setActiveLanguage(final LocaleAdapter inActiveLanguage) {
			activeLanguage = inActiveLanguage;
		}

		protected LocaleAdapter getActiveLanguage() {
			return activeLanguage;
		}
	}

	private static class LocaleAdapter {
		private final Locale locale;

		LocaleAdapter(final Locale inLocale) {
			locale = inLocale;
		}

		protected Locale getLocale() {
			return locale;
		}

		@Override
		public String toString() {
			return locale.getLanguage();
		}
	}

}

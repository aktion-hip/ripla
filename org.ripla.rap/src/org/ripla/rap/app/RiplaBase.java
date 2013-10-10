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

package org.ripla.rap.app;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.useradmin.Authorization;
import org.osgi.service.useradmin.User;
import org.ripla.exceptions.NoControllerFoundException;
import org.ripla.interfaces.IMenuCommand;
import org.ripla.rap.Constants;
import org.ripla.rap.interfaces.IBodyComponent;
import org.ripla.rap.interfaces.IPluggable;
import org.ripla.rap.interfaces.IRapConfiguration;
import org.ripla.rap.interfaces.IToolbarAction;
import org.ripla.rap.interfaces.IToolbarActionListener;
import org.ripla.rap.interfaces.IToolbarItemCreator;
import org.ripla.rap.internal.menu.DropDownMenu;
import org.ripla.rap.internal.menu.MenuFactory;
import org.ripla.rap.internal.services.ConfigManager;
import org.ripla.rap.internal.services.RiplaEventDispatcher;
import org.ripla.rap.internal.services.SkinRegistry;
import org.ripla.rap.internal.services.ToolbarItemRegistry;
import org.ripla.rap.internal.services.UseCaseRegistry;
import org.ripla.rap.internal.views.DefaultRiplaView;
import org.ripla.rap.internal.views.RiplaLogin;
import org.ripla.rap.services.ISkin;
import org.ripla.rap.services.IToolbarItem;
import org.ripla.rap.util.GridLayoutHelper;
import org.ripla.rap.util.ToolbarItemFactory;
import org.ripla.util.ParameterObject;
import org.ripla.util.PreferencesHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The base entry point of a RAP application based on Ripla.<br />
 * This class is instantiated once for every user/session.
 * 
 * @author Luthiger
 */
public class RiplaBase extends AbstractEntryPoint implements IBodyComponent {
	private static final Logger LOG = LoggerFactory.getLogger(RiplaBase.class);

	// the width of the scrollable view
	private static final int CONTENT_MIN_WIDTH = 1500;
	// the height of the scrollable view
	private static final int CONTENT_MIN_HEIGHT = 700;
	private static final int MENUBAR_HEIGTH = 38;
	// the default height of the toolbar
	public static final int DFT_TOOLBAR_HEIGHT = 22; // 22

	private IRapConfiguration appConfig;
	private ToolbarItemFactory toolbarItemFactory;
	private PreferencesHelper preferences;

	private ScrolledComposite scrolledArea;
	private Composite bodyView;
	private ContentView contentView;
	private ContextView contextView;
	private IMenuCommand defaultMenuCmd;
	private RiplaEventDispatcher eventDispatcher;

	@Override
	protected Shell createShell(final Display inDisplay) {

		final Shell outShell = super.createShell(inDisplay);
		outShell.setData(RWT.CUSTOM_VARIANT, "ripla-main");
		return outShell;
	}

	@Override
	protected void createContents(final Composite inParent) {
		eventDispatcher = new RiplaEventDispatcher();
		eventDispatcher.setBodyComponent(this);
		RWT.getUISession().setAttribute(Constants.RS_EVENT_DISPATCHER,
				eventDispatcher);

		// retrieve application attributes
		preferences = retrieveAttribute(PreferencesHelper.class,
				RiplaApplication.AN_PREFS);
		RWT.setLocale(preferences.getLocale(RWT.getLocale()));
		RWT.getUISession().setAttribute(Constants.RS_PREFS, preferences);
		final ConfigManager lConfigManager = retrieveAttribute(
				ConfigManager.class, RiplaApplication.AN_CONFIG);

		appConfig = retrieveAttribute(IRapConfiguration.class,
				RiplaApplication.AN_APP_CONFIG);

		toolbarItemFactory = new ToolbarItemFactory(preferences,
				lConfigManager, null);

		createRiplaView(inParent, appConfig);
	}

	private void createRiplaView(final Composite inParent,
			final IRapConfiguration inConfiguration) {
		inParent.setLayout(new FillLayout());
		scrolledArea = createScrolledArea(inParent);
		bodyView = createContent(scrolledArea, inConfiguration);
		scrolledArea.setContent(bodyView);

		showDefault();
	}

	@Override
	public void refreshBody() {
		bodyView.dispose();

		final ISkin lSkin = SkinRegistry.INSTANCE.getActiveSkin();
		// see method createContent()
		bodyView = new Composite(scrolledArea, SWT.NONE);
		bodyView.setLayout(GridLayoutHelper.createGridLayout());
		createAppContent(lSkin, bodyView);
		if (lSkin.hasFooter()) {
			final Composite footer = createComposite(bodyView,
					GridLayoutHelper.createFillLayoutData());
			lSkin.getFooter(footer);
		}

		scrolledArea.setContent(bodyView);
		bodyView.pack();

		showDefault();
	}

	private <T> T retrieveAttribute(final Class<T> inType,
			final String inAttrName) {
		final Object obj = RWT.getApplicationContext().getAttribute(inAttrName);
		try {
			return inType.cast(obj);
		} catch (final ClassCastException exc) {
			LOG.error("Error encounteres while retrieving application attribute!");
		}
		return null;
	}

	private ScrolledComposite createScrolledArea(final Composite inParent) {
		final ScrolledComposite outScrolledComp = new ScrolledComposite(
				inParent, SWT.V_SCROLL | SWT.H_SCROLL);
		outScrolledComp.setMinSize(CONTENT_MIN_WIDTH, CONTENT_MIN_HEIGHT);
		outScrolledComp.setExpandVertical(true);
		outScrolledComp.setExpandHorizontal(true);
		return outScrolledComp;
	}

	private Composite createContent(final ScrolledComposite inParent,
			final IRapConfiguration inConfiguration) {
		final ISkin lSkin = SkinRegistry.INSTANCE.getActiveSkin();

		final Composite out = new Composite(inParent, SWT.NONE);
		out.setLayout(GridLayoutHelper.createGridLayout());

		if (inConfiguration.getLoginAuthenticator() == null
				|| RWT.getUISession().getAttribute(Constants.RS_USER) != null) {
			createAppContent(lSkin, out);
		} else {
			createLoginView(out, inConfiguration, lSkin);
		}

		if (lSkin.hasFooter()) {
			final Composite footer = createComposite(out,
					GridLayoutHelper.createFillLayoutData());
			lSkin.getFooter(footer);
		}
		return out;
	}

	/**
	 * Creates the application's login view.<br />
	 * Subclasses may override.
	 * 
	 * @param inParent
	 *            {@link Composite}
	 * @param inConfiguration
	 *            {@link IRapConfiguration} the application's configuration
	 *            object
	 * @param inSkin
	 *            {@link ISkin} the actual application skin
	 */
	protected void createLoginView(final Composite inParent,
			final IRapConfiguration inConfiguration, final ISkin inSkin) {
		final Composite lTop = createComposite(inParent,
				GridLayoutHelper.createFillLayoutData());
		inSkin.getHeader(lTop, appConfig.getAppName());

		final Composite lContent = createComposite(inParent, new GridData(
				GridData.FILL_BOTH));

		new RiplaLogin(lContent, inConfiguration,
				UseCaseRegistry.INSTANCE.getUserAdmin(), this);
	}

	@Override
	public void showAfterLogin(final User inUser) {
		toolbarItemFactory.setUser(inUser);
		RWT.getUISession().setAttribute(Constants.RS_USER, inUser);
		RWT.getUISession().setAttribute(Constants.RS_LOCALE,
				preferences.getLocale(inUser, RWT.getLocale()));
		refreshBody();
	}

	private void createAppContent(final ISkin inSkin, final Composite inParent) {
		// top consists of header, toolbar, menubar
		final Composite lTop = createComposite(inParent,
				GridLayoutHelper.createFillLayoutData());
		inSkin.getHeader(lTop, appConfig.getAppName());
		if (inSkin.hasToolBar()) {
			createToolbar(lTop, inSkin);
		}
		if (inSkin.hasMenuBar()) {
			createMenubar(lTop, inSkin);
		}

		// then create page area beneath the top, i.e. the content area
		final Composite lContent = createComposite(inParent, new GridData(
				GridData.FILL_BOTH));

		final SashForm lContentSash = new SashForm(lContent, SWT.HORIZONTAL
				| SWT.SMOOTH);
		lContentSash.setLayoutData(new GridData(GridData.FILL_BOTH));
		lContentSash.setData(RWT.CUSTOM_VARIANT, "ripla-sash");

		final Composite lContentLeft = createComposite(lContentSash,
				new GridData(GridData.FILL_BOTH));
		lContentLeft.setData(RWT.CUSTOM_VARIANT, "ripla-context-pane");
		contextView = new ContextView(lContentLeft);

		final Composite lContentRight = createComposite(lContentSash,
				new GridData(GridData.FILL_BOTH));
		lContentRight.setData(RWT.CUSTOM_VARIANT, "ripla-content-pane");
		contentView = new ContentView(lContentRight);

		lContentSash.setWeights(new int[] { 1, 6 });
	}

	@Override
	public void showDefault() {
		if (defaultMenuCmd != null) {
			try {
				setContentView(defaultMenuCmd.getControllerName());
			} catch (final NoControllerFoundException exc) {
				handleNoTaskFound(exc);
			}
		}
	}

	private Composite createMenubar(final Composite inParent, final ISkin inSkin) {
		Composite out = inSkin.getMenuBarMedium(inParent);
		if (out != null) {
			// skin.menuBar contains menu items
			final Composite lMenuBar = inSkin.getMenuBar(out);
			if (lMenuBar != null) {
				createMenuControls(lMenuBar, inSkin.getSubMenuIcon());
				return out;
			}
			// skin.menuBarMedium contains menu items
			createMenuControls(out, inSkin.getSubMenuIcon());
			return out;
		}

		// skin.menuBar contains menu items
		out = inSkin.getMenuBar(inParent);
		// dft.menuBar contains menu items
		if (out == null) {
			out = createDftMenuBar(inParent);
		}
		createMenuControls(out, inSkin.getSubMenuIcon());
		return out;
	}

	@Override
	public void setContentView(final String inControllerName)
			throws NoControllerFoundException {
		contentView.recreate(inControllerName);
	}

	@Override
	public void setContentView(final NoControllerFoundException inExc) {
		handleNoTaskFound(inExc);
	}

	@Override
	public void setContextMenu(final String inMenuSetName,
			final Class<? extends IPluggable> inControllerClass)
			throws NoControllerFoundException {
		contextView.recreate(inMenuSetName, inControllerClass);
	}

	protected void handleNoTaskFound(final NoControllerFoundException inExc) {
		LOG.error("Configuration error:", inExc); //$NON-NLS-1$
		contentView.showDft(inExc);
	}

	@SuppressWarnings("serial")
	private void createMenuControls(final Composite inParent,
			final Image inMenuIcon) {

		final SelectionListener lMenuSelectionListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent inEvent) {
				final Object lAction = inEvent.widget
						.getData(MenuFactory.KEY_MENU_ACTION);
				if (lAction instanceof IMenuCommand) {
					afterMenuClick();
					try {
						setContentView(((IMenuCommand) lAction)
								.getControllerName());
					} catch (final NoControllerFoundException exc) {
						handleNoTaskFound(exc);
					}
				}
			}
		};

		final Authorization lAuthorization = UseCaseRegistry.INSTANCE
				.getUserAdmin().getAuthorization(getUser());
		// the menu map is used for that the selected menu can be marked
		final Map<String, DropDownMenu> lMenuMap = new HashMap<String, DropDownMenu>();
		for (final MenuFactory lFactory : UseCaseRegistry.INSTANCE.getMenus()) {
			final DropDownMenu lMenu = lFactory.createMenu(inParent,
					inMenuIcon, lMenuSelectionListener, lAuthorization);
			if (lMenu != null) {
				lMenuMap.put(lFactory.getProviderSymbolicName(), lMenu);
				if (defaultMenuCmd == null) {
					defaultMenuCmd = lMenu.getDefaultCmd();
				}
			}
		}
		RWT.getUISession().setAttribute(Constants.RS_MENU_MAP, lMenuMap);
	}

	private User getUser() {
		return (User) RWT.getUISession().getAttribute(Constants.RS_USER);
	}

	/**
	 * Hook for subclasses to provide functionality to be processed after the
	 * user clicked a menu item.
	 * <p>
	 * Subclasses may override.
	 * </p>
	 * 
	 */
	protected void afterMenuClick() {
		// do nothing
	}

	/**
	 * @return {@link Composite} a menu bar composite having
	 *         <code>RowLayout(HORIZONTAL)</code>.
	 */
	private Composite createDftMenuBar(final Composite inParent) {
		final Composite lHolder = new Composite(inParent, SWT.NONE);
		lHolder.setLayout(GridLayoutHelper.createGridLayout());
		lHolder.setData(RWT.CUSTOM_VARIANT, Constants.CSS_MENU_BAR);
		final GridData lLayoutData = new GridData(SWT.DEFAULT, MENUBAR_HEIGTH);
		lLayoutData.grabExcessHorizontalSpace = true;
		lLayoutData.horizontalAlignment = GridData.FILL;
		lHolder.setLayoutData(lLayoutData);

		final Composite lFill = new Composite(lHolder, SWT.NONE);
		lFill.setLayout(GridLayoutHelper.createGridLayout());
		lFill.setLayoutData(GridLayoutHelper.createFillLayoutData());

		final Composite out = new Composite(lFill, SWT.BORDER);
		final RowLayout lLayout = new RowLayout(SWT.HORIZONTAL);
		lLayout.marginTop = 0;
		out.setLayout(lLayout);
		out.setLayoutData(GridLayoutHelper.createFillLayoutData());
		out.setData(RWT.CUSTOM_VARIANT, Constants.CSS_MENU_HOLDER);
		return out;
	}

	/**
	 * @return {@link Composite} a toolbar composite having
	 *         <code>RowLayout(HORIZONTAL)</code>.
	 */
	private Composite createToolbar(final Composite inParent, final ISkin inSkin) {
		final Composite lHolder = new Composite(inParent, SWT.NONE);
		lHolder.setLayout(GridLayoutHelper.createGridLayout());
		lHolder.setLayoutData(GridLayoutHelper.createFillLayoutData());
		lHolder.setData(RWT.CUSTOM_VARIANT, "ripla-toolbar-holder");

		final Composite out = new Composite(lHolder, SWT.RIGHT);
		out.setData(RWT.CUSTOM_VARIANT, "ripla-toolbar");
		final RowLayout lLayout = new RowLayout(SWT.HORIZONTAL);
		lLayout.marginTop = 0;
		out.setLayout(lLayout);
		final GridData lLayoutData = new GridData(SWT.END, SWT.CENTER, true,
				true);
		lLayoutData.heightHint = DFT_TOOLBAR_HEIGHT;
		out.setLayoutData(lLayoutData);
		toolbarItemFactory.setParent(out);

		final Collection<IToolbarItem> lItems = ToolbarItemRegistry.INSTANCE
				.getSortedItems();
		Label lLastSep = null;
		for (final IToolbarItem lItem : lItems) {
			final IToolbarItemCreator lFactory = lItem.getCreator();
			final Control lComponent = lFactory == null ? lItem
					.getComponent(out) : lFactory.createToolbarItem(out, this,
					getUser());
			if (lComponent == null) {
				continue;
			}

			lItem.registerToolbarActionListener(new IToolbarActionListener() { // NOPMD
				@Override
				public void processAction(final IToolbarAction inAction) {
					inAction.run();
				}
			});

			lLastSep = inSkin.getToolbarSeparator(out);
		}
		if (lLastSep != null) {
			lLastSep.dispose();
		}

		return out;
	}

	private Composite createComposite(final Composite inParent,
			final GridData inLayoutData) {
		final Composite out = new Composite(inParent, SWT.NONE);
		out.setLayout(GridLayoutHelper.createGridLayout());
		out.setLayoutData(inLayoutData);
		return out;
	}

	/**
	 * Returns the parameter object to pass context parameters that can be
	 * evaluated to decide whether a context menu item should be displayed or
	 * hidden.<br />
	 * By default, an empty parameter object is created.<br />
	 * Subclasses may override.
	 * 
	 * @return {@link ParameterObject}
	 */
	protected ParameterObject createParametersForContextMenu() {
		return new ParameterObject();
	}

	/**
	 * The factory method to create a toolbar component instance. <br />
	 * Toolbar items created with this factory must have a constructor with the
	 * following parameters:
	 * <ul>
	 * <li>org.eclipse.swt.widgets.Composite</li>
	 * <li>org.ripla.util.PreferencesHelper</li>
	 * <li>org.ripla.rap.internal.services.ConfigManager</li>
	 * <li>org.osgi.service.useradmin.User</li>
	 * </ul>
	 * 
	 * @param inClass
	 *            Class the toolbar component class
	 * @return {@link Composite} the created toolbar component instance or
	 *         <code>null</code> in case of an error
	 */
	@Override
	public <T extends Composite> T createToolbarItem(final Class<T> inClass) {
		try {
			return toolbarItemFactory.createToolbarComponent(inClass);
		} catch (final Exception exc) {
			LOG.error("Error encountered while creating the toolbar item!", exc);
		}
		return null;
	}

	// --
	/**
	 * Subclasses may override to provide their own
	 * <code>PreferencesHelper</code>.
	 * 
	 * @return PreferencesHelper
	 */
	protected PreferencesHelper createPreferencesHelper() {
		return new PreferencesHelper();
	}

	private static abstract class ViewHandler {
		private final Composite parent;
		private Composite delegate;

		protected ViewHandler(final Composite inParent) {
			parent = inParent;
			delegate = createDelegate(parent);
		}

		private Composite createDelegate(final Composite inParent) {
			final Composite out = new Composite(inParent, SWT.NONE);
			out.setData(RWT.CUSTOM_VARIANT, getStyle());
			out.setLayout(new GridLayout());
			out.setLayoutData(GridLayoutHelper.createFillLayoutData());
			return out;
		}

		abstract protected String getStyle();

		protected void recreate(final String inName)
				throws NoControllerFoundException {
			delegate.dispose();
			delegate = createDelegate(parent);
			manageView(inName, delegate);
			delegate.pack();
		}

		protected void showDft(final Exception inExc) {
			delegate.dispose();
			delegate = createDelegate(parent);
			new DefaultRiplaView(delegate, inExc);
			delegate.pack();
		}

		abstract protected void manageView(String inControllerName,
				Composite inParent) throws NoControllerFoundException;
	}

	private static class ContentView extends ViewHandler {
		protected ContentView(final Composite inParent) {
			super(inParent);
		}

		@Override
		protected void manageView(final String inControllerName,
				final Composite inParent) throws NoControllerFoundException {
			UseCaseRegistry.INSTANCE.getControllerManager().getContent(
					inControllerName, inParent);
		}

		@Override
		protected String getStyle() {
			return "ripla-content-view";
		}
	}

	private class ContextView extends ViewHandler {
		private Class<? extends IPluggable> controllerClass;

		protected ContextView(final Composite inParent) {
			super(inParent);
		}

		protected void recreate(final String inName,
				final Class<? extends IPluggable> inControllerClass)
				throws NoControllerFoundException {
			controllerClass = inControllerClass;
			super.recreate(inName);
		}

		@Override
		protected void manageView(final String inMenuSetName,
				final Composite inParent) throws NoControllerFoundException {
			final User lUser = (User) RWT.getUISession().getAttribute(
					Constants.RS_USER);
			UseCaseRegistry.INSTANCE.getContextMenuManager().renderContextMenu(
					inParent,
					inMenuSetName,
					lUser,
					UseCaseRegistry.INSTANCE.getUserAdmin().getAuthorization(
							null), createParametersForContextMenu(),
					controllerClass);
		}

		@Override
		protected String getStyle() {
			return "ripla-context-view";
		}
	}

}

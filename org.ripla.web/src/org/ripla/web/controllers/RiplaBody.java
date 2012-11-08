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

package org.ripla.web.controllers; // NOPMD by Luthiger on 09.09.12 00:48

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.osgi.service.useradmin.Authorization;
import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;
import org.ripla.web.RiplaApplication;
import org.ripla.web.exceptions.NoControllerFoundException;
import org.ripla.web.interfaces.IBodyComponent;
import org.ripla.web.interfaces.IMenuCommand;
import org.ripla.web.interfaces.IPluggable;
import org.ripla.web.interfaces.IToolbarAction;
import org.ripla.web.interfaces.IToolbarActionListener;
import org.ripla.web.interfaces.IToolbarItemCreator;
import org.ripla.web.internal.menu.MenuFactory;
import org.ripla.web.internal.services.ApplicationData;
import org.ripla.web.internal.services.ToolbarItemRegistry;
import org.ripla.web.internal.services.UseCaseManager;
import org.ripla.web.internal.views.DefaultRiplaView;
import org.ripla.web.services.ISkin;
import org.ripla.web.services.IToolbarItem;
import org.ripla.web.util.ParameterObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.terminal.Resource;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.VerticalLayout;

/**
 * <p>
 * The application's body component consisting of the toolbar, the menubar, the
 * sidebar panel and the main content panel.
 * </p>
 * <p>
 * Subclasses may override the following methods:
 * <ul>
 * <li><code>RiplaBody.{@link #initializeLayout()}</code></li>
 * <li><code>RiplaBody.{@link #createParametersForContextMenu()}</code></li>
 * <li><code>RiplaBody.{@link #setContextMenu(String)}</code></li>
 * <li><code>RiplaBody.{@link #afterMenuClick()}</code></li>
 * <li><code>RiplaBody.{@link #showDefault()}</code></li>
 * <li><code>RiplaBody.{@link #getDftMenuBarLayout()}</code></li>
 * </ul>
 * </p>
 * 
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class RiplaBody extends CustomComponent implements IBodyComponent { // NOPMD
	public static final Logger LOG = LoggerFactory.getLogger(RiplaBody.class);

	private Map<Integer, IMenuCommand> menuMap;

	private final VerticalLayout layout;
	private VerticalLayout sidebar;
	private VerticalLayout content;

	private final UseCaseManager useCaseManager;
	private final ISkin skin;
	private final ToolbarItemRegistry toolbarRegistry;
	private final RiplaApplication application;

	private MenuBar menuBar;

	/**
	 * Private constructor.
	 * 
	 * @param inSkin
	 *            {@link ISkin}
	 * @param inToolbarRegistry
	 *            {@link ToolbarItemRegistry}
	 * @param inUseCaseManager
	 *            {@link UseCaseManager}
	 * @param inApplication
	 *            {@link RiplaApplication}
	 */
	private RiplaBody(final ISkin inSkin,
			final ToolbarItemRegistry inToolbarRegistry,
			final UseCaseManager inUseCaseManager,
			final RiplaApplication inApplication) {
		super();

		useCaseManager = inUseCaseManager;
		skin = inSkin;
		toolbarRegistry = inToolbarRegistry;
		application = inApplication;

		setSizeFull();

		layout = new VerticalLayout();
		setCompositionRoot(layout);
		layout.setStyleName("ripla-body");
		layout.setSizeFull();
		layout.removeAllComponents();
	}

	/**
	 * Factory method to create an initialized instance of {@link RiplaBody}.
	 * 
	 * @param inSkin
	 *            {@link ISkin}
	 * @param inToolbarRegistry
	 *            {@link ToolbarItemRegistry}
	 * @param inUseCaseManager
	 *            {@link UseCaseManager}
	 * @param inApplication
	 *            {@link RiplaApplication}
	 * @return {@link RiplaBody}
	 */
	public static RiplaBody createInstance(final ISkin inSkin,
			final ToolbarItemRegistry inToolbarRegistry,
			final UseCaseManager inUseCaseManager,
			final RiplaApplication inApplication) {
		final RiplaBody outBody = new RiplaBody(inSkin, inToolbarRegistry,
				inUseCaseManager, inApplication);
		outBody.initializeLayout();
		return outBody;
	}

	/**
	 * Arranges the body window's views (i.e. <code>header</code>,
	 * <code>footer</code>, <code>toolbar</code>, <code>menubar</code>,
	 * <code>sidebar</code>, <code>content</code>).<br />
	 * Subclasses may override for a different arrangement of the views.
	 */
	protected void initializeLayout() {
		if (skin.hasHeader()) {
			final Component lHeader = skin.getHeader(application.getAppName());
			layout.addComponent(lHeader);
			layout.setExpandRatio(lHeader, 0);
		}

		if (skin.hasToolBar()) {
			final Component lToolbar = createToolbar(toolbarRegistry,
					skin.getToolbarSeparator());
			layout.addComponent(lToolbar);
		}

		if (skin.hasMenuBar()) {
			final Component lMenubar = createMenubar(
					skin.getMenuBarComponent(), skin.getMenuBarLayout(),
					skin.getSubMenuIcon());
			layout.addComponent(lMenubar);
		}

		final HorizontalSplitPanel lPanel = new HorizontalSplitPanel();
		lPanel.setSplitPosition(10, Sizeable.UNITS_PERCENTAGE);
		lPanel.setHeight(SIZE_UNDEFINED, 0);
		lPanel.setStyleName("ripla-split"); //$NON-NLS-1$

		sidebar = new VerticalLayout();
		sidebar.setStyleName("ripla-sidebar");
		sidebar.setSizeFull();
		lPanel.setFirstComponent(sidebar);

		content = new VerticalLayout();
		content.setStyleName("ripla-content");
		lPanel.setSecondComponent(content);
		content.setMargin(true);
		lPanel.setSizeFull();

		layout.addComponent(lPanel);
		layout.setExpandRatio(lPanel, 1);
	}

	protected final UseCaseManager getUseCaseManager() {
		return useCaseManager;
	}

	protected final UserAdmin getUserAdmin() {
		return useCaseManager.getUserAdmin();
	}

	protected final Layout getSidebar() {
		return sidebar;
	}

	private Component createMenubar(final HorizontalLayout inComponent,
			final HorizontalLayout inMenuBarLayout, final Resource inSubMenuIcon) {
		final HorizontalLayout outComponent = inComponent == null ? getDftMenuBarLayout()
				: inComponent;

		menuBar = new MenuBar();
		menuBar.setAutoOpen(true);
		menuBar.setStyleName("ripla-menu"); //$NON-NLS-1$
		createMenu(menuBar, inSubMenuIcon);
		if (inMenuBarLayout == null) {
			outComponent.addComponent(menuBar);
		} else {
			inMenuBarLayout.removeAllComponents();
			inMenuBarLayout.addComponent(menuBar);
		}

		return outComponent;
	}

	/**
	 * Returns the default menu bar's layout component.<br />
	 * Subclasses may override.
	 * 
	 * @return {@link HorizontalLayout} the default layout for the menu bar.
	 */
	protected HorizontalLayout getDftMenuBarLayout() {
		final HorizontalLayout outLayout = new HorizontalLayout();
		outLayout.setStyleName("ripla-menubar"); //$NON-NLS-1$
		outLayout.setMargin(false, false, false, true);
		outLayout.setWidth("100%"); //$NON-NLS-1$
		outLayout.setHeight(32, UNITS_PIXELS);
		return outLayout;
	}

	private void createMenu(final MenuBar inMenuBar,
			final Resource inSubMenuIcon) {
		final MenuBar.Command lCommand = new MenuBar.Command() {
			@Override
			public void menuSelected(final MenuItem inSelected) {
				final IMenuCommand lAction = getMenuMap().get(
						inSelected.getId());
				afterMenuClick(getUseCaseManager());
				if (lAction != null) {
					try {
						setContentView(getContentComponent(lAction
								.getControllerName()));
					}
					catch (final NoControllerFoundException exc) {
						handleNoTaskFound(exc);
					}
				}
			}
		};

		final Authorization lAuthorization = useCaseManager.getUserAdmin()
				.getAuthorization(ApplicationData.getUser());
		final Map<String, MenuItem> lMenuMap = new HashMap<String, MenuBar.MenuItem>();
		for (final MenuFactory lFactory : useCaseManager.getMenus()) {
			final MenuItem lItem = lFactory.createMenu(inMenuBar,
					inSubMenuIcon, getMenuMap(), lCommand, lAuthorization);
			lMenuMap.put(lFactory.getProviderSymbolicName(), lItem);
		}
		ApplicationData.setMenuMap(lMenuMap);
		LOG.debug("Menu created for Ripla."); //$NON-NLS-1$
	}

	/**
	 * Hook for subclasses to provide functionality to be processed after the
	 * user clicked a menu item.
	 * <p>
	 * Subclasses may override.
	 * </p>
	 * 
	 * @param inUseCaseManager
	 *            {@link UseCaseManager}
	 */
	protected void afterMenuClick(final UseCaseManager inUseCaseManager) {
		// do nothing
	}

	private void handleNoTaskFound(final NoControllerFoundException inExc) {
		LOG.error("Configuration error:", inExc); //$NON-NLS-1$
		setContentView(new DefaultRiplaView(inExc));
	}

	private Map<Integer, IMenuCommand> getMenuMap() {
		if (menuMap == null) {
			menuMap = new HashMap<Integer, IMenuCommand>();
		}
		return menuMap;
	}

	private Component createToolbar(
			final ToolbarItemRegistry inToolbarRegistry, final Label inSeparator) {
		final HorizontalLayout outToolbar = new HorizontalLayout();
		outToolbar.setStyleName("ripla-toolbar"); //$NON-NLS-1$
		outToolbar.setWidth("100%"); //$NON-NLS-1$
		outToolbar.setSpacing(true);
		outToolbar.setMargin(false, true, false, true);
		outToolbar.setHeight(22, UNITS_PIXELS);

		final Label lExpand = new Label("&#160;", Label.CONTENT_XHTML);
		lExpand.setWidth("100%");
		outToolbar.addComponent(lExpand);
		outToolbar.setExpandRatio(lExpand, 1);

		final Iterator<IToolbarItem> lItems = inToolbarRegistry
				.getSortedItems().iterator();
		boolean lFirst = true;
		while (lItems.hasNext()) {
			final IToolbarItem lItem = lItems.next();
			final IToolbarItemCreator lFactory = lItem.getCreator();
			final Component lComponent = lFactory == null ? lItem
					.getComponent() : lFactory.createToolbarItem(application,
					ApplicationData.getUser());
			if (lComponent == null) {
				continue;
			}

			if (!lFirst) {
				final Label lSeparator = getSeparator(inSeparator);
				outToolbar.addComponent(lSeparator);
				outToolbar.setComponentAlignment(lSeparator,
						Alignment.MIDDLE_CENTER);
			}
			lFirst = false;

			outToolbar.addComponent(lComponent);
			outToolbar.setComponentAlignment(lComponent,
					Alignment.MIDDLE_CENTER);
			lItem.registerToolbarActionListener(new IToolbarActionListener() { // NOPMD
				@Override
				public void processAction(final IToolbarAction inAction) {
					inAction.run(application, useCaseManager
							.getControllerManager().getEventAdmin());
				}
			});
		}

		return outToolbar;
	}

	/**
	 * We have to clone the separator label defined in the skin.
	 * 
	 * @param inSeparator
	 *            Label
	 * @return {@link Label} the cloned label
	 */
	private Label getSeparator(final Label inSeparator) {
		final Label out = new Label(inSeparator.getValue().toString(),
				inSeparator.getContentMode());
		out.setWidth(inSeparator.getWidth(), inSeparator.getWidthUnits());
		out.setStyleName(inSeparator.getStyleName());
		return out;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ripla.web.interfaces.IBodyComponent#getContentComponent(java.lang
	 * .String)
	 */
	@Override
	public Component getContentComponent(final String inControllerName)
			throws NoControllerFoundException {
		return useCaseManager.getControllerManager().getContent(
				inControllerName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ripla.web.interfaces.IBodyComponent#setContentView(com.vaadin.ui.
	 * Component)
	 */
	@Override
	public void setContentView(final Component inComponent) {
		content.removeAllComponents();
		content.addComponent(inComponent);
	}

	/**
	 * Loads the context menu with the specified set name into the sidebar (i.e.
	 * conext menu) panel.
	 * <p>
	 * This implementation first removes all displayed menu items from the
	 * sidebar. Then it renders the context menu with the specified set name and
	 * passes a parameter object created by
	 * {@link #createParametersForContextMenu()} and adds the rendered component
	 * to the cleaned sidebar panel.
	 * </p>
	 */
	@Override
	public void setContextMenu(final String inMenuSetName,
			final Class<? extends IPluggable> inControllerClass) {
		getSidebar().removeAllComponents();
		final User lUser = ApplicationData.getUser();
		getSidebar().addComponent(
				getUseCaseManager().getContextMenuManager().renderContextMenu(
						inMenuSetName, lUser,
						getUserAdmin().getAuthorization(lUser),
						createParametersForContextMenu(), inControllerClass));
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ripla.web.interfaces.IBodyComponent#showNotification(java.lang.String
	 * , int)
	 */
	@Override
	public final void showNotification(final String inNotification,
			final int inNotificationType) {
		getWindow().showNotification(inNotification, inNotificationType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.interfaces.IBodyComponent#refreshBody()
	 */
	@Override
	public final void refreshBody() {
		layout.removeAllComponents();
		initializeLayout();
		showDefault();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.interfaces.IBodyComponent#showDefault()
	 */
	@Override
	public void showDefault() {
		final List<MenuItem> lMenuItems = menuBar.getItems();
		if (!lMenuItems.isEmpty()) {
			final MenuItem lMenuItem = lMenuItems.get(0);
			lMenuItem.getCommand().menuSelected(lMenuItem);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ripla.web.interfaces.IBodyComponent#close()
	 */
	@Override
	public final void close() {
		application.close();
	}

}

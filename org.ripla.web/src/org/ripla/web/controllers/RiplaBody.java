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
package org.ripla.web.controllers; // NOPMD by Luthiger on 09.09.12 00:48

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.osgi.service.useradmin.Authorization;
import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;
import org.ripla.exceptions.NoControllerFoundException;
import org.ripla.interfaces.IMenuCommand;
import org.ripla.util.ParameterObject;
import org.ripla.web.Constants;
import org.ripla.web.RiplaApplication;
import org.ripla.web.interfaces.IBodyComponent;
import org.ripla.web.interfaces.IPluggable;
import org.ripla.web.interfaces.IToolbarAction;
import org.ripla.web.interfaces.IToolbarActionListener;
import org.ripla.web.interfaces.IToolbarItemCreator;
import org.ripla.web.internal.menu.MenuFactory;
import org.ripla.web.internal.services.ToolbarItemRegistry;
import org.ripla.web.internal.services.UseCaseRegistry;
import org.ripla.web.internal.views.DefaultRiplaView;
import org.ripla.web.services.ISkin;
import org.ripla.web.services.IToolbarItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
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

	private final ISkin skin;
	private final RiplaApplication application;

	private MenuBar menuBar;

	/**
	 * Private constructor.
	 * 
	 * @param inSkin
	 *            {@link ISkin}
	 * @param inApplication
	 *            {@link RiplaApplication}
	 */
	private RiplaBody(final ISkin inSkin, final RiplaApplication inApplication) {
		super();

		skin = inSkin;
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
	 * @param inApplication
	 *            {@link RiplaApplication}
	 * @return {@link RiplaBody}
	 */
	public static RiplaBody createInstance(final ISkin inSkin,
			final RiplaApplication inApplication) {
		final RiplaBody outBody = new RiplaBody(inSkin, inApplication);
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
		Page.getCurrent().setTitle(application.getAppName());
		if (skin.hasHeader()) {
			final Component lHeader = skin.getHeader(application.getAppName());
			layout.addComponent(lHeader);
			layout.setExpandRatio(lHeader, 0);
		}

		if (skin.hasToolBar()) {
			final Component lToolbar = createToolbar(skin.getToolbarSeparator());
			layout.addComponent(lToolbar);
		}

		if (skin.hasMenuBar()) {
			final Component lMenubar = createMenubar(skin.getMenuBarMedium(),
					skin.getMenuBar(), skin.getSubMenuIcon());
			layout.addComponent(lMenubar);
		}

		final HorizontalSplitPanel lPanel = new HorizontalSplitPanel();
		layout.addComponent(lPanel);
		layout.setExpandRatio(lPanel, 1);
		lPanel.setSplitPosition(10, Unit.PERCENTAGE);
		// lPanel.setHeight(SIZE_UNDEFINED, Unit.PIXELS);
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

		layout.setSizeFull();
	}

	protected final UserAdmin getUserAdmin() {
		return UseCaseRegistry.INSTANCE.getUserAdmin();
	}

	protected final Layout getSidebar() {
		return sidebar;
	}

	private Component createMenubar(final HorizontalLayout inMenuBarMedium,
			final HorizontalLayout inMenuBarLayout, final Resource inSubMenuIcon) {
		final HorizontalLayout outComponent = inMenuBarMedium == null ? getDftMenuBarLayout()
				: inMenuBarMedium;

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
		outLayout.setMargin(new MarginInfo(false, false, false, true));
		outLayout.setWidth("100%"); //$NON-NLS-1$
		outLayout.setHeight(32, Unit.PIXELS);
		return outLayout;
	}

	private void createMenu(final MenuBar inMenuBar,
			final Resource inSubMenuIcon) {
		final MenuBar.Command lCommand = new MenuBar.Command() {
			@Override
			public void menuSelected(final MenuItem inSelected) {
				final IMenuCommand lAction = getMenuMap().get(
						inSelected.getId());
				afterMenuClick();
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

		final Authorization lAuthorization = UseCaseRegistry.INSTANCE
				.getUserAdmin().getAuthorization(getUser());
		final Map<String, MenuItem> lMenuMap = new HashMap<String, MenuBar.MenuItem>();
		for (final MenuFactory lFactory : UseCaseRegistry.INSTANCE.getMenus()) {
			final MenuItem lItem = lFactory.createMenu(inMenuBar,
					inSubMenuIcon, getMenuMap(), lCommand, lAuthorization);
			lMenuMap.put(lFactory.getProviderSymbolicName(), lItem);
		}
		// we set the menu map to the session to access it later to mark the
		// active menu
		try {
			VaadinSession.getCurrent().getLockInstance().lock();
			VaadinSession.getCurrent().setAttribute(Constants.SA_MENU_MAP,
					lMenuMap);
		} finally {
			VaadinSession.getCurrent().getLockInstance().unlock();
		}
		LOG.debug("Menu created for Ripla."); //$NON-NLS-1$
	}

	/**
	 * Hook for subclasses to provide functionality to be processed after the
	 * user clicked a menu item.
	 * <p>
	 * Subclasses may override.
	 * </p>
	 */
	protected void afterMenuClick() {
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

	private Component createToolbar(final Label inSeparator) {
		final HorizontalLayout outToolbar = new HorizontalLayout();
		outToolbar.setStyleName("ripla-toolbar"); //$NON-NLS-1$
		outToolbar.setWidth("100%"); //$NON-NLS-1$
		outToolbar.setSpacing(true);
		outToolbar.setMargin(new MarginInfo(false, true, false, true));
		outToolbar.setHeight(22, Unit.PIXELS);

		final Label lExpand = new Label("&#160;", ContentMode.HTML);
		lExpand.setWidth("100%");
		outToolbar.addComponent(lExpand);
		outToolbar.setExpandRatio(lExpand, 1);

		final Iterator<IToolbarItem> lItems = ToolbarItemRegistry.INSTANCE
				.getSortedItems().iterator();
		boolean lFirst = true;
		while (lItems.hasNext()) {
			final IToolbarItem lItem = lItems.next();
			final IToolbarItemCreator lFactory = lItem.getCreator();
			final Component lComponent = lFactory == null ? lItem
					.getComponent() : lFactory.createToolbarItem(application,
					getUser());
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
					inAction.run();
				}
			});
		}

		return outToolbar;
	}

	private User getUser() {
		try {
			VaadinSession.getCurrent().getLockInstance().lock();
			return VaadinSession.getCurrent().getAttribute(User.class);
		} finally {
			VaadinSession.getCurrent().getLockInstance().unlock();
		}
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

	@Override
	public Component getContentComponent(final String inControllerName)
			throws NoControllerFoundException {
		return UseCaseRegistry.INSTANCE.getControllerManager().getContent(
				inControllerName);
	}

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
		final User lUser = getUser();
		getSidebar().addComponent(
				UseCaseRegistry.INSTANCE.getContextMenuManager()
						.renderContextMenu(inMenuSetName, lUser,
								getUserAdmin().getAuthorization(lUser),
								createParametersForContextMenu(),
								inControllerClass));
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

	@Override
	public final void refreshBody() {
		layout.removeAllComponents();
		initializeLayout();
		showDefault();
	}

	/*
	 * Subclasses may override.
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

	@Override
	public final void close() {
		application.close();
	}

}

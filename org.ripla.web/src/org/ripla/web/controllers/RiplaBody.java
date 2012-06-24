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

package org.ripla.web.controllers;

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
import org.ripla.web.util.LabelHelper;
import org.ripla.web.util.ParameterObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * <p>The application's body component consisting of the toolbar, the menubar, the sidebar panel and the main content panel.</p>
 * <p>Subclasses may override the following methods:
 * <ul>
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
public class RiplaBody extends CustomComponent implements IBodyComponent {
	public static final Logger LOG = LoggerFactory.getLogger(RiplaBody.class);
	
	private Map<Integer, IMenuCommand> menuMap;
	
	private VerticalLayout layout;
	private VerticalLayout sidebar;
	private VerticalLayout content;

	private UseCaseManager useCaseManager;
	private ISkin skin;
	private ToolbarItemRegistry toolbarRegistry;
	private RiplaApplication application;

	private MenuBar menuBar;

	/**
	 * RiplaBody constructor.
	 * 
	 * @param inSkin {@link ISkin}
	 * @param inToolbarRegistry {@link ToolbarItemRegistry}
	 * @param inUseCaseManager {@link UseCaseManager}
	 * @param inApplication {@link RiplaApplication}
	 */
	public RiplaBody(ISkin inSkin, ToolbarItemRegistry inToolbarRegistry, UseCaseManager inUseCaseManager, RiplaApplication inApplication) {
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

		initialize(inSkin, inToolbarRegistry);
	}

	/**
	 * @param inSkin
	 * @param inToolbarRegistry
	 */
	private void initialize(ISkin inSkin, ToolbarItemRegistry inToolbarRegistry) {
		if (inSkin.hasToolBar()) {
			Component lToolbar = createToolbar(inToolbarRegistry, inSkin.getToolbarSeparator());
			layout.addComponent(lToolbar);
		}
		
		if (inSkin.hasMenuBar()) {
			Component lMenubar = createMenubar(inSkin.getMenuBarComponent(), inSkin.getMenuBarLayout());
			layout.addComponent(lMenubar);
		}
		
		HorizontalSplitPanel lPanel = new HorizontalSplitPanel();
		lPanel.setSplitPosition(10, Sizeable.UNITS_PERCENTAGE);
		lPanel.setHeight(SIZE_UNDEFINED, 0);
		lPanel.setStyleName("ripla-split"); //$NON-NLS-1$

		sidebar = new VerticalLayout();
		sidebar.setStyleName("ripla-sidebar");
		lPanel.setFirstComponent(sidebar);
		//TODO: do we need this
		sidebar.addComponent(LabelHelper.createLabel("&#160;", "ripla-sidebar-text"));
		
		content = new VerticalLayout();
		content.setStyleName("ripla-content");
		lPanel.setSecondComponent(content);
		content.setMargin(true);
		lPanel.setSizeFull();
		
		layout.addComponent(lPanel);
		layout.setExpandRatio(lPanel, 1);
	}	
	
	protected UseCaseManager getUseCaseManager() {
		return useCaseManager;
	}
	
	protected UserAdmin getUserAdmin() {
		return useCaseManager.getUserAdmin();
	}
	
	protected Layout getSidebar() {
		return sidebar;
	}

	private Component createMenubar(HorizontalLayout inComponent, HorizontalLayout inMenuBarLayout) {
		HorizontalLayout outComponent = inComponent == null ? getDftMenuBarLayout() : inComponent;

		menuBar = new MenuBar();
		menuBar.setAutoOpen(true);
		menuBar.setStyleName("ripla-menu"); //$NON-NLS-1$
		createMenu(menuBar);
		if (inMenuBarLayout == null) {
			outComponent.addComponent(menuBar);			
		}
		else {
			inMenuBarLayout.removeAllComponents();
			inMenuBarLayout.addComponent(menuBar);
		}

		return outComponent;
	}
	
	protected HorizontalLayout getDftMenuBarLayout() {
		HorizontalLayout outLayout = new HorizontalLayout();
		outLayout.setStyleName("ripla-menubar"); //$NON-NLS-1$
		outLayout.setMargin(false, false, false, true);
		outLayout.setWidth("100%"); //$NON-NLS-1$
		outLayout.setHeight(32, UNITS_PIXELS);
		return outLayout;
	}
	
	private void createMenu(MenuBar inMenuBar) {
		MenuBar.Command lCommand = new MenuBar.Command() {			
			@Override
			public void menuSelected(MenuItem inSelected) {
				IMenuCommand lAction = getMenuMap().get(inSelected.getId());
				afterMenuClick(getUseCaseManager());
				if (lAction != null) {
					try {
						setContentView(getContentComponent(lAction.getControllerName()));
					} 
					catch (NoControllerFoundException exc) {
						handleNoTaskFound(exc);
					}
				}
			}
		};
		
		Authorization lAuthorization = useCaseManager.getUserAdmin().getAuthorization(ApplicationData.getUser());
		Map<String, MenuItem> lMenuMap = new HashMap<String, MenuBar.MenuItem>();
		for (MenuFactory lFactory : useCaseManager.getMenus()) {
			MenuItem lItem = lFactory.createMenu(inMenuBar, getMenuMap(), lCommand, lAuthorization);
			lMenuMap.put(lFactory.getProviderSymbolicName(), lItem);
		}
		ApplicationData.setMenuMap(lMenuMap);
		LOG.debug("Menu created for Ripla."); //$NON-NLS-1$
	}
	
	/**
	 * Hook for subclasses to provide functionality to be processed after the user clicked a menu item.
	 * 
	 * @param inUseCaseManager {@link UseCaseManager}
	 */
	protected void afterMenuClick(UseCaseManager inUseCaseManager) {
		// do nothing
	}

	private void handleNoTaskFound(NoControllerFoundException inExc) {
		LOG.error("Configuration error:", inExc); //$NON-NLS-1$
		setContentView(new DefaultRiplaView(inExc));
	}

	private Map<Integer, IMenuCommand> getMenuMap() {
		if (menuMap == null) {
			menuMap = new HashMap<Integer, IMenuCommand>();
		}
		return menuMap;
	}

	private Component createToolbar(ToolbarItemRegistry inToolbarRegistry, Label inSeparator) {
		HorizontalLayout outToolbar = new HorizontalLayout();
		outToolbar.setStyleName("ripla-toolbar"); //$NON-NLS-1$
		outToolbar.setWidth("100%"); //$NON-NLS-1$
		outToolbar.setSpacing(true);
		outToolbar.setMargin(false, true, false, true);
		outToolbar.setHeight(22, UNITS_PIXELS);
		
		Label lExpand = new Label("&#160;", Label.CONTENT_XHTML);
		lExpand.setWidth("100%");
		outToolbar.addComponent(lExpand);
		outToolbar.setExpandRatio(lExpand, 1);
		
		Iterator<IToolbarItem> lItems = inToolbarRegistry.getSortedItems().iterator();
		boolean lFirst = true;
		while (lItems.hasNext()) {
			IToolbarItem lItem = lItems.next();
			IToolbarItemCreator lFactory = lItem.getCreator();
			Component lComponent = lFactory != null ? lFactory.createToolbarItem(application, ApplicationData.getUser()) : lItem.getComponent();
			if (lComponent == null) {
				continue;
			}
			
			if (!lFirst) {
				Label lSeparator = getSeparator(inSeparator);
				outToolbar.addComponent(lSeparator);
				outToolbar.setComponentAlignment(lSeparator, Alignment.MIDDLE_CENTER);
			}
			lFirst = false;
			
			outToolbar.addComponent(lComponent);
			outToolbar.setComponentAlignment(lComponent, Alignment.MIDDLE_CENTER);
			lItem.registerToolbarActionListener(new IToolbarActionListener() {
				@Override
				public void processAction(IToolbarAction inAction) {
					inAction.run(application, useCaseManager.getControllerManager().getEventAdmin());
				}
			});
		}

		return outToolbar;
	}
	
	/**
	 * We have to clone the separator label defined in the skin.
	 * 
	 * @param inSeparator Label
	 * @return {@link Label} the cloned label
	 */
	private Label getSeparator(Label inSeparator) {
		Label out = new Label(inSeparator.getValue().toString(), inSeparator.getContentMode());
		out.setWidth(inSeparator.getWidth(), inSeparator.getWidthUnits());
		out.setStyleName(inSeparator.getStyleName());
		return out;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.ripla.web.interfaces.IBodyComponent#getContentComponent(java.lang.String)
	 */
	public Component getContentComponent(String inControllerName) throws NoControllerFoundException {
		return useCaseManager.getControllerManager().getContent(inControllerName);
	}

	/*
	 * (non-Javadoc)
	 * @see org.ripla.web.interfaces.IBodyComponent#setContentView(com.vaadin.ui.Component)
	 */
	public void setContentView(Component inComponent) {
		content.removeAllComponents();
		content.addComponent(inComponent);
	}

	/* (non-Javadoc)
	 * @see org.ripla.web.interfaces.IBodyComponent#setContextMenu(com.vaadin.ui.Component)
	 */
	@Override
	public void setContextMenu(String inMenuSetName) {
		getSidebar().removeAllComponents();
		User lUser = ApplicationData.getUser();
		getSidebar().addComponent(getUseCaseManager().getContextMenuManager().renderContextMenu(inMenuSetName, lUser, 
				getUserAdmin().getAuthorization(lUser), 
				createParametersForContextMenu()));
	}
	
	/**
	 * Returns the parameter object to pass context parameters that can be evaluated to decide whether a context menu item should be displayed or hidden.<br />
	 * By default, an empty parameter object is created.<br />
	 * Subclasses may override.
	 * 
	 * @return {@link ParameterObject} 
	 */
	protected ParameterObject createParametersForContextMenu() {
		return new ParameterObject();
	}

	/* (non-Javadoc)
	 * @see org.ripla.web.interfaces.IBodyComponent#showNotification(java.lang.String, int)
	 */
	@Override
	public void showNotification(String inNotification, int inNotificationType) {
		getWindow().showNotification(inNotification, inNotificationType);
	}

	/* (non-Javadoc)
	 * @see org.ripla.web.interfaces.IBodyComponent#refreshBody()
	 */
	@Override
	public void refreshBody() {
		layout.removeAllComponents();
		initialize(skin, toolbarRegistry);
		showDefault();
	}

	/*
	 * (non-Javadoc)
	 * @see org.ripla.web.interfaces.IBodyComponent#showDefault()
	 */
	public void showDefault() {
		List<MenuItem> lMenuItems = menuBar.getItems();
		if (lMenuItems.size() != 0) {
			MenuItem lMenuItem = lMenuItems.get(0);
			lMenuItem.getCommand().menuSelected(lMenuItem);
		}		
	}
	
	/* (non-Javadoc)
	 * @see org.ripla.web.interfaces.IBodyComponent#close()
	 */
	@Override
	public void close() {
		application.close();
	}

}

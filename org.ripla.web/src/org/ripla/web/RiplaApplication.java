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

package org.ripla.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventHandler;
import org.osgi.service.prefs.PreferencesService;
import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;
import org.ripla.web.controllers.RiplaBody;
import org.ripla.web.interfaces.IAppConfiguration;
import org.ripla.web.interfaces.IAuthenticator;
import org.ripla.web.interfaces.IWorkflowListener;
import org.ripla.web.internal.services.ApplicationData;
import org.ripla.web.internal.services.PermissionHelper;
import org.ripla.web.internal.services.RiplaEventHandler;
import org.ripla.web.internal.services.SkinRegistry;
import org.ripla.web.internal.services.ToolbarItemRegistry;
import org.ripla.web.internal.services.UseCaseManager;
import org.ripla.web.internal.views.RiplaLogin;
import org.ripla.web.services.IExtendibleMenuContribution;
import org.ripla.web.services.IPermissionEntry;
import org.ripla.web.services.ISkin;
import org.ripla.web.services.IToolbarItem;
import org.ripla.web.services.IUseCase;
import org.ripla.web.util.PreferencesHelper;
import org.ripla.web.util.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

/**
 * <p>The base class of all web applications using the Ripla platform.</p>
 * <p>Subclasses should override the method {@link #RiplaApplication.createWindow()}.
 * </p>
 * <p>Subclasses may override the following methods:<br />
 * <ul>
 * <li>{@link #getAppConfiguration()}</li>
 * <li>{@link #createBodyView(ISkin)}</li>
 * <li>{@link #createPreferencesHelper()}</li>
 * <li>{@link #beforeLogin()}</li>
 * <li>{@link #workflowExit()}</li>
 * </ul>
 * </p> 
 *
 * @author Luthiger
 */
@SuppressWarnings("serial")
public class RiplaApplication extends Application implements EventHandler, HttpServletRequestListener, IWorkflowListener {
	private static final Logger LOG = LoggerFactory.getLogger(RiplaApplication.class);
	
	private PreferencesHelper preferences = createPreferencesHelper();
	private SkinRegistry skinRegistry = new SkinRegistry(preferences);
	private ToolbarItemRegistry toolbarRegistry = new ToolbarItemRegistry();
	private UseCaseManager useCaseHelper = new UseCaseManager();
	private RiplaEventHandler eventHandler = new RiplaEventHandler();
	private PermissionHelper permissionHelper = new PermissionHelper();
	
	private String requestURL;	
	private RequestHandler requestHandler;
	private Layout bodyView;

	/* (non-Javadoc)
	 * @see com.vaadin.Application#init()
	 */
	@Override
	public final void init() {
		ApplicationData.create(this);
		
		//synchronize language settings
		ApplicationData.initLocale(preferences.getLocale(getLocale()));
		
		Window lWindow = createWindow();
		ApplicationData.setWindow(lWindow);
		ApplicationData.setPreferences(preferences);
		
		useCaseHelper.registerContextMenus();
		if (!initializeLayout(lWindow, getAppConfiguration())) {
			return;
		}
	}
	
	/**
	 * Subclasses may override to provide their own <code>PreferencesHelper</code>.
	 * 
	 * @return PreferencesHelper
	 */
	protected PreferencesHelper createPreferencesHelper() {
		return new PreferencesHelper();
	}
	
	/**
	 * Returns the configuration object to configure the application.<br />
	 * Subclasses may override.
	 * 
	 * @return {@link IAppConfiguration} the configuration object.
	 */
	protected IAppConfiguration getAppConfiguration() {
		return new IAppConfiguration() {
			@Override
			public String getWelcome() {
				return null;
			}
			@Override
			public String getDftSkinID() {
				return SkinRegistry.DFT_SKIN_ID;
			}
			@Override
			public IAuthenticator getLoginAuthenticator() {
				return null;
			}
		};
	}

	private boolean initializeLayout(Window inMain, IAppConfiguration inConfiguration) {
		inMain.setStyleName("ripla-window"); //$NON-NLS-1$
		inMain.addListener(new Window.CloseListener() {			
			@Override
			public void windowClose(CloseEvent inEvent) {
				close();
			}
		});
		
		requestHandler = setRequestHandler(inMain);
		skinRegistry.setDefaultSkin(inConfiguration.getDftSkinID());
		ISkin lSkin = skinRegistry.getActiveSkin();

		setTheme(lSkin.getSkinID());
		inMain.getContent().setSizeFull();

		VerticalLayout lLayout = new VerticalLayout();
		lLayout.setSizeFull();
		lLayout.setStyleName("ripla-main");
		getMainWindow().setContent(lLayout);
		
		if (lSkin.hasHeader()) {
			Component lHeader = lSkin.getHeader();
			lLayout.addComponent(lHeader);
			lLayout.setExpandRatio(lHeader, 0);
		}
		
		bodyView = createBody();
		lLayout.addComponent(bodyView);
		lLayout.setExpandRatio(bodyView, 1);
		
		if (!beforeLogin(inMain, this)) {
			return false;
		}
		
		if (inConfiguration.getLoginAuthenticator() != null) {
			bodyView.addComponent(createLoginView(inConfiguration));
		}
		else {
			bodyView.addComponent(createBodyView(lSkin));
		}
		
		if (lSkin.hasFooter()) {
			Component lFooter = lSkin.getFooter();
			lLayout.addComponent(lFooter);
			lLayout.setExpandRatio(lFooter, 0);
		}
		return true;
	}
	
	/**
	 * Callback method to display the application's views after the user has successfully logged in.
	 * 
	 * @param inUser {@link User} the user instance
	 */
	public void showAfterLogin(User inUser) {
		ApplicationData.setUser(inUser);
		refreshBody();
	}
	
	/**
	 * Hook for application configuration.
	 * 
	 * @param inMain {@link Window} the application's main window
	 * @param inWorkflowListener {@link IWorkflowListener} the listener of the application workflow configuration
	 * @return boolean <code>true</code> in case of no there's no need of application configuration and, therefore, 
	 * the startup process can continue, <code>false</code> if the startup is handed over to the 
	 * application configuration workflow.
	 */
	protected boolean beforeLogin(Window inMain, IWorkflowListener inWorkflowListener) {
		return true;
	}

	private RequestHandler setRequestHandler(Window inMain) {
		RequestHandler out = new RequestHandler(requestURL, useCaseHelper);
		inMain.addParameterHandler(out);
		return out;
	}
	
	private Layout createBody() {
		Layout outBody = new VerticalLayout();
		outBody.setStyleName("ripla-body");
		outBody.setSizeFull();
		return outBody;
	}

	/**
	 * Refreshes the body component.
	 */
	public void refreshBody() {
		bodyView.removeAllComponents();
		bodyView.addComponent(createBodyView(skinRegistry.getActiveSkin()));
	}
	
	/**
	 * Creates the application's body view.<br />
	 * Subclasses may override.
	 * 
	 * @param inSkin {@link ISkin} the actual application skin
	 * @return {@link Component} the application's body view
	 */
	protected Component createBodyView(ISkin inSkin) {
		RiplaBody out = new RiplaBody(inSkin, toolbarRegistry, useCaseHelper, this);
		eventHandler.setBodyComponent(out);
		
		if (!requestHandler.process(out)) {
			out.showDefault();
		}
		return out;
	}
	
	/**
	 * Creates the application's login view.<br />
	 * Subclasses may override.
	 * 
	 * @param inConfiguration {@link IAppConfiguration} the application's configuration object
	 * @return {@link Component} the application's login view
	 */
	private Component createLoginView(IAppConfiguration inConfiguration) {
		RiplaLogin out = new RiplaLogin(inConfiguration, this, useCaseHelper.getUserAdmin());
		return out;
	}

	/**
	 * Creates the application's main window.<br />
	 * Subclasses should override.
	 * 
	 * @return {@link Window} the application's main window
	 */
	protected Window createWindow() {
		Window outWindow = new Window("Ripla");
		setMainWindow(outWindow);
		return outWindow;
	}

	/* (non-Javadoc)
	 * @see org.osgi.service.event.EventHandler#handleEvent(org.osgi.service.event.Event)
	 */
	@Override
	public void handleEvent(Event inEvent) {
		eventHandler.handleEvent(inEvent);
	}

	/*
	 * (non-Javadoc)
	 * @see com.vaadin.terminal.gwt.server.HttpServletRequestListener#onRequestStart(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void onRequestStart(HttpServletRequest inRequest, HttpServletResponse inResponse) {
		requestURL = new String(inRequest.getRequestURL());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.vaadin.terminal.gwt.server.HttpServletRequestListener#onRequestEnd(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void onRequestEnd(HttpServletRequest inRequest, HttpServletResponse inResponse) {
		//intentionally left empty		
	}

	/**
	 * Subclasses may override.
	 * 
	 * @see org.ripla.web.interfaces.IWorkflowListener#workflowExit(int, java.lang.String)
	 */
	@Override
	public void workflowExit(int inReturnCode, String inMessage) {
		// intentionally left empty
	}
	
	/**
	 * Allows access to the application's preferences.
	 * 
	 * @return {@link PreferencesHelper}
	 */
	public PreferencesHelper getPreferences() {
		return preferences;
	}
	
	/**
	 * <p>Subclasses that want to make use of the OSGi user admin service functionality
	 * may trigger initialization of registered permissions.</p>
	 * 
	 * <p>This will create a permission group for each registered <code>IPermissionEntry</code> and
	 * add the members defined in those permission instances.</p>
	 * 
	 * <p>This requires that such member instances exist already. 
	 * Thus, subclasses have to prepare groups (i.e. roles) that can act as members for the registered 
	 * permission groups before the registered permission instances are processed.</p> 
	 * <p>This method is best called in the subclass's <code>setUserAdmin(UserAdmin inUserAdmin)</code> method:
	 * 
	 * <pre>public void setUserAdmin(UserAdmin inUserAdmin) {
	 *    super.setUserAdmin(inUserAdmin);
	 *    Group lAdministrators = (Group)inUserAdmin.createRole("ripla.admin", Role.GROUP);
	 *    initializePermissions();
	 *}</pre></p>
	 */
	protected void initializePermissions() {
		permissionHelper.initializePermissions();
	}
	
//--- OSGi DS bind and unbind methods --- 
	
	public void setPreferences(PreferencesService inPreferences) {
		preferences.setPreferences(inPreferences);
		LOG.debug("The OSGi preferences service is made available.");
	}
	
	public void unsetPreferences(PreferencesService inPreferences) {
		preferences.dispose();
		LOG.debug("Removed the OSGi preferences service.");
	}
	
	public void setEventAdmin(EventAdmin inEventAdmin) {
		useCaseHelper.setEventAdmin(inEventAdmin);
		LOG.debug("The OSGi event admin service is made available.");
	}
	
	public void unsetEventAdmin(EventAdmin inEventAdmin) {
		useCaseHelper.setEventAdmin(null);		
		LOG.debug("Removed the OSGi event admin service.");
	}
	
	public void setUserAdmin(UserAdmin inUserAdmin) {		
		useCaseHelper.setUserAdmin(inUserAdmin);
		permissionHelper.setUserAdmin(inUserAdmin);
		LOG.debug("The OSGi user admin service is made available.");
	}
	
	public void unsetUserAdmin(UserAdmin inUserAdmin) {
		useCaseHelper.setUserAdmin(null);
		permissionHelper.setUserAdmin(null);
		LOG.debug("Removed the OSGi user admin service is made available.");
	}

	public void registerSkin(ISkin inSkin) {
		LOG.debug("Registered skin '{}'.", inSkin.getSkinID());
		skinRegistry.registerSkin(inSkin);
	}
	
	public void unregisterSkin(ISkin inSkin) {
		LOG.debug("Unregistered skin '{}'.", inSkin.getSkinID());
		skinRegistry.unregisterSkin(inSkin);
	}
	
	public void registerToolbarItem(IToolbarItem inItem) {
		LOG.debug("Registered the toolbar item '{}'.", inItem);
		toolbarRegistry.registerToolbarItem(inItem);
	}

	public void unregisterToolbarItem(IToolbarItem inItem) {
		LOG.debug("Unregistered the toolbar item '{}'.", inItem);
		toolbarRegistry.unregisterToolbarItem(inItem);		
	}
	
	public void addUseCase(IUseCase inUseCase) {
		LOG.debug("Added use case {}.", inUseCase);
		useCaseHelper.addUseCase(inUseCase);
	}
	
	public void removeUseCase(IUseCase inUseCase) {
		LOG.debug("Removed use case {}.", inUseCase);
		useCaseHelper.removeUseCase(inUseCase);		
	}
	
	public void registerMenuContribution(IExtendibleMenuContribution inMenuContribution) {
		LOG.debug("Registered extendible menu contribution '{}'.", inMenuContribution.getExtendibleMenuID());
		useCaseHelper.registerMenuContribution(inMenuContribution);
	}
	
	public void unregisterMenuContribution(IExtendibleMenuContribution inMenuContribution) {
		LOG.debug("Unregistered extendible menu contribution '{}'.", inMenuContribution.getExtendibleMenuID());
		useCaseHelper.unregisterMenuContribution(inMenuContribution);		
	}
	
	public void registerPermission(IPermissionEntry inPermission) {
		LOG.debug("Registered permission '{}'.", inPermission.getPermissionName());
		permissionHelper.addPermission(inPermission);
	}
	
	public void unregisterPermission(IPermissionEntry inPermission) {
		LOG.debug("Unregistered permission '{}'.", inPermission.getPermissionName());
		permissionHelper.removePermission(inPermission);		
	}

}
